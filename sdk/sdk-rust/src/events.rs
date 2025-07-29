use crate::{Event, Service};

#[derive(serde::Deserialize, Debug)]
pub struct ServiceOnlineEvent {
    pub service: Service
}

impl Event for ServiceOnlineEvent {
    fn event_name() -> &'static str {
        "ServiceOnlineEvent"
    }
}