import { ClusterGroup } from '@/lib/api/entities/ClusterGroup';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

interface ClusterGroupProps {
  name?: string;
}

export const useClusterGroup = (props: ClusterGroupProps) => {
  const { name } = props;
  const { instanceEndpoint } = useInstance();

  const query = useQuery<undefined, Error, ClusterGroup>({
    queryKey: ['clusterGroup', name],
    queryFn: async () => {
      console.log(`/group/${name}`);

      return (await instanceEndpoint.get(`/group/${name}`)).data;
    },
    enabled: !!name,
  });

  return query;
};
