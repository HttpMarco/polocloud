
export function getProtocol(): string {
  if (typeof window !== 'undefined') {
    return window.location.protocol
  }
  return 'http:'
}
export function buildBackendUrl(ip: string, path: string): string {
  if (ip.includes('localhost') || ip.includes('127.0.0.1') ||
      ip.startsWith('192.168.') || ip.startsWith('10.') || ip.startsWith('172.')) {
    return `http://${ip}${path}`
  }
  
  if (ip.includes('.') && !ip.includes(':')) {
    return `https://${ip}${path}`
  }
  
  if (ip.includes('.') && ip.includes(':')) {
    return `http://${ip}${path}`
  }
  
  const protocol = getProtocol()
  return `${protocol}//${ip}${path}`
}
