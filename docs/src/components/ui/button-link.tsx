import Link from 'next/link';

interface ButtonLinkProps {
  icon: React.ReactNode;
  text: string;
  href: string;
}

export default function ButtonLink({ icon, text, href }: ButtonLinkProps) {
  return (
    <Link
      href={href}
      target="_blank"
      rel="noopener noreferrer"
      className="inline-flex items-center gap-2 px-3 py-1.5 bg-muted/50 hover:bg-muted border border-border/50 hover:border-border text-foreground rounded-md font-medium hover:scale-105 transition-all duration-200 text-sm"
    >
      {icon}
      {text}
    </Link>
  );
} 