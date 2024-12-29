import { useBreadcrumbPage } from '@/components/system/breadcrumb/hook/useBreadcrumbPage';
import { ServiceDetailTerminalLayout } from '@/layout/service/detail/panel/ServiceDetailTerminalLayout';
import { cn } from '@/lib/utils';
import { AnimatePresence, motion } from 'framer-motion';
import { Link, Route, Router, Switch, useParams, useRoute } from 'wouter';

export default function ServiceDetailLayout() {
  const serviceName = useParams().name;

  useBreadcrumbPage({
    items: [
      { activeHref: '/services', href: '/services', label: 'Services' },
      {
        activeHref: `/services/${serviceName}/*?`,
        href: `/services/${serviceName}`,
        label: serviceName || '',
      },
    ],
  });

  if (!serviceName) return null;

  return (
    <Router base={`/services/${serviceName}`}>
      <div className="flex flex-col flex-1 relative">
        <div className="sticky top-16 w-full h-12 px-4 flex flex-row items-center space-x-4 border-b z-10 bg-background">
          <ServiceDetailNav activeHref={`/`} href={`/`} label="Overview" />
          <ServiceDetailNav
            activeHref={`/terminal`}
            href={`/terminal`}
            label="Terminal"
          />
          <ServiceDetailNav
            activeHref={`/templates`}
            href={`/templates`}
            label="Templates"
          />
          <ServiceDetailNav activeHref={`/files`} href={`/files`} label="Files" />
        </div>

        <div className="flex flex-1 p-4">
          <Switch>
            <Route path="/terminal">
              <ServiceDetailTerminalLayout />
            </Route>
          </Switch>
        </div>
      </div>
    </Router>
  );
}

interface ServiceDetailNavProps {
  href: string;
  activeHref: string;
  label: string;
}

const ServiceDetailNav = ({ href, activeHref, label }: ServiceDetailNavProps) => {
  const [match] = useRoute(activeHref);

  return (
    <AnimatePresence>
      <div className="relative px-2 py-0.5 flex items-center justify-center text-center">
        <Link
          href={href}
          className={cn(
            'relative cursor-pointer text-muted-foreground transition-all hover:text-foreground',
            match && 'text-foreground'
          )}
        >
          {label}
        </Link>

        {match && (
          <motion.div
            transition={{
              duration: 0.25,
              type: 'spring',
              ease: 'easeInOut',
            }}
            initial={{ height: '2px' }}
            layoutId="serviceDetailNavActive"
            className="absolute -bottom-2 bg-white w-full"
          />
        )}
      </div>
    </AnimatePresence>
  );
};
