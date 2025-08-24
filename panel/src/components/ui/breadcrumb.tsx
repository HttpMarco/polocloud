import * as React from "react"
import {Slot} from "@radix-ui/react-slot"
import {ChevronRight, MoreHorizontal} from "lucide-react"

import {cn} from "@/lib/utils"

const Breadcrumb = React.forwardRef<
    HTMLElement,
    React.ComponentPropsWithoutRef<"nav"> & {
    separator?: React.ReactNode
}
>(({...props}, ref) => <nav ref={ref} aria-label="breadcrumb" {...props} />)
Breadcrumb.displayName = "Breadcrumb"

const BreadcrumbList = React.forwardRef<
    HTMLOListElement,
    React.ComponentPropsWithoutRef<"ol">
>(({className, ...props}, ref) => (
    <ol
        ref={ref}
        className={cn(
            "flex flex-wrap items-center gap-2 break-words text-sm sm:gap-3",
            className
        )}
        {...props}
    />
))
BreadcrumbList.displayName = "BreadcrumbList"

const BreadcrumbItem = React.forwardRef<
    HTMLLIElement,
    React.ComponentPropsWithoutRef<"li">
>(({className, ...props}, ref) => (
    <li
        ref={ref}
        className={cn("inline-flex items-center gap-2", className)}
        {...props}
    />
))
BreadcrumbItem.displayName = "BreadcrumbItem"

const BreadcrumbLink = React.forwardRef<
    HTMLAnchorElement,
    React.ComponentPropsWithoutRef<"a"> & {
    asChild?: boolean
}
>(({asChild, className, ...props}, ref) => {
    const Comp = asChild ? Slot : "a"

    return (
        <Comp
            ref={ref}
            className={cn(
                "flex items-center gap-2 px-3 py-1.5 rounded-lg text-muted-foreground transition-all duration-200 hover:text-foreground hover:bg-accent/50 hover:scale-105 font-medium",
                className
            )}
            {...props}
        />
    )
})
BreadcrumbLink.displayName = "BreadcrumbLink"

const BreadcrumbPage = React.forwardRef<
    HTMLSpanElement,
    React.ComponentPropsWithoutRef<"span">
>(({className, ...props}, ref) => (
    <span
        ref={ref}
        role="link"
        aria-disabled="true"
        aria-current="page"
        className={cn(
            "flex items-center gap-2 px-3 py-1.5 rounded-lg bg-accent/20 text-foreground font-semibold border border-accent/30",
            className
        )}
        {...props}
    />
))
BreadcrumbPage.displayName = "BreadcrumbPage"

const BreadcrumbSeparator = ({
                                 children,
                                 className,
                                 ...props
                             }: React.ComponentProps<"li">) => (
    <li
        role="presentation"
        aria-hidden="true"
        className={cn("flex items-center text-muted-foreground/50", className)}
        {...props}
    >
        {children ?? <ChevronRight className="w-4 h-4"/>}
    </li>
)
BreadcrumbSeparator.displayName = "BreadcrumbSeparator"

const BreadcrumbEllipsis = ({
                                className,
                                ...props
                            }: React.ComponentProps<"span">) => (
    <span
        role="presentation"
        aria-hidden="true"
        className={cn("flex h-9 w-9 items-center justify-center", className)}
        {...props}
    >
    <MoreHorizontal className="h-4 w-4"/>
    <span className="sr-only">More</span>
  </span>
)
BreadcrumbEllipsis.displayName = "BreadcrumbElipssis"

export {
    Breadcrumb,
    BreadcrumbList,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbPage,
    BreadcrumbSeparator,
    BreadcrumbEllipsis,
}
