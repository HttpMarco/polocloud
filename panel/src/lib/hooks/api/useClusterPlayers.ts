import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

const useClusterPlayers = () => {
  const { instanceEndpoint } = useInstance();
  const query = useQuery<{ count: number; players: string[] }>({
    queryKey: ['clusterPlayers'],
    queryFn: async () => {
      return (await instanceEndpoint.get('/players')).data;
    },
    refetchInterval: 1000 * 10,
  });

  return query;
};
export default useClusterPlayers;
