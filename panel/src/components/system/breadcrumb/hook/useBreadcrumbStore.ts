import { create } from 'zustand';

export interface BreadcrumbItem {
  label: string;
  href: string;
  activeHref: string;
}

const defaultBreadcrumbItems: BreadcrumbItem[] = [
  {
    label: 'PoloCloud',
    href: '/',
    activeHref: '/',
  },
];

interface BreadcrumbStore {
  breadCrumbItems: BreadcrumbItem[];
  initializePage: (items: BreadcrumbItem[]) => void;
  addItems: (item: BreadcrumbItem[]) => void;
  resetBreadcrumb: () => void;
}

const useBreadcrumbStore = create<BreadcrumbStore>()((set) => ({
  breadCrumbItems: [...defaultBreadcrumbItems],
  addItems: (item: BreadcrumbItem[]) => {
    set((state) => ({
      breadCrumbItems: [...state.breadCrumbItems, ...item],
    }));
  },
  resetBreadcrumb: () => {
    set(() => ({
      breadCrumbItems: [...defaultBreadcrumbItems],
    }));
  },
  initializePage: (items: BreadcrumbItem[]) => {
    set(() => ({
      breadCrumbItems: [...defaultBreadcrumbItems, ...items],
    }));
  },
}));

export default useBreadcrumbStore;
