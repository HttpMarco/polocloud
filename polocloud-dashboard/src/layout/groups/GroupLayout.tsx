import { StaggerInAnimation } from '@/components/system/animation/StaggerInAnimation';
import { useBreadcrumbPage } from '@/components/system/breadcrumb/hook/useBreadcrumbPage';
import { Button } from '@/components/ui/button';
import {
  ClusterGroupCard,
  ClusterGroupCardSkeleton,
} from '@/layout/groups/card/ClusterGroupCard';
import useClusterGroups from '@/lib/hooks/api/useClusterGroups';
import { motion } from 'framer-motion';
import { Plus } from 'lucide-react';
import { Link } from 'wouter';

export default function GroupLayout() {
  const { data: groups, isLoading } = useClusterGroups();

  useBreadcrumbPage({
    items: [
      {
        label: 'Groups',
        href: '/groups',
        activeHref: '/groups',
      },
    ],
  });

  return (
    <div className="flex flex-1 flex-col p-4">
      {/* <Breadcrumb>
        <BreadcrumbList>
          <BreadcrumbItem>
            <BreadcrumbLink href="/">Dashboard</BreadcrumbLink>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <DropdownMenu>
              <DropdownMenuTrigger className="flex items-center gap-1">
                <BreadcrumbEllipsis className="h-4 w-4" />
                <span className="sr-only">Toggle menu</span>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="start">
                <DropdownMenuItem>Documentation</DropdownMenuItem>
                <DropdownMenuItem>Themes</DropdownMenuItem>
                <DropdownMenuItem>GitHub</DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </BreadcrumbItem>
          <BreadcrumbSeparator />
          <BreadcrumbItem>
            <BreadcrumbPage>Groups</BreadcrumbPage>
          </BreadcrumbItem>
        </BreadcrumbList>
      </Breadcrumb> */}
      <div className="flex flex-row items-center w-full justify-between">
        <h1 className="text-2xl font-semibold">Groups</h1>
        <Link href="/groups/create">
          <Button>
            <Plus className="h-4 w-4" />
            Create Group
          </Button>
        </Link>
      </div>

      {isLoading ? (
        <div className="grid gap-2 grid-cols-2 md:grid-cols-4 py-2">
          {Array(4)
            .fill(0)
            .map((_, i) => (
              <ClusterGroupCardSkeleton key={i} />
            ))}
        </div>
      ) : (
        <motion.div
          initial="hidden"
          animate="visible"
          variants={{
            hidden: { opacity: 0, y: 20 },
            visible: {
              opacity: 1,
              y: 0,
              transition: {
                type: 'spring',
                staggerChildren: 0.1,
                delayChildren: 0.1,
              },
            },
          }}
          className="grid gap-2 grid-cols-2 lg:grid-cols-4 py-2"
        >
          {groups?.map((group) => (
            <StaggerInAnimation key={group.name}>
              <ClusterGroupCard group={group} />
            </StaggerInAnimation>
          ))}
        </motion.div>
      )}
    </div>
  );
}
