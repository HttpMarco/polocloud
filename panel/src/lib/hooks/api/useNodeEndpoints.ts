import { NodeEndpoint } from '@/lib/api/entities/NodeEndpoint';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

const useNodeEndpoints = () => {
  const { instanceEndpoint } = useInstance();
  const query = useQuery<{
    localNode: NodeEndpoint;
    headNode: NodeEndpoint;
    endpoints: NodeEndpoint[];
  }>({
    queryKey: ['nodeEndpoints'],
    queryFn: async () => {
      return (await instanceEndpoint.get('/nodes')).data;
    },
    refetchInterval: 1000 * 10,
  });

  return query;
};
export default useNodeEndpoints;
