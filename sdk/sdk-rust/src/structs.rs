use std::collections::HashMap;
use serde::Deserialize;
use tonic::transport::Channel;
use crate::polocloud::{GroupSnapshot, GroupType, ServiceState};

#[derive(Debug, Clone, Deserialize)]
pub struct Service {
    #[serde(rename(deserialize = "groupName"))]
    pub group_name: String,
    pub id: u32,
    pub hostname: String,
    pub port: u32,
    #[serde(default, deserialize_with = "crate::proto_deserializers::deserialize_service_state_case_insensitive")]
    pub state: Option<ServiceState>,
    #[serde(rename(deserialize = "type"))]
    #[serde(deserialize_with = "crate::proto_deserializers::deserialize_group_type_case_insensitive")]
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
