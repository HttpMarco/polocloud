import apiEndpoint from '@/lib/api/apiEndpoint';
import { Instance } from '@/lib/api/entities/instance/Instance';
import { AxiosInstance } from 'axios';
import { createContext, useContext, useEffect, useState } from 'react';

interface InstanceProviderType {
  currentInstance?: Instance;
  availableInstances: Instance[];
  connectInstance: (instance: Instance) => void;
  switchInstance: (instance: Instance) => void;
  logoutInstance: () => void;
  instanceEndpoint: AxiosInstance;
}

const InstanceContext = createContext<InstanceProviderType | undefined>(undefined);

export const InstanceProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [currentInstance, setCurrentInstance] = useState<Instance>();
  const [instanceEndpoint] = useState<AxiosInstance>();

  const [availableInstances, setAvailableInstances] = useState<Instance[]>(() => {
    const key = localStorage.getItem('availableInstances');
    if (key) {
      return JSON.parse(key);
    }
    return [];
  });

  const switchInstance = (instance: Instance) => {
    localStorage.setItem('currentInstance', instance.id);
    setCurrentInstance(instance);

    // const endpoint = axios.create({
    //   baseURL: `http://${instance.hostname}:5173/polocloud/api/v1/`,
    //   timeout: 5000,
    //   withCredentials: true,
    // });
    // setInstanceEndpoint(endpoint);
  };

  const logoutInstance = () => {
    setCurrentInstance(new Instance({}));
  };

  function connectInstance(instance: Instance) {
    setAvailableInstances((prev) => [...prev, instance]);
    switchInstance(instance);
  }

  useEffect(() => {
    localStorage.setItem('availableInstances', JSON.stringify(availableInstances));
  }, [availableInstances]);

  useEffect(() => {
    const instance = availableInstances?.find(
      (i) => i.id === localStorage.getItem('currentInstance')
    );
    if (instance) {
      switchInstance(instance);
    }
  }, []);

  return (
    <InstanceContext.Provider
      value={{
        currentInstance,
        availableInstances,
        switchInstance,
        logoutInstance,
        connectInstance,
        instanceEndpoint: instanceEndpoint || apiEndpoint,
      }}
    >
      {children}
    </InstanceContext.Provider>
  );
};

export const useInstance = () => {
  const context = useContext(InstanceContext);
  if (!context) {
    throw new Error('useInstance must be used within a InstanceProvider');
  }
  return context;
};
