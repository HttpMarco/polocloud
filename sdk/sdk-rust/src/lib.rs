pub mod polocloud {
    tonic::include_proto!("dev.httpmarco.polocloud.v1.proto");
}

use std::collections::HashMap;
use std::env;
use polocloud::service_controller_client::ServiceControllerClient;
use polocloud::group_controller_client::GroupControllerClient;
use polocloud::event_provider_client::EventProviderClient;
use polocloud::{
    ServiceFindRequest,
    FindGroupRequest, GroupSnapshot,
    EventSubscribeRequest
};

#[derive(Debug, Clone)]
pub struct Service {
    pub group_name: String,
    pub id: u32,
    pub hostname: String,
    pub port: u32,
    pub state: ServiceState,
    pub server_type: GroupType,
    pub properties: HashMap<String, String>,
}

#[derive(Debug, Clone)]
pub struct Group {
    snapshot: GroupSnapshot,
}

impl Group {
    pub fn new(snapshot: GroupSnapshot) -> Self {
        Self { snapshot }
    }

    pub fn name(&self) -> &str {
        &self.snapshot.name
    }

    pub fn minimum_memory(&self) -> i32 {
        self.snapshot.minimum_memory
    }

    pub fn maximum_memory(&self) -> i32 {
        self.snapshot.maximum_memory
    }

    pub fn minimum_online(&self) -> i32 {
        self.snapshot.minimum_online
    }

    pub fn maximum_online(&self) -> i32 {
        self.snapshot.maximum_online
    }

    pub fn properties(&self) -> &HashMap<String, String> {
        &self.snapshot.properties
    }
}

pub struct SdkGrpcClient {
    pub channel: Option<Channel>,
}

impl SdkGrpcClient {
    pub async fn new() -> Result<Self, Box<dyn std::error::Error>> {
        let agent_port = env::var("agent_port")
            .unwrap_or_else(|_| "8932".to_string())
            .parse::<u16>()?;

        let endpoint = Endpoint::from_shared(format!("http://127.0.0.1:{}", agent_port))?;
        let channel = endpoint.connect().await?;

        Ok(Self {
            channel: Some(channel),
        })
    }
}

pub struct ServiceProvider {
    service_stub: ServiceControllerClient<Channel>,
}

impl ServiceProvider {
    pub fn new(channel: Channel) -> Self {
        Self {
            service_stub: ServiceControllerClient::new(channel),
        }
    }

    pub async fn find(&mut self) -> Result<Vec<Service>, Status> {
        let request = Request::new(ServiceFindRequest { name: "".to_string() });
        let response = self.service_stub.find(request).await?;

        let services = response.into_inner().services
            .into_iter()
            .map(|proto_service| Service {
                group_name: proto_service.group_name,
                id: proto_service.id,
                hostname: proto_service.hostname,
                port: proto_service.port,
                state: ServiceState::try_from(proto_service.state).unwrap_or(ServiceState::Stopped),
                server_type: GroupType::try_from(proto_service.server_type).unwrap_or(GroupType::Server),
                properties: proto_service.properties,
            })
            .collect();

        Ok(services)
    }

    pub async fn find_async(&mut self) -> Result<Vec<Service>, Status> {
        // In Rust, async is the default, so this is the same as find()
        self.find().await
    }
}

pub struct GroupProvider {
    group_stub: GroupControllerClient<Channel>,
}

impl GroupProvider {
    pub fn new(channel: Channel) -> Self {
        Self {
            group_stub: GroupControllerClient::new(channel),
        }
    }

    pub async fn find(&mut self) -> Result<Vec<Group>, Status> {
        let request = Request::new(FindGroupRequest { name: "".to_string()});
        let response = self.group_stub.find(request).await?;

        let groups = response.into_inner().groups
            .into_iter()
            .map(|group_snapshot| Group::new(group_snapshot))
            .collect();

        Ok(groups)
    }

    pub async fn find_by_name(&mut self, name: String) -> Result<Option<Group>, Status> {
        let request = Request::new(FindGroupRequest { name });
        let response = self.group_stub.find(request).await?;

        let group = response.into_inner().groups
            .into_iter()
            .map(|group_snapshot| Group::new(group_snapshot))
            .next();

        Ok(group)
    }
}

pub trait Event: Send + Sync {
    fn event_name() -> &'static str;
}

pub struct EventProvider {
    event_stub: EventProviderClient<Channel>,
    service_name: String,
}

impl EventProvider {
    pub fn new(channel: Channel, service_name: String) -> Self {
        Self {
            event_stub: EventProviderClient::new(channel),
            service_name,
        }
    }

    pub async fn call<T: Event>(&mut self, _event: T) -> Result<(), Status> {
        // TODO: Implement event calling
        todo!("Not yet implemented")
    }

    pub async fn subscribe<T, F>(&mut self, event_name: &str, mut callback: F) -> Result<(), Status>
    where
        T: Event + serde::de::DeserializeOwned + Send + 'static,
        F: FnMut(T) + Send + 'static,
    {
        let request = Request::new(EventSubscribeRequest {
            service_name: self.service_name.clone(),
            event_name: event_name.to_string(),
        });

        let mut stream = self.event_stub.subscribe(request).await?.into_inner();

        tokio::spawn(async move {
            while let Some(event_context_result) = stream.next().await {
                match event_context_result {
                    Ok(event_context) => {
                        match serde_json::from_str::<T>(&event_context.event_data) {
                            Ok(event) => callback(event),
                            Err(e) => eprintln!("Error deserializing event: {}", e),
                        }
                    }
                    Err(e) => {
                        eprintln!("Error while subscribing to event: {}", e);
                        break;
                    }
                }
            }
        });

        Ok(())
    }
}

pub struct Polocloud {
    grpc_client: SdkGrpcClient,
    service_provider: Arc<Mutex<ServiceProvider>>,
    group_provider: Arc<Mutex<GroupProvider>>,
    event_provider: Arc<Mutex<EventProvider>>,
}

impl Polocloud {
    pub async fn new() -> Result<Self, Box<dyn std::error::Error>> {
        let grpc_client = SdkGrpcClient::new().await?;
        let channel = grpc_client.channel.as_ref().unwrap().clone();

        let service_name = env::var("service-name")
            .unwrap_or_else(|_| "default-service".to_string());

        Ok(Self {
            service_provider: Arc::new(Mutex::new(ServiceProvider::new(channel.clone()))),
            group_provider: Arc::new(Mutex::new(GroupProvider::new(channel.clone()))),
            event_provider: Arc::new(Mutex::new(EventProvider::new(channel, service_name))),
            grpc_client,
        })
    }

    pub fn service_provider(&self) -> Arc<Mutex<ServiceProvider>> {
        self.service_provider.clone()
    }

    pub fn group_provider(&self) -> Arc<Mutex<GroupProvider>> {
        self.group_provider.clone()
    }

    pub fn event_provider(&self) -> Arc<Mutex<EventProvider>> {
        self.event_provider.clone()
    }

    pub fn available(&self) -> bool {
        self.grpc_client.channel.is_some()
    }

    pub fn self_service_name(&self) -> String {
        env::var("service-name").unwrap_or_else(|_| "default-service".to_string())
    }
}

// Singleton pattern implementation
use std::sync::{Arc, Mutex, OnceLock};
use tonic::{Request, Status};
use tonic::codegen::tokio_stream::StreamExt;
use tonic::transport::{Channel, Endpoint};
use crate::polocloud::{GroupType, ServiceState};

static POLOCLOUD_INSTANCE: OnceLock<Arc<Mutex<Polocloud>>> = OnceLock::new();

impl Polocloud {
    pub async fn instance() -> Arc<Mutex<Polocloud>> {
        if let Some(instance) = POLOCLOUD_INSTANCE.get() {
            instance.clone()
        } else {
            let polocloud = Polocloud::new().await.expect("Failed to create Polocloud instance");
            let instance = Arc::new(Mutex::new(polocloud));
            match POLOCLOUD_INSTANCE.set(instance.clone()) {
                Ok(_) => {}
                Err(_) => {
                    println!("Failed to set INSTANCE into OnceLock")
                }
            };
            instance
        }
    }
}