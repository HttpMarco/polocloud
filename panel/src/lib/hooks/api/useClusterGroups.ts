import { ClusterGroup } from '@/lib/api/entities/ClusterGroup';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

const useClusterGroups = () => {
  const { instanceEndpoint } = useInstance();
  const query = useQuery<ClusterGroup[]>({
    queryKey: ['clusterGroups'],
    queryFn: async () => {
      return (await instanceEndpoint.get('/groups')).data;
    },
    refetchInterval: 1000 * 10,
  });

  return query;
};
export default useClusterGroups;
