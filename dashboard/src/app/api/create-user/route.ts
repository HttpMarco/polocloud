import { NextRequest, NextResponse } from 'next/server'
import { buildBackendUrl } from '@/lib/api/utils'

export async function POST(request: NextRequest) {
  try {
    const body = await request.json()
    const { username, password, roleId, backendIp } = body

    if (!backendIp) {
      return NextResponse.json({ error: 'Backend IP fehlt' }, { status: 400 })
    }

    const backendUrl = buildBackendUrl(backendIp, '/polocloud/api/v3/user/self')


    const requestBody = {
        username,
        password,
        roleId
    }

    const response = await fetch(backendUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody)
    })

    if (response.ok) {
      const setCookieHeader = response.headers.get('set-cookie')

      if (setCookieHeader) {

        const response = NextResponse.json({ success: true, message: 'User created successfully' })

        const cookieParts = setCookieHeader.split(';')
        const [nameValue] = cookieParts
        const [name, value] = nameValue.split('=')

        response.cookies.set(name.trim(), value.trim(), {
          httpOnly: true,
          secure: true,
          maxAge: 7 * 24 * 60 * 60,
          path: '/',
          sameSite: 'lax'
        })
        
        return response
      }
      
      return NextResponse.json({ success: true, message: 'User created successfully' })
    } else {
      const errorText = await response.text()
      return NextResponse.json({
        error: `HTTP ${response.status}: ${errorText}`
      }, { status: response.status })
    }

  } catch (error) {
    return NextResponse.json({
      error: error instanceof Error ? error.message : 'User creation failed'
    }, { status: 500 })
  }
}
