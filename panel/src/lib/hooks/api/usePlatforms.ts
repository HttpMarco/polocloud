import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

const usePlatforms = () => {
  const { instanceEndpoint } = useInstance();
  const query = useQuery<{ platforms: string[] }>({
    queryKey: ['platforms'],
    queryFn: async () => {
      return (await instanceEndpoint.get('/platforms')).data;
    },
  });

  return query;
};
export default usePlatforms;
