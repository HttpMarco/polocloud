import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Progress } from '@/components/ui/progress';
import {
  ClusterService,
  getServiceStatusColor,
} from '@/lib/api/entities/ClusterService';
import useShutdownClusterService from '@/lib/hooks/api/useShutdownClusterService';
import { cn } from '@/lib/utils';
import { MoreVertical } from 'lucide-react';
import { toast } from 'sonner';

interface ClusterServiceCardProps {
  service: ClusterService;
}

export default function ClusterServiceCard({ service }: ClusterServiceCardProps) {
  const { mutateAsync } = useShutdownClusterService();

  function handleShutdown() {
    toast.promise(async () => await mutateAsync({ service }), {
      loading: 'Shutting down...',
      success: 'Shutdown successful.',
      error: 'Shutdown failed.',
    });
  }

  return (
    <div className="flex flex-col p-4 space-y-2 border rounded-lg hover:bg-primary-foreground transition-all cursor-pointer hover:shadow-lg h-32 justify-between">
      <div className="w-full flex flex-row items-center justify-between pb-4">
        <div className="flex flex-row items-center space-x-2">
          <div
            className={cn(
              'size-3 rounded-full animate-pulse',
              `bg-${getServiceStatusColor(service.state)}-500`
            )}
          />
          <div>
            <p className="text-xl font-semibold">{service.name}</p>
            <p className="text-xs text-muted-foreground">
              {service.hostname}:{service.port}
            </p>
          </div>
        </div>

        <div>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button size="icon" variant="outline" className="h-8 w-8">
                <MoreVertical className="h-3.5 w-3.5" />
                <span className="sr-only">More</span>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem>Show</DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleShutdown}>Shutdown</DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
      <p className="text-xs text-muted-foreground truncate">
        Started less than a minute ago.
      </p>
      <Progress
        value={Math.min(
          ((service.onlinePlayers.length || 0) / service.maxPlayers) * 100,
          100
        )}
      />
    </div>
  );
}
