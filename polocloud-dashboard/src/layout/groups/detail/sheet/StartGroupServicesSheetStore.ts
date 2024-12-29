import { ClusterGroup } from '@/lib/api/entities/ClusterGroup';
import { create } from 'zustand';

interface StartGroupServicesSheetStore {
  open: boolean;
  group?: ClusterGroup;
  setOpen: (open: boolean) => void;
  openSheet: (group: ClusterGroup) => void;
}

const useStartGroupServicesSheetStore = create<StartGroupServicesSheetStore>()(
  (set) => ({
    open: false,
    setOpen: (open: boolean) => set({ open }),
    openSheet: (group: ClusterGroup) => set({ open: true, group }),
  })
);

export default useStartGroupServicesSheetStore;
