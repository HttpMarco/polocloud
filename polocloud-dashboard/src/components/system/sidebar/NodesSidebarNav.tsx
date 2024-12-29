'use client';

import { ChevronRight, Cloudy } from 'lucide-react';

import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
} from '@/components/ui/sidebar';
import { Skeleton } from '@/components/ui/skeleton';
import useNodeEndpoints from '@/lib/hooks/api/useNodeEndpoints';
import { Link } from 'wouter';
import { generateGradientStyle } from '@/lib/color/colorHelper';

export function NodesSidebarNav() {
  const { data: nodes, isLoading } = useNodeEndpoints();

  return (
    <SidebarMenu>
      <Collapsible defaultOpen asChild className="group/collapsible">
        <SidebarMenuItem>
          <CollapsibleTrigger asChild>
            <Link href={'/nodes'}>
              <SidebarMenuButton tooltip={'Nodes'}>
                <Cloudy />
                <span>Nodes</span>
                <ChevronRight className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90" />
              </SidebarMenuButton>
            </Link>
          </CollapsibleTrigger>
          <CollapsibleContent>
            <SidebarMenuSub>
              {!nodes || isLoading ? (
                Array.from({ length: 4 }).map((_, i) => (
                  <SidebarMenuSubItem key={i}>
                    <SidebarMenuSubButton asChild>
                      <Skeleton className="w-full h-5 rounded-md" />
                    </SidebarMenuSubButton>
                  </SidebarMenuSubItem>
                ))
              ) : (
                <>
                  <SidebarMenuSubItem>
                    <SidebarMenuSubButton asChild>
                      <Link href={'/nodes/' + nodes.headNode.data?.name}>
                        <div
                          className="size-3 rounded-sm"
                          style={generateGradientStyle(
                            nodes.headNode.data?.name || 'head-node'
                          )}
                        />
                        <span>{nodes.headNode.data?.name}</span>
                      </Link>
                    </SidebarMenuSubButton>
                  </SidebarMenuSubItem>
                  <SidebarMenuSubItem>
                    <SidebarMenuSubButton asChild>
                      <Link href={'/nodes/' + nodes.localNode.data?.name}>
                        <div
                          className="size-3 rounded-sm"
                          style={generateGradientStyle(
                            nodes.headNode.data?.name || 'local-node'
                          )}
                        />
                        <span>{nodes.localNode.data?.name}</span>
                      </Link>
                    </SidebarMenuSubButton>
                  </SidebarMenuSubItem>

                  {nodes.endpoints?.map((node) => (
                    <SidebarMenuSubItem key={node.data?.name}>
                      <SidebarMenuSubButton asChild>
                        <Link href={'/nodes/' + node.data?.name}>
                          <div className="size-3 rounded-full bg-transparent" />
                          <span>{node.data?.name}</span>
                        </Link>
                      </SidebarMenuSubButton>
                    </SidebarMenuSubItem>
                  ))}
                </>
              )}
            </SidebarMenuSub>
          </CollapsibleContent>
        </SidebarMenuItem>
      </Collapsible>
    </SidebarMenu>
  );
}
