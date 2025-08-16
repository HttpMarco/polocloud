use crate::polocloud::player_controller_client::PlayerControllerClient;
use crate::polocloud::{PlayerFindByNameRequest, PlayerFindByServiceRequest, PlayerFindRequest};
use crate::structs::PolocloudPlayer;
use tonic::transport::Channel;
use tonic::{Request, Status};

pub struct PlayerProvider {
    player_stub: PlayerControllerClient<Channel>,
}
impl PlayerProvider {
    pub fn new(channel: Channel) -> Self {
        Self {
            player_stub: PlayerControllerClient::new(channel),
        }
    }

    pub async fn find_all_async(&mut self) -> Result<Vec<PolocloudPlayer>, Status> {
        let request = Request::new(PlayerFindRequest::default());
        let response = self.player_stub.find_all(request).await?;

        let players = response.into_inner().players
            .into_iter()
            .map(|player_snapshot| PolocloudPlayer::from_snapshot(player_snapshot))
            .collect();

        Ok(players)
    }

    pub async fn find_by_name_async(&mut self, name: String) -> Result<Option<PolocloudPlayer>, Status> {
        let request = Request::new(PlayerFindByNameRequest {name});
        let response = self.player_stub.find_by_name(request).await?;

        let player_option = response.into_inner().players
            .into_iter()
            .map(|player_snapshot| PolocloudPlayer::from_snapshot(player_snapshot)).nth(0);

        Ok(player_option)
    }

    pub async fn find_by_service_async(&mut self, service_name: String) -> Result<Vec<PolocloudPlayer>, Status> {
        let request = Request::new(PlayerFindByServiceRequest {current_service_name: service_name});
        let response = self.player_stub.find_by_service(request).await?;

        let players = response.into_inner().players
            .into_iter()
            .map(|player_snapshot| PolocloudPlayer::from_snapshot(player_snapshot))
            .collect();

        Ok(players)
    }
}

