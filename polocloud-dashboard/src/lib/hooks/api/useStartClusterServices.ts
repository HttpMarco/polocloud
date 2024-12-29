import { ClusterGroup } from '@/lib/api/entities/ClusterGroup';
import { useInstance } from '@/lib/providers/InstanceProvider';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';

type StartClusterServicesInput =
  | { group: ClusterGroup; count: number }
  | { groupName: string; count: number };

const useStartClusterServices = () => {
  const queryClient = useQueryClient();
  const { instanceEndpoint } = useInstance();

  const mutation = useMutation<void, AxiosError, StartClusterServicesInput>({
    mutationFn: async (input) => {
      const groupName = 'group' in input ? input.group.name : input.groupName;

      await instanceEndpoint.post(`/service/start`, {
        group: groupName,
        amount: input.count,
      });
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

export default useStartClusterServices;
