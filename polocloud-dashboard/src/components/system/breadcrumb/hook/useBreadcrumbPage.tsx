import useBreadcrumbStore, {
  BreadcrumbItem,
} from '@/components/system/breadcrumb/hook/useBreadcrumbStore';
import { useEffect } from 'react';

interface BreadcrumbPageProps {
  items?: BreadcrumbItem[];
  reset?: boolean;
}
export const useBreadcrumbPage = ({ items }: BreadcrumbPageProps) => {
  const { initializePage } = useBreadcrumbStore();
  useEffect(() => {
    initializePage(items || []);
  }, []);
  return {};
};
