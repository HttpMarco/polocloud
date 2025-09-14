"use client";

import { Star, User, ChevronDown, ChevronUp } from "lucide-react";
import { cn } from "@/lib/utils";
import { useState } from "react";

interface FeedbackCardProps {
  username: string;
  avatar: string;
  rating: number;
  description: string;
  createdAt: string;
  className?: string;
}

export default function FeedbackCard({
  username,
  avatar,
  rating,
  description,
  createdAt,
  className
}: FeedbackCardProps) {
  const [isExpanded, setIsExpanded] = useState(false);

  const shouldTruncate = description.length > 200;
  const truncatedDescription = shouldTruncate && !isExpanded 
    ? description.substring(0, 200) + "..."
    : description;
  return (
    <div className={cn(
      "relative flex-shrink-0 w-96 p-8 rounded-2xl border border-border/50",
      "bg-gradient-to-br from-background/80 via-background/90 to-background/95",
      "backdrop-blur-sm shadow-lg hover:shadow-xl transition-all duration-300",
      "hover:scale-[1.02] hover:border-primary/30",
      className
    )}>
      <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-primary/5 via-transparent to-primary/5 opacity-0 hover:opacity-100 transition-opacity duration-300" />

      <div className="relative z-10">

        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-3">
            {avatar ? (
              <img
                src={avatar}
                alt={username}
                className="w-10 h-10 rounded-full ring-2 ring-primary/20 object-cover"
              />
            ) : (
              <div className="w-10 h-10 rounded-full bg-gradient-to-br from-primary/20 to-primary/10 flex items-center justify-center">
                <User className="w-5 h-5 text-primary" />
              </div>
            )}
            <div>
              <div className="font-semibold text-foreground">{username}</div>
              <div className="text-xs text-muted-foreground">
                {new Date(createdAt).toLocaleDateString('de-DE', {
                  day: '2-digit',
                  month: '2-digit',
                  year: 'numeric'
                })}
              </div>
            </div>
          </div>

          <div className="flex items-center gap-1 px-3 py-1 rounded-full bg-gradient-to-r from-amber-500/10 to-orange-500/10 border border-amber-500/20">
            {Array.from({ length: 5 }).map((_, i) => (
              <Star
                key={i}
                className={cn(
                  "w-4 h-4 transition-colors",
                  i < rating
                    ? "text-amber-500 fill-amber-500"
                    : "text-amber-500/30"
                )}
              />
            ))}
          </div>
        </div>

        <div className="space-y-3">
          <div className="text-muted-foreground text-base leading-relaxed">
            &ldquo;{truncatedDescription}&rdquo;
          </div>
          
          {shouldTruncate && (
            <button
              onClick={() => setIsExpanded(!isExpanded)}
              className="flex items-center gap-1 text-primary hover:text-primary/80 text-sm font-medium transition-colors duration-200 group"
            >
              {isExpanded ? (
                <>
                  <span>Read Less</span>
                  <ChevronUp className="w-4 h-4 group-hover:-translate-y-0.5 transition-transform duration-200" />
                </>
              ) : (
                <>
                  <span>Read More</span>
                  <ChevronDown className="w-4 h-4 group-hover:translate-y-0.5 transition-transform duration-200" />
                </>
              )}
            </button>
          )}
        </div>

        <div className="mt-6 pt-4 border-t border-border/30">
          <div className="w-12 h-0.5 bg-gradient-to-r from-primary/60 to-primary/20 rounded-full" />
        </div>
      </div>
    </div>
  );
}
