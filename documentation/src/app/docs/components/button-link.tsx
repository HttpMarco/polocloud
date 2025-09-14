'use client';

import { cn } from "fumadocs-ui/utils/cn";
import { ArrowRight } from 'lucide-react';

export default function ButtonLink({
    icon,
    text,
    href,
    className,
    variant = 'default'
}: {
    icon?: React.ReactNode,
    text: string,
    href: string;
    className?: string;
    variant?: 'default' | 'outline' | 'secondary' | 'ghost';
}) {
    const baseClasses = 'inline-flex items-center justify-center font-medium transition-all duration-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/50 disabled:opacity-50 disabled:cursor-not-allowed group';

    const variantClasses = {
        default: 'bg-[rgba(0,120,255,0.9)] hover:bg-[rgba(0,120,255,1)] text-white shadow-[0_0_20px_rgba(0,120,255,0.3)] hover:shadow-[0_0_25px_rgba(0,120,255,0.4)] font-semibold px-4 py-2.5',
        outline: 'border border-border/50 hover:bg-background/50 text-foreground px-4 py-2.5',
        secondary: 'bg-gradient-to-r from-green-500 to-green-600 hover:from-green-600 hover:to-green-700 text-white shadow-lg hover:shadow-xl px-4 py-2.5',
        ghost: 'hover:bg-background/50 text-foreground px-4 py-2.5',
    };

    const sizeClasses = {
        sm: 'text-sm px-3 py-2',
        md: 'text-base px-4 py-2.5',
        lg: 'text-lg px-6 py-3',
    };

    const combinedClasses = cn(
        baseClasses,
        variantClasses[variant],
        sizeClasses.md,
        'gap-2 [&_svg]:size-4 [&_svg]:text-current',
        className
    );

    return (
        <a 
            href={href} 
            target="_blank" 
            rel="noopener noreferrer"
            className="inline-block"
        >
            <button className={combinedClasses}>
                {icon && <span className="group-hover:scale-110 transition-transform duration-200">{icon}</span>}
                <span>{text}</span>
                <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform duration-200" />
            </button>
        </a>
    );
}