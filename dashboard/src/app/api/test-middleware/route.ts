import { NextRequest, NextResponse } from 'next/server';

export async function GET(request: NextRequest) {
  return NextResponse.json({ 
    message: 'Test-Route funktioniert',
    pathname: request.nextUrl.pathname,
    search: request.nextUrl.search,
    cookies: Object.fromEntries(request.cookies.getAll().map(c => [c.name, c.value]))
  });
}
