import { ClusterService } from '@/lib/api/entities/ClusterService';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

interface ClusterGroupServices {
  name?: string;
}

export const useClusterGroupServices = (props: ClusterGroupServices) => {
  const { name } = props;
  const { instanceEndpoint } = useInstance();

  const query = useQuery<undefined, Error, ClusterService[]>({
    queryKey: ['clusterGroup', name, 'services'],
    queryFn: async () => {
      return (await instanceEndpoint.get(`/group/${name}/services`)).data;
    },
    refetchInterval: 15000,
    enabled: !!name,
  });

  return query;
};
