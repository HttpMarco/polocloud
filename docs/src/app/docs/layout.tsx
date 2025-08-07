import { DocsLayout } from 'fumadocs-ui/layouts/notebook';
import type { DocsLayoutProps } from 'fumadocs-ui/layouts/notebook';
import type { ReactNode } from 'react';
import { source } from '@/lib/source';
import { GithubInfo } from 'fumadocs-ui/components/github-info';
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
  links: [
    {
      type: 'custom',
      children: (
        <GithubInfo 
          owner="httpmarco" 
          repo="polocloud" 
          className="" 
        />
      ),
    },
  ],
};

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <>
      <DocsLayout {...docsOptions}>{children}</DocsLayout>
      <Footer />
    </>
  );
}
