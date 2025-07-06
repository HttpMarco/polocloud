import { useInstance } from '@/lib/providers/InstanceProvider';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';

interface CreateClusterGroupProps {
  name: string;
  platform: string;
  version: string;
  maxMemory: number;
  staticService: boolean;
  fallback: boolean;
  minOnlineServices: number;
  maxOnlineServices: number;
}

const useCreateClusterGroup = () => {
  const queryClient = useQueryClient();

  const { instanceEndpoint } = useInstance();
  const mutation = useMutation<void, AxiosError, CreateClusterGroupProps>({
    mutationFn: async (data) => {
      await instanceEndpoint.post('/group', data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clusterGroups'] });
    },
  });

  return mutation;
};

export default useCreateClusterGroup;
