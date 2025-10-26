import { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Terminal - PoloCloud',
  description: 'Real-time system logs and command execution',
};

export default function TerminalLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="min-h-screen bg-background">
      {children}
    </div>
  );
}
