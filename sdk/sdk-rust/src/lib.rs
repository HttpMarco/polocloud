mod proto_deserializers;
pub mod events;
pub mod structs;
pub mod providers;

pub mod polocloud {
    tonic::include_proto!("dev.httpmarco.polocloud.v1.proto");
}

use std::env;

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



pub trait Event: Send + Sync {
    fn event_name() -> String;
}


pub struct Polocloud {
    grpc_client: SdkGrpcClient,
    service_provider: Arc<Mutex<ServiceProvider>>,
    group_provider: Arc<Mutex<GroupProvider>>,
    event_provider: Arc<Mutex<EventProvider>>,
    player_provider: Arc<Mutex<PlayerProvider>>
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
            event_provider: Arc::new(Mutex::new(EventProvider::new(channel.clone(), service_name))),
            player_provider: Arc::new(Mutex::new(PlayerProvider::new(channel.clone()))),
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

    pub fn player_provider(&self) -> Arc<Mutex<PlayerProvider>> {
        self.player_provider.clone()
    }

    pub fn available(&self) -> bool {
        self.grpc_client.channel.is_some()
    }

    pub fn self_service_name(&self) -> String {
        env::var("service-name").unwrap_or_else(|_| "default-service".to_string())
    }
}

use crate::providers::event_provider::EventProvider;
use crate::providers::group_provider::GroupProvider;
use crate::providers::service_provider::ServiceProvider;
use crate::structs::{Service};
// Singleton pattern implementation
use std::sync::{Arc, Mutex, OnceLock};
use tonic::transport::{Channel, Endpoint};
use crate::providers::player_provider::PlayerProvider;

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