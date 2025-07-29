use crate::{Event, Service};

#[derive(serde::Deserialize, Debug)]
pub struct ServiceOnlineEvent {
    pub service: Service
}

impl Event for ServiceOnlineEvent {
    fn event_name() -> String {
        "ServiceOnlineEvent".to_string()
    }
}

#[derive(serde::Deserialize, Debug)]
pub struct ServiceShutdownEvent {
    pub service: Service
}

impl Event for ServiceShutdownEvent {
    fn event_name() -> String {
        "ServiceShutdownEvent".to_string()
    }
}
