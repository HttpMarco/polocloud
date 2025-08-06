import { HeroSection } from './components/hero-section';
import { AboutSection } from './components/about-section';
import { CompatibilitySection } from './components/compatibility-section';
import { PartnersSection } from './components/partners-section';
import { AddonsSection } from './components/addons-section';
import { Footer } from './components/footer';

export default function HomePage() {
  return (
    <main className="flex flex-1 flex-col">
      <HeroSection />
      <AboutSection />
      <CompatibilitySection />
      <AddonsSection />
      <PartnersSection />
      <Footer />
    </main>
  );
}
