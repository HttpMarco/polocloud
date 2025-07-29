use serde::{Deserialize, Deserializer};
use crate::polocloud::{GroupType, ServiceState};

pub fn deserialize_group_type_case_insensitive<'de, D>(deserializer: D) -> Result<GroupType, D::Error>
where
    D: Deserializer<'de>,
{
    let s = String::deserialize(deserializer)?;
    match s.to_lowercase().as_str() {
        "service" => Ok(GroupType::Service),
        "server" => Ok(GroupType::Server),
        "proxy" => Ok(GroupType::Proxy),
        _ => Err(serde::de::Error::unknown_variant(&s, &["service", "server", "proxy"])),
    }
}


pub fn deserialize_service_state_case_insensitive<'de, D>(deserializer: D) -> Result<Option<ServiceState>, D::Error>
where
    D: Deserializer<'de>,
{
    let opt = Option::<String>::deserialize(deserializer)?;
    match opt {
        Some(s) => match s.to_lowercase().as_str() {
            "online" => Ok(Some(ServiceState::Online)),
            "preparing" => Ok(Some(ServiceState::Preparing)),
            "starting" => Ok(Some(ServiceState::Starting)),
            "stopping" => Ok(Some(ServiceState::Stopping)),
            "stopped" => Ok(Some(ServiceState::Stopped)),
            _ => Err(serde::de::Error::unknown_variant(&s, &["online", "preparing", "starting", "stopping", "stopped"])),
        },
        None => Ok(None),
    }
}
