import { useBreadcrumbPage } from '@/components/system/breadcrumb/hook/useBreadcrumbPage';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { MemoryUsageChart } from '@/layout/dashboard/MemoryUsageChart';
import { PlayerCountChart } from '@/layout/dashboard/PlayerCountChart';
import { Activity, Boxes, Cloudy, LucideIcon, Server } from 'lucide-react';

export default function DashboardRoute() {
  useBreadcrumbPage({});

  return (
    <div className="flex flex-1 flex-col p-4 gap-4">
      <h1 className="text-2xl font-semibold">Dashboard</h1>

      <div className="grid gap-4 lg:gap-8 grid-cols-3 lg:grid-cols-4">
        <DashboardCard
          title="Status"
          icon={Activity}
          value="Healthy"
          subValue="No major incidents in the last 24 hours"
        />
        <DashboardCard
          title="Groups"
          icon={Boxes}
          value="23"
          subValue="+1 in the last 5 minutes"
        />
        <DashboardCard
          title="Services"
          icon={Server}
          value="15"
          subValue="+2 from last month"
        />
        <DashboardCard
          title="Nodes"
          icon={Cloudy}
          value="2/2"
          subValue="All online"
        />
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div className="col-span-3">
          <PlayerCountChart />
        </div>
        <div className="col-span-3">
          <MemoryUsageChart />
        </div>
      </div>
    </div>
  );
}

interface DashboardCardProps {
  title: string;
  icon: LucideIcon;
  value: string;
  subValue: string;
}

const DashboardCard = ({ title, icon, value, subValue }: DashboardCardProps) => {
  const LucideIcon = icon;
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-0">
        <CardTitle className="text-sm font-medium">{title}</CardTitle>
        <LucideIcon className="size-4 text-muted-foreground" />
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">{value}</div>
        <p className="text-xs text-muted-foreground">{subValue}</p>
      </CardContent>
    </Card>
  );
};
