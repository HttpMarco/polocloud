import type { BaseLayoutProps } from 'fumadocs-ui/layouts/shared';
import Image from 'next/image';

import Logo from '../../public/logo.png';
/**
 * Shared layout configurations
 *
 * you can customise layouts individually from:
 * Home Layout: app/(home)/layout.tsx
 * Docs Layout: app/docs/layout.tsx
 */
export const baseOptions: BaseLayoutProps = {
  nav: {
    title: (
      <>
        <Image
          src={Logo}
          alt="Logo"
          width={50}
          height={50}
          />
          PoloCloud
        </>
    ),
  },
  // see https://fumadocs.dev/docs/ui/navigation/links
  links: [],
};
