import { docs } from '@/.source';
import { loader } from 'fumadocs-core/source';
import { icons } from 'lucide-react';
import { createElement } from 'react';

// See https://fumadocs.vercel.app/docs/headless/source-api for more info
export const source = loader({
  // it assigns a URL to your pages
  baseUrl: '/docs',
  icon(icon) {
      if (icon && icon in icons)
        return createElement(icons[icon as keyof typeof icons]);
  },
  source: docs.toFumadocsSource(),
});
