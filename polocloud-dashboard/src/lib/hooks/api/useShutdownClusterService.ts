import { ClusterService } from '@/lib/api/entities/ClusterService';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';

interface ShutdownClusterServiceProps {
  service: ClusterService;
}

interface ShutdownClusterServiceProps0 {
  serviceName: string;
}

const useShutdownClusterService = () => {
  const { instanceEndpoint } = useInstance();
  const queryClient = useQueryClient();

  const mutation = useMutation<
    void,
    AxiosError,
    ShutdownClusterServiceProps | ShutdownClusterServiceProps0
  >({
    mutationFn: async (input) => {
      let serviceName: string;

      if ('service' in input && input.service) {
        serviceName = input.service.name;
      } else if ('serviceName' in input && input.serviceName) {
        serviceName = input.serviceName;
      } else {
        throw new Error('Invalid input: service or serviceName must be provided.');
      }

      await instanceEndpoint.post(`/service/${serviceName}/stop`);
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({
        queryKey: ['clusterServices'],
        predicate: (query) => {
          if (
            query.queryKey[0] === 'clusterGroup' &&
            query.queryKey[2] === 'services'
          ) {
            return true;
          }
          return false;
        },
      });
    },
  });

  return mutation;
};

export default useShutdownClusterService;
