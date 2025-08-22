interface PageLayoutProps {
  children: React.ReactNode;
}

export function PageLayout({ children }: PageLayoutProps) {
  return (
    <main className="flex flex-1 flex-col">
      {children}
    </main>
  );
} 