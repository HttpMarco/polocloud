import { DocsLayout } from 'fumadocs-ui/layouts/notebook';
import type { DocsLayoutProps } from 'fumadocs-ui/layouts/notebook';
import type { ReactNode } from 'react';
import { baseOptions } from '@/app/layout.config';
import { source } from '@/lib/source';
import { GithubInfo } from 'fumadocs-ui/components/github-info';

const docsOptions: DocsLayoutProps = {
  ...baseOptions,
  tree: source.pageTree,
  tabMode: 'sidebar',
  links: [
    {
      type: 'custom',
      children: (
        <GithubInfo owner="httpmarco" repo="polocloud" className="" />
      ),
    },
  ],
};
export default function Layout({ children }: { children: ReactNode }) {
  return <DocsLayout {...docsOptions}>{children}</DocsLayout>;
}
