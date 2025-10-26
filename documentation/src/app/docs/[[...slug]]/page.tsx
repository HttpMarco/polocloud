import { source } from '@/lib/source';
import {
  DocsPage,
  DocsBody,
  DocsTitle,
} from 'fumadocs-ui/page';
import { notFound } from 'next/navigation';
import { createRelativeLink } from 'fumadocs-ui/mdx';
import { getMDXComponents } from '@/mdx-components';

export default async function Page(props: {
  params: Promise<{ slug?: string[] }>;
}) {
  const params = await props.params;
  const page = source.getPage(params.slug);
  if (!page) notFound();

  const MDXContent = page.data.body;

  // Disabled to avoid GitHub API rate limits during build
  // To enable: Add GITHUB_TOKEN to Vercel environment variables
  const lastModified = undefined;

  const editOnGithub = {
    owner: 'httpMarco',
    repo: 'polocloud',
    path: `documentation/content/docs/${page.file.path}`,
    sha: 'master',
  }
  
  return (
    <DocsPage toc={page.data.toc} editOnGithub={editOnGithub} full={page.data.full} lastUpdate={lastModified || undefined}>
      <DocsTitle className="text-sm font-medium bg-gradient-to-r from-primary via-primary/70 to-primary bg-clip-text text-transparent tracking-tight text-left mb-4 opacity-70">
        {page.data.title}
      </DocsTitle>
      <DocsBody>
        <MDXContent
          components={getMDXComponents({
            // this allows you to link to other pages with relative file paths
            a: createRelativeLink(source, page),
          })}
        />
      </DocsBody>
    </DocsPage>
  );
}

export async function generateStaticParams() {
  return source.generateParams();
}

export async function generateMetadata(props: {
  params: Promise<{ slug?: string[] }>;
}) {
  const params = await props.params;
  const page = source.getPage(params.slug);
  if (!page) notFound();

  return {
    title: page.data.title,
    description: page.data.description,
  };
}
