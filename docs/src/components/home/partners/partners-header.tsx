'use client';

interface PartnersHeaderProps {
    isVisible: boolean;
}

export function PartnersHeader({ isVisible }: PartnersHeaderProps) {
    return (
        <div className={`text-center mb-20 transition-all duration-1000 ease-out ${
            isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
        }`}>
            <h2 className={`text-3xl md:text-4xl lg:text-5xl font-black mb-8 bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent transition-all duration-1000 delay-200 ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
                Official Partners
            </h2>
            <p className={`text-base md:text-lg text-muted-foreground max-w-4xl mx-auto leading-relaxed transition-all duration-1000 delay-400 ${
                isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-8'
            }`}>
                We partner with leading cloud providers and server networks to deliver the best Minecraft hosting experience.
            </p>
        </div>
    );
}
