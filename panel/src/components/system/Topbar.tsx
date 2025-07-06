import icon from '@/assets/img.png';
import UserDropdown from '@/components/system/dropdown/UserDropdown';
import { cn } from '@/lib/utils';
import { motion } from 'framer-motion';
import { Boxes, Cloudy, Gauge, LucideIcon, Server, Users } from 'lucide-react';
import { Link, useRoute } from 'wouter';

export const Topbar = () => {
  return (
    <div className="fixed top-0 h-12 w-full bg-primary-foreground flex flex-row items-center justify-between px-2 border-b">
      <div className="flex flex-row items-center space-x-2">
        <div className="flex flex-row items-center pr-2">
          <img src={icon} alt="PoloCloud" className="h-8 w-8" />
          <p className="font-semibold">PoloCloud</p>
        </div>
        <NavigationItem label="Dashboard" icon={Gauge} href="/" activeHref="/" />
        <NavigationItem
          label="Groups"
          icon={Boxes}
          href="/groups"
          activeHref="/groups/*?"
        />
        <NavigationItem
          label="Players"
          icon={Users}
          href="/players"
          activeHref="/players/*?"
        />
        <NavigationItem
          label="Services"
          icon={Server}
          href="/services"
          activeHref="/services/*?"
        />
        <NavigationItem
          label="Nodes"
          icon={Cloudy}
          href="/nodes"
          activeHref="/nodes/*?"
        />
      </div>

      <UserDropdown />
    </div>
  );
};

interface NavigationItemProps {
  label: string;
  icon: LucideIcon;
  href: string;
  activeHref: string;
}

const NavigationItem = ({ label, icon, href, activeHref }: NavigationItemProps) => {
  const [active] = useRoute(activeHref);

  const LucideIcon = icon;

  return (
    <Link href={href} className="relative">
      <div
        className={cn(
          'flex flex-row items-center space-x-1 rounded-lg p-2 transition-all hover:bg-foreground/10',
          active && 'rounded-b-none'
        )}
      >
        <LucideIcon className="size-4" />
        <p className={cn('text-sm transition-all px-0.5')}>{label}</p>
      </div>

      {active && (
        <motion.div
          transition={{
            duration: 0.25,
            type: 'spring',
            ease: 'easeInOut',
          }}
          layoutId="activeSettingsTab"
          className="absolute w-full bottom-0 bg-white h-px"
        />
      )}
    </Link>
  );
};
