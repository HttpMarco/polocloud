'use client';

import { buttonVariants } from "fumadocs-ui/components/ui/button";
import { cn } from "fumadocs-ui/utils/cn";

export default function ButtonLink({
                                       icon,
                                       text,
                                       href,
                                       className
                                   }: {
    icon?: React.ReactNode,
    text: string,
    href: string;
    className?: string;
}) {
    return (
        <a href={href} target="_blank" rel="noopener noreferrer">
            <button
                className={cn(
                    buttonVariants({
                        color: 'secondary',
                        size: 'sm',
                        className: cn('gap-2 [&_svg]:size-3.5 [&_svg]:text-fd-muted-foreground hover:cursor-pointer', className),
                    }),
                )}
            >
                {icon} {text}
            </button>
        </a>
    );
}