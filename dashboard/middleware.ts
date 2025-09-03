import { NextRequest, NextResponse } from 'next/server'

export function middleware(request: NextRequest) {
  
  if (request.nextUrl.pathname.startsWith('/polocloud/api/v3/')) {
    let backendIp: string | undefined = request.cookies.get('backend_ip')?.value
    
    if (!backendIp) {
      backendIp = request.nextUrl.searchParams.get('backend_ip') || undefined
    }
    
    if (!backendIp) {
      return NextResponse.next()
    }
    
    const backendUrl = decodeURIComponent(backendIp)
    
    if (request.method === 'OPTIONS') {
      return new NextResponse(null, {
        status: 200,
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'GET, POST, PUT, PATCH, DELETE, OPTIONS',
          'Access-Control-Allow-Headers': 'Content-Type, Authorization, Accept',
          'Access-Control-Max-Age': '86400',
        },
      })
    }
    
    const backendBaseUrl = `http://${backendUrl}`
    const backendUrlFull = new URL(request.nextUrl.pathname, backendBaseUrl)
    
    const searchParams = new URLSearchParams(request.nextUrl.search)
    searchParams.delete('backend_ip')
    if (searchParams.toString()) {
      backendUrlFull.search = searchParams.toString()
    }
    
    return NextResponse.rewrite(backendUrlFull)
  } else {
  }
  
  return NextResponse.next()
}

export const config = {
  matcher: ['/polocloud/api/v3/:path*']
}
