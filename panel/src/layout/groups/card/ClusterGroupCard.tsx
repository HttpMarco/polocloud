import { Skeleton } from '@/components/ui/skeleton';
import { ClusterGroup } from '@/lib/api/entities/ClusterGroup';
import { getPlatformIcon } from '@/lib/api/entities/Platform';
import { Boxes } from 'lucide-react';
import { Link } from 'wouter';

export const ClusterGroupCardSkeleton = () => {
  return (
    <div className="flex flex-col p-4 space-y-2 border rounded-lg hover:bg-primary-foreground transition-all cursor-pointer hover:shadow-lg h-32 justify-between">
      <div className="w-full flex flex-row items-center justify-between pb-4">
        <div className="flex flex-row items-center space-x-2">
          <Skeleton className="w-16 h-4 rounded-md" />
        </div>
      </div>
      <Skeleton className="h-2 w-32 rounded-md" />
    </div>
  );
};

interface GroupCardProps {
  group: ClusterGroup;
}

export const ClusterGroupCard = ({ group }: GroupCardProps) => {
  if (!group) return null;
  return (
    <Link
      href={`/groups/${group.name}`}
      className="flex flex-col p-4 space-y-2 border rounded-lg hover:bg-primary-foreground transition-all cursor-pointer hover:shadow-lg h-32 justify-between"
    >
      <div className="w-full flex flex-row items-center justify-between pb-4">
        <div>
          <div className="flex flex-row items-center space-x-2">
            <div className="size-2 rounded-full bg-green-500 animate-pulse "></div>
            <p className="text-xl font-semibold">
              {group.name} {group.staticService && ' (Static)'}
            </p>
          </div>

          <div className="text-[0.6rem] text-muted-foreground pl-4">
            <p className="capitalize">Platform: {group.platform?.platform}</p>
            <p>Version: {group.platform?.version}</p>
          </div>
        </div>

        {getPlatformIcon(group.platform?.platform) ? (
          <img
            className="size-8 object-contain"
            src={getPlatformIcon(group.platform?.platform)}
            alt="Platform Icon"
          />
        ) : (
          <Boxes className="size-6" />
        )}
      </div>
      <p className="text-xs text-muted-foreground truncate">
        Created less than a minute ago
      </p>
    </Link>
  );
};
