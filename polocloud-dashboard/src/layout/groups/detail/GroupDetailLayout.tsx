import { MemoryStick, MoreVertical, Power } from 'lucide-react';

import LoadingSpinner from '@/components/system/LoadingSpinner';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Progress } from '@/components/ui/progress';
import { Separator } from '@/components/ui/separator';
import { Skeleton } from '@/components/ui/skeleton';
import ClusterServiceCard from '@/layout/groups/detail/card/ClusterServiceCard';
import { getPlatformIcon } from '@/lib/api/entities/Platform';
import { useClusterGroup } from '@/lib/hooks/api/useClusterGroup';
import { useClusterGroupServices } from '@/lib/hooks/api/useClusterGroupServices';
import { booleanToString, formatBytes } from '@/lib/utils';
import { useParams } from 'wouter';
import { useBreadcrumbPage } from '@/components/system/breadcrumb/hook/useBreadcrumbPage';
import useStartGroupServicesSheetStore from '@/layout/groups/detail/sheet/StartGroupServicesSheetStore';
import { StartGroupServicesSheet } from '@/layout/groups/detail/sheet/StartGroupServicesSheet';

export default function GroupDetailLayout() {
  const { openSheet } = useStartGroupServicesSheetStore();
  const groupName = useParams().name;
  const {
    data: group,
    isLoading,
    dataUpdatedAt,
  } = useClusterGroup({ name: groupName });
  const { data: services, isLoading: isServicesLoading } = useClusterGroupServices({
    name: groupName,
  });

  useBreadcrumbPage({
    items: [
      {
        label: 'Groups',
        href: '/groups',
        activeHref: '/groups',
      },
      {
        label: groupName || 'NA',
        href: `/groups/${groupName}`,
        activeHref: `/groups/${groupName}`,
      },
    ],
  });

  if (!groupName) return null;

  return (
    <>
      <div className="flex flex-1 w-full flex-col p-4">
        {isLoading || !group ? (
          <div className="flex flex-1 justify-center items-center">
            <LoadingSpinner />
          </div>
        ) : (
          <div className="flex flex-col sm:gap-4 sm:py-4">
            <main className="grid flex-1 items-start gap-4 sm:py-0 md:gap-8 lg:grid-cols-3 xl:grid-cols-3">
              <div className="grid auto-rows-max items-start gap-4 md:gap-8 lg:col-span-2">
                <div className="grid gap-4 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-2 xl:grid-cols-4">
                  <Card className="md:col-span-2 flex flex-col justify-between">
                    <CardHeader className="pb-3">
                      <CardTitle>Start new services</CardTitle>
                      <CardDescription className="max-w-lg text-balance leading-relaxed">
                        Start easily new services from this group.
                      </CardDescription>
                    </CardHeader>
                    <CardFooter className="flex items-end justify-end">
                      <Button onClick={() => openSheet(group)}>
                        Start new services
                      </Button>
                    </CardFooter>
                  </Card>
                  <Card className="justify-between flex flex-col">
                    <CardHeader className="pb-2">
                      <CardDescription>Total running services</CardDescription>
                      <CardTitle className="text-4xl">{services?.length}</CardTitle>
                    </CardHeader>
                    <CardFooter>
                      <Progress
                        value={Math.min(
                          ((services?.length || 0) /
                            group.maxOnlineServerInstances) *
                            100,
                          100
                        )}
                      />
                    </CardFooter>
                  </Card>
                  <Card className="justify-between flex flex-col">
                    <CardHeader className="pb-2">
                      <CardDescription>Total players online</CardDescription>
                      <CardTitle className="text-4xl">
                        {services?.reduce((sum, service) => {
                          return sum + service.onlinePlayers.length;
                        }, 0)}
                      </CardTitle>
                    </CardHeader>
                    <CardFooter>
                      <Progress
                        value={Math.min(
                          ((services?.reduce((sum, service) => {
                            return sum + service.onlinePlayers.length;
                          }, 0) || 0) /
                            (services?.reduce((sum, service) => {
                              return sum + service.maxPlayers;
                            }, 0) || 0)) *
                            100,
                          100
                        )}
                      />
                    </CardFooter>
                  </Card>
                </div>
                <div>
                  <h1 className="text-2xl font-semibold pb-2">Services</h1>
                  {isServicesLoading || !services ? (
                    <LoadingSpinner />
                  ) : (
                    <div className="grid grid-cols-2 lg:grid-cols-3 gap-4">
                      {services.map((service) => (
                        <ClusterServiceCard key={service.name} service={service} />
                      ))}
                    </div>
                  )}
                </div>
              </div>
              <div>
                <Card className="overflow-hidden" x-chunk="dashboard-05-chunk-4">
                  <CardHeader className="flex flex-row items-start bg-muted/50">
                    <div className="grid gap-0.5">
                      <CardTitle className="group flex items-center gap-2 text-lg">
                        <img
                          src={getPlatformIcon(group.platform?.platform)}
                          className="size-6 object-contain"
                          alt=""
                        />
                        <p>{groupName}</p>
                      </CardTitle>

                      <CardDescription>
                        {isServicesLoading || !services ? (
                          <Skeleton className="w-32 h-4" />
                        ) : (
                          `${services.length} services running.`
                        )}
                      </CardDescription>
                    </div>
                    <div className="ml-auto flex items-center gap-1">
                      <Button size="sm" variant="outline" className="h-8 gap-1">
                        <Power className="h-3.5 w-3.5" />
                        <span className="lg:sr-only xl:not-sr-only xl:whitespace-nowrap">
                          Shutdown
                        </span>
                      </Button>
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button size="icon" variant="outline" className="h-8 w-8">
                            <MoreVertical className="h-3.5 w-3.5" />
                            <span className="sr-only">More</span>
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuItem>Edit</DropdownMenuItem>
                          <DropdownMenuSeparator />
                          <DropdownMenuItem>Delete</DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </div>
                  </CardHeader>
                  <CardContent className="p-6 text-sm">
                    <div className="grid gap-3">
                      <div className="font-semibold">Group Details</div>
                      <ul className="grid gap-3">
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">
                            Maximal players
                          </span>
                          <span>{group.maxPlayers}</span>
                        </li>
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">
                            Minimal online services
                          </span>
                          <span>{group.minOnlineServerInstances}</span>
                        </li>
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">
                            Maximal online services
                          </span>
                          <span>{group.maxOnlineServerInstances}</span>
                        </li>
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">
                            Static service?
                          </span>
                          <span>{booleanToString(group.staticService)}</span>
                        </li>
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">Is fallback?</span>
                          <span>{booleanToString(group.fallback)}</span>
                        </li>
                      </ul>
                    </div>
                    <Separator className="my-4" />
                    <div className="grid gap-3">
                      <div className="font-semibold">Platform Details</div>
                      <ul className="grid gap-3">
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">Platform</span>
                          <span className="capitalize">
                            {group.platform?.platform}
                          </span>
                        </li>
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">Version</span>
                          <span>{group.platform?.version}</span>
                        </li>
                        <li className="flex items-center justify-between">
                          <span className="text-muted-foreground">
                            Platform Category
                          </span>
                          <span>{group.platform?.type}</span>
                        </li>
                      </ul>
                    </div>
                    <Separator className="my-4" />
                    <div className="grid gap-3">
                      <div className="font-semibold">Resource Information</div>
                      <dl className="grid gap-3">
                        <div className="flex items-center justify-between">
                          <dt className="flex items-center gap-1 text-muted-foreground">
                            <MemoryStick className="h-4 w-4" />
                            Memory
                          </dt>
                          <dd>{formatBytes(group.maxMemory * 1024 * 1024)}</dd>
                        </div>
                      </dl>
                    </div>
                  </CardContent>
                  <CardFooter className="flex flex-row items-center border-t bg-muted/50 px-6 py-4">
                    <div className="text-xs text-muted-foreground">
                      Data last updated at {new Date(dataUpdatedAt).toLocaleString()}
                    </div>
                  </CardFooter>
                </Card>
              </div>
            </main>
          </div>
        )}
      </div>
      <StartGroupServicesSheet />
    </>
  );
}
