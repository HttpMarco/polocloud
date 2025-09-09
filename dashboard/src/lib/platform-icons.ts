

export const platformIcons: Record<string, string> = {

  'velocity': 'velocity.svg',
  'VELOCITY': 'velocity.svg',

  'paper': 'paper.svg',
  'PAPER': 'paper.svg',

  'spigot': 'spigot.svg',
  'SPIGOT': 'spigot.svg',

  'bungeecord': 'bungeecord.png',
  'BUNGEECORD': 'bungeecord.png',

  'waterfall': 'waterfall.svg',
  'WATERFALL': 'waterfall.svg',

  'fabric': 'fabric.png',
  'FABRIC': 'fabric.png',

  'forge': 'forge.png',
  'FORGE': 'forge.png',

  'node': 'node.png',
  'NODE': 'node.png',
  'nodejs': 'node.png',
  'NODEJS': 'node.png',

  'python': 'python.png',
  'PYTHON': 'python.png',

  'java': 'java.png',
  'JAVA': 'java.png',

  'waterdog': 'waterdog.png',
  'WATERDOG': 'waterdog.png',
  
  'pumpkin': 'pumkin.svg',
  'PUMPKIN': 'pumkin.svg',
  
  'limbo': 'limbo.jpg',
  'LIMBO': 'limbo.jpg',
  
  'nukkit': 'nukkit.png',
  'NUKKIT': 'nukkit.png',
  
  'purpur': 'purpur.svg',
  'PURPUR': 'purpur.svg',
  
  'leaf': 'leaf.svg',
  'LEAF': 'leaf.svg',
  
  'gate': 'gate.png',
  'GATE': 'gate.png',
  
  'folia': 'folia.svg',
  'FOLIA': 'folia.svg',
  
  'AdvancedSlimePaper': 'advancedslimepaper.png',
  'advancedslimepaper': 'advancedslimepaper.png',

  'default': 'placeholder.png'
};

export function getPlatformIcon(platformName: string): string {
  const iconFile = platformIcons[platformName] || platformIcons['default'];
  return `/${iconFile}`;
}

