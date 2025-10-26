import Image from 'next/image';
import Link from 'next/link';
import image from '../../../../public/logo.png';

export function LogoWithLink() {
    return (
        <Link 
            href="/" 
            className="flex items-center gap-2 hover:scale-105 transition-transform duration-300 group"
        >
            <Image
                src={image}
                alt="Logo"
                width={30}
                height={30}
                className="w-6 h-6 sm:w-7 sm:h-7 lg:w-8 lg:h-8 group-hover:scale-110 transition-transform duration-300"
            />
            <span className="font-semibold text-foreground group-hover:text-primary transition-colors duration-300 text-sm sm:text-base lg:text-lg">
                PoloCloud
            </span>
        </Link>
    )
}


export function Logo() {
    return (
        <div className="flex items-center gap-2">
            <Image
                src={image}
                alt="Logo"
                width={30}
                height={30}
                className="w-6 h-6 sm:w-7 sm:h-7 lg:w-8 lg:h-8"
            />
            <span className="font-semibold text-foreground text-sm sm:text-base lg:text-lg">
            PoloCloud
            </span>
        </div>
    )
}