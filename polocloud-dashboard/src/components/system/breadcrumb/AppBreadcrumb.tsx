import useBreadcrumbStore, {
  BreadcrumbItem as BreadcrumbAppItem,
} from '@/components/system/breadcrumb/hook/useBreadcrumbStore';
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from '@/components/ui/breadcrumb';
import { Fragment } from 'react/jsx-runtime';
import { Link, useRoute } from 'wouter';
import { AnimatePresence, motion } from 'framer-motion';

export const AppBreadcrumb = () => {
  const { breadCrumbItems } = useBreadcrumbStore();

  return (
    <Breadcrumb>
      <BreadcrumbList>
        <AnimatePresence>
          {breadCrumbItems.map((item, index) => (
            <Fragment key={item.href}>
              <motion.div
                initial={{ opacity: 0, y: 5 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -5 }}
                transition={{ duration: 0.15 }}
              >
                <BreadcrumbItemComponent item={item} />
              </motion.div>
              {index < breadCrumbItems.length - 1 && (
                <motion.div
                  initial={{ opacity: 0, y: 5 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -5 }}
                  transition={{ duration: 0.15 }}
                >
                  <BreadcrumbSeparator />
                </motion.div>
              )}
            </Fragment>
          ))}
        </AnimatePresence>
      </BreadcrumbList>
    </Breadcrumb>
  );
};

interface BreadcrumbItemProps {
  item: BreadcrumbAppItem;
}

const BreadcrumbItemComponent: React.FC<BreadcrumbItemProps> = ({ item }) => {
  const [match] = useRoute(item.activeHref);

  return (
    <BreadcrumbItem className="block">
      {match ? (
        <BreadcrumbPage>{item.label}</BreadcrumbPage>
      ) : (
        <Link href={item.href}>
          <BreadcrumbLink>{item.label}</BreadcrumbLink>
        </Link>
      )}
    </BreadcrumbItem>
  );
};

export default AppBreadcrumb;
