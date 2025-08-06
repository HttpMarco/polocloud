import type { ReactNode } from 'react';
import { HomeLayout } from 'fumadocs-ui/layouts/home';
import { baseOptions } from '@/app/layout.config';

export default function ChangelogsLayout({ children }: { children: ReactNode }) {
  return (
    <HomeLayout {...baseOptions}>
      <main className="flex flex-1 flex-col">
        {children}
      </main>
    </HomeLayout>
  );
} 