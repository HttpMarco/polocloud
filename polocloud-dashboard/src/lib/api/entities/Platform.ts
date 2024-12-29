import bungeeCordLogo from '@/assets/bungee.png';
import fabricLogo from '@/assets/fabric.png';
import folioLogo from '@/assets/folia.png';
import nukkitLogo from '@/assets/nukkit.png';
import paperLogo from '@/assets/paper.png';
import purpurLogo from '@/assets/purpur.png';
import spigotLogo from '@/assets/spigot.png';
import spongeLogo from '@/assets/sponge.png';
import velocityLogo from '@/assets/velocity.png';
import waterdogpeLogo from '@/assets/waterdogpe.png';

export class Platform {
  platform: string = '';
  version: string = '';
  type: PlatformType = PlatformType.PROXY;

  constructor(data: Partial<Platform>) {
    Object.assign(this, data);
  }
}

export enum PlatformType {
  PROXY = 'PROXY',
  SERVER = 'SERVER',
  SERVICE = 'SERVICE',
}

const SPECIFIC_PLATFORM_ICON_MAP: Record<string, string> = {
  paper: paperLogo,
  velocity: velocityLogo,
  folia: folioLogo,
  purpur: purpurLogo,
  spigot: spigotLogo,
  bungeecord: bungeeCordLogo,
  waterdogpe: waterdogpeLogo,
  nukkit: nukkitLogo,
  fabric: fabricLogo,
  sponge: spongeLogo,
};

export const getPlatformIcon = (platform?: string) => {
  if (!platform) {
    return undefined;
  }
  const icon = SPECIFIC_PLATFORM_ICON_MAP[platform];
  if (icon) {
    return icon;
  }
  return undefined;
};
