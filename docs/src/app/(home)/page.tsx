import { Suspense } from 'react';
import { HeroSection } from './components/hero-section';
import { AboutSection } from './components/about-section';
import { AddonsSection } from './components/addons-section';
import { CompatibilitySection } from './components/compatibility-section';
import { ContributorsSection } from './components/contributors-section';
import { PartnersSection } from './components/partners-section';
import { FAQSection } from './components/faq-section';
import CommunityFeedbackSection from '@/components/sections/feedback-section';

export default function HomePage() {
  return (
    <main className="min-h-screen scroll-optimized">
      <HeroSection />
      <AboutSection />
      <AddonsSection />
      <CompatibilitySection />
      <ContributorsSection />
      <PartnersSection />
      <Suspense fallback={<div>Loading...</div>}>
        <CommunityFeedbackSection />
      </Suspense>
      <FAQSection />
    </main>
  );
}
