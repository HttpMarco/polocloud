const fnv1aHash = (str: string): number => {
  let hash = 2166136261;
  for (let i = 0; i < str.length; i++) {
    hash ^= str.charCodeAt(i);
    hash = Math.imul(hash, 16777619);
  }
  return hash >>> 0;
};

const hslToHex = (h: number, s: number, l: number): string => {
  h = h % 360;
  s /= 100;
  l /= 100;

  const a = s * Math.min(l, 1 - l);
  const f = (n: number): string => {
    const k = (n + h / 30) % 12;
    const color = l - a * Math.max(Math.min(k - 3, 9 - k, 1), -1);
    return Math.round(255 * color)
      .toString(16)
      .padStart(2, '0');
  };
  return `#${f(0)}${f(8)}${f(4)}`;
};

const generateAnalogousColors = (hash: number): [string, string] => {
  const baseHue = 180 + (hash % 80); // Restrict baseHue to the range 180-260 (cyan and blue)
  const hueOffset = 10; // Small offset for analogous variation

  const hue1 = (baseHue + 360) % 360;
  const hue2 = (baseHue + hueOffset + 360) % 360;

  const saturation = 60; // Cyan/blue tones often benefit from moderate saturation
  const lightness = 50; // Balanced lightness for visibility

  const color1 = hslToHex(hue1, saturation, lightness);
  const color2 = hslToHex(hue2, saturation, lightness);

  return [color1, color2];
};

const calculateBrightness = (hex: string): number => {
  hex = hex.replace('#', '');
  const r = parseInt(hex.substring(0, 2), 16);
  const g = parseInt(hex.substring(2, 4), 16);
  const b = parseInt(hex.substring(4, 6), 16);
  return (r * 299 + g * 587 + b * 114) / 1000;
};

const getTextColor = (
  hex: string,
  darkColor: string = '#000000',
  lightColor: string = '#FFFFFF'
): string => {
  return calculateBrightness(hex) > 128 ? darkColor : lightColor;
};

export const generateGradientStyle = (
  id: string
): { background: string; color: string } => {
  const hash = fnv1aHash(id);
  const [color1, color2] = generateAnalogousColors(hash);
  const textColor = getTextColor(color1);
  return {
    background: `linear-gradient(to right, ${color1}, ${color2})`,
    color: textColor,
  };
};
