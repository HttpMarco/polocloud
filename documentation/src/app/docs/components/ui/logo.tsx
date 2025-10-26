import Image from 'next/image';
import image from '../../../../../public/logo.png';

export function Logo() {
    return (
        <>
            <Image
                src={image}
                alt="Logo"
                width={30}
                height={30}
            />
            PoloCloud
        </>
    )
}