'use client'

import Image from "next/image";
import { SidebarHeader } from "@/components/ui/sidebar";

export function SidebarHeaderComponent() {
  return (
    <SidebarHeader className="border-b border-sidebar-border p-4">
      <a href="/" className="flex items-center space-x-3 hover:opacity-80 transition-opacity cursor-pointer">
        <div className="w-8 h-8 flex items-center justify-center">
          <Image src="/logo.png" alt="Polocloud Logo" width={32} height={32} className="w-8 h-8" />
        </div>
        <div>
          <h2 className="text-sm font-semibold text-sidebar-foreground">Polocloud</h2>
          <p className="text-xs text-sidebar-foreground/60">The simplest cloudsystem</p>
        </div>
      </a>
    </SidebarHeader>
  );
}
