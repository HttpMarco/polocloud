import { ClusterService } from '@/lib/api/entities/ClusterService';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

const useClusterServices = () => {
  const { instanceEndpoint } = useInstance();
  const query = useQuery<ClusterService[]>({
    queryKey: ['clusterServices'],
    queryFn: async () => {
      return (await instanceEndpoint.get('/services')).data;
    },
    refetchInterval: 1000 * 10,
  });

  return query;
};
export default useClusterServices;
