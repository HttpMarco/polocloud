import { Footer } from '../../app/(home)/components/footer';

interface PageLayoutProps {
    children: React.ReactNode;
}

export function PageLayout({ children }: PageLayoutProps) {
    return (
        <main className="flex flex-1 flex-col">
            {children}
            <Footer />
        </main>
    );
}