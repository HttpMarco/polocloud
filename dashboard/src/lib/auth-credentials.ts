'use client';

export interface AuthCredentials {
  backendIp: string;
  username: string;
  password: string;
}

export function setCredentialsCookie(credentials: AuthCredentials): void {
  if (typeof window === 'undefined') return;
  
  try {
    const jsonString = JSON.stringify(credentials);
    const base64String = Buffer.from(jsonString).toString('base64');
    const encodedString = encodeURIComponent(base64String);
    
    document.cookie = `polocloud_credentials=${encodedString}; path=/; max-age=${7 * 24 * 60 * 60}; secure; samesite=lax`;
  } catch (error) {
    console.error('Failed to set credentials cookie:', error);
  }
}

export function getCredentialsCookie(): AuthCredentials | null {
  if (typeof window === 'undefined') return null;
  
  try {
    const cookies = document.cookie.split(';');
    const credentialsCookie = cookies.find(cookie => 
      cookie.trim().startsWith('polocloud_credentials=')
    );
    
    if (!credentialsCookie) return null;
    
    const cookieValue = credentialsCookie.split('=')[1];
    const base64String = decodeURIComponent(cookieValue);
    const jsonString = Buffer.from(base64String, 'base64').toString();
    
    return JSON.parse(jsonString);
  } catch (error) {
    console.error('Failed to get credentials from cookie:', error);
    return null;
  }
}

export function clearCredentialsCookie(): void {
  if (typeof window === 'undefined') return;
  
  document.cookie = 'polocloud_credentials=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT';
}

export function getBackendIpFromCookie(): string | null {
  const credentials = getCredentialsCookie();
  return credentials?.backendIp || null;
}

export function getUsernameFromCookie(): string | null {
  const credentials = getCredentialsCookie();
  return credentials?.username || null;
}

export function getPasswordFromCookie(): string | null {
  const credentials = getCredentialsCookie();
  return credentials?.password || null;
}

export function getAuthToken(): string | null {
  if (typeof window === 'undefined') return null;
  return localStorage.getItem('token');
}