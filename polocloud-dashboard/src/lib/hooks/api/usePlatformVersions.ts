import { useInstance } from '@/lib/providers/InstanceProvider';
import { useQuery } from '@tanstack/react-query';

interface PlatformVersionsProps {
  id?: string;
}

interface PlatformVersions {
  id: string;
  versions: string[];
}

const usePlatformVersions = ({ id }: PlatformVersionsProps) => {
  const { instanceEndpoint } = useInstance();
  const query = useQuery<PlatformVersions>({
    queryKey: [id, 'platformVersions'],
    queryFn: async () => {
      return (await instanceEndpoint.get(`/platform/${id}`)).data;
    },
    enabled: !!id,
  });

  return query;
};
export default usePlatformVersions;
