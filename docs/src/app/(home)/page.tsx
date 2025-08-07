import { HeroSection } from './components/hero-section';
import { AboutSection } from './components/about-section';
import { CompatibilitySection } from './components/compatibility-section';
import { ComingSoonSection } from './components/coming-soon-section';

export default function HomePage() {
  return (
    <main className="flex flex-1 flex-col">
      <HeroSection />
      <section id="about">
      <AboutSection />
      </section>
      <section id="platforms">
      <CompatibilitySection />
      </section>
      <section id="addons">
        <ComingSoonSection />
      </section>
      <section id="partners">
      </section>
    </main>
  );
}
