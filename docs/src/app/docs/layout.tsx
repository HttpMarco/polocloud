import { DocsLayout } from 'fumadocs-ui/layouts/notebook';
import type { DocsLayoutProps } from 'fumadocs-ui/layouts/notebook';
import type { ReactNode } from 'react';
import { source } from '@/lib/source';
import { Logo } from '@/components/layout/header/logo';
import { Footer } from '../(home)/components/footer';

const docsOptions: DocsLayoutProps = {
  tree: source.pageTree,
  tabMode: 'sidebar',
  nav: {
    title: <Logo />,
    mode: "top",
    transparentMode: "top",
  },
  sidebar: {
    collapsible: false,
  },
  links: [],
};

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <div className="relative min-h-screen">
      <div className="absolute left-[280px] right-0 inset-y-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)] pointer-events-none" />
      <div className="relative">
        <DocsLayout {...docsOptions}>{children}</DocsLayout>
        <Footer />
      </div>
    </div>
  );
}
