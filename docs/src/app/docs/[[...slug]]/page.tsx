import { source } from '@/lib/source';
import {
  DocsPage,
  DocsBody,
  DocsDescription,
  DocsTitle,
} from 'fumadocs-ui/page';
import { notFound } from 'next/navigation';
import { createRelativeLink } from 'fumadocs-ui/mdx';
import { getMDXComponents } from '@/mdx-components';
import { getGithubLastEdit } from 'fumadocs-core/server';

const development = process.env.NODE_ENV === 'development';

export default async function Page(props: {
  params: Promise<{ slug?: string[] }>;
}) {
  const params = await props.params;
  const page = source.getPage(params.slug);
  if (!page) notFound();

  const MDXContent = page.data.body;

  const lastModified = !development 
    ? await getGithubLastEdit({
        owner: 'httpMarco',
        repo: 'polocloud',
        sha: 'docs',
        path: `docs/content/docs/${page.path}`,
      })
    : undefined;

  const editOnGithub = {
    owner: 'httpMarco',
    repo: 'polocloud',
    path: `docs/content/docs/${page.path}`,
    sha: 'docs',
  }
  
  return (
    <DocsPage toc={page.data.toc} editOnGithub={editOnGithub} full={page.data.full} lastUpdate={lastModified || undefined}>
      <DocsTitle>{page.data.title}</DocsTitle>
      <DocsDescription>{page.data.description}</DocsDescription>
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
