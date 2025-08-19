import { FooterBrand, FooterLinks, FooterTechStack, FooterBottom } from '@/components/home/footer';

export function Footer() {
  return (
    <footer className="bg-card/30 backdrop-blur-sm border-t border-border/50 dark:bg-black/40 dark:border-white/10 mt-16 sm:mt-20 lg:mt-32">
      <div className="container mx-auto px-4 sm:px-6 py-8 sm:py-12">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-8 sm:gap-8 lg:gap-12">
                    <FooterBrand />
                    <FooterLinks />
                    <FooterTechStack />
            </div>
                <FooterBottom />
      </div>
    </footer>
  );
} 