import { NextRequest } from 'next/server';

export interface AuthCredentials {
  backendIp: string;
  username: string;
  password: string;
}

export function getCredentialsFromRequest(request: NextRequest): AuthCredentials | null {
  const credentialsCookie = request.cookies.get('polocloud_credentials')?.value;
  
  if (!credentialsCookie) return null;
  
  try {
    const jsonString = decodeURIComponent(Buffer.from(credentialsCookie, 'base64').toString());
    return JSON.parse(jsonString);
  } catch (error) {
    console.error('Failed to decode credentials from request:', error);
    return null;
  }
}

export function getBackendIpFromRequest(request: NextRequest): string | null {
  const credentials = getCredentialsFromRequest(request);
  return credentials?.backendIp || null;
}

export function getUsernameFromRequest(request: NextRequest): string | null {
  const credentials = getCredentialsFromRequest(request);
  return credentials?.username || null;
}


export function hasCredentialsInRequest(request: NextRequest): boolean {
  return getCredentialsFromRequest(request) !== null;
}



