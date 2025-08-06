import { HeroSection } from './components/hero-section';
import { AboutSection } from './components/about-section';
import { CompatibilitySection } from './components/compatibility-section';
import { ComingSoonSection } from './components/coming-soon-section';
import { Footer } from './components/footer';

export default function HomePage() {
    return (
        <main className="flex flex-1 flex-col">
            <HeroSection />
            <AboutSection />
            <CompatibilitySection />
            <ComingSoonSection />
            <Footer />
        </main>
    );
}
