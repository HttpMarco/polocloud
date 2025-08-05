import { HeroSection } from './components/hero-section';
import { CompatibilitySection } from './components/compatibility-section';
import { Footer } from './components/footer';

export default function HomePage() {
    return (
        <main className="flex flex-1 flex-col">
            <HeroSection />
            <CompatibilitySection />
            <Footer />
        </main>
    );
}
