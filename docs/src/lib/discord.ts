export interface DiscordUser {
  id: string;
  username: string;
  discriminator: string;
  avatar: string;
  email?: string;
  verified: boolean;
}

export interface DiscordAuthResponse {
  access_token: string;
  token_type: string;
  expires_in: number;
  refresh_token: string;
  scope: string;
}

const DISCORD_CLIENT_ID = process.env.DISCORD_CLIENT_ID;
const DISCORD_CLIENT_SECRET = process.env.DISCORD_CLIENT_SECRET;
const DISCORD_REDIRECT_URI = process.env.DISCORD_REDIRECT_URI || 'https://polocloud.de/api/auth/discord/callback';

export function getDiscordAuthUrl(): string {
  if (!DISCORD_CLIENT_ID) {
    throw new Error('Discord OAuth2 not configured');
  }

  const params = new URLSearchParams({
    client_id: DISCORD_CLIENT_ID,
    redirect_uri: DISCORD_REDIRECT_URI,
    response_type: 'code',
    scope: 'identify email',
  });

  return `https://discord.com/api/oauth2/authorize?${params.toString()}`;
}

export async function exchangeCodeForToken(code: string): Promise<DiscordAuthResponse> {
  if (!DISCORD_CLIENT_ID || !DISCORD_CLIENT_SECRET) {
    throw new Error('Discord OAuth2 not configured');
  }

  const tokenResponse = await fetch('https://discord.com/api/oauth2/token', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: new URLSearchParams({
      client_id: DISCORD_CLIENT_ID,
      client_secret: DISCORD_CLIENT_SECRET,
      grant_type: 'authorization_code',
      code,
      redirect_uri: DISCORD_REDIRECT_URI,
    }),
  });

  if (!tokenResponse.ok) {
    const errorText = await tokenResponse.text();
    console.error('Discord token exchange failed:', errorText);
    throw new Error('Failed to exchange code for token');
  }

  return tokenResponse.json();
}

export async function getDiscordUser(accessToken: string): Promise<DiscordUser> {
  const userResponse = await fetch('https://discord.com/api/users/@me', {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (!userResponse.ok) {
    const errorText = await userResponse.text();
    console.error('Discord user fetch failed:', errorText);
    throw new Error('Failed to fetch Discord user');
  }

  return userResponse.json();
}

export async function revokeToken(token: string): Promise<void> {
  if (!DISCORD_CLIENT_ID || !DISCORD_CLIENT_SECRET) {
    return;
  }

  await fetch('https://discord.com/api/oauth2/token/revoke', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: new URLSearchParams({
      client_id: DISCORD_CLIENT_ID,
      client_secret: DISCORD_CLIENT_SECRET,
      token,
    }),
  });
}

interface DiscordEmbed {
  title?: string;
  description?: string;
  color?: number;
  fields?: Array<{
    name: string;
    value: string;
    inline?: boolean;
  }>;
  timestamp?: string;
  footer?: {
    text: string;
    icon_url?: string;
  };
  author?: {
    name: string;
    icon_url?: string;
  };
}

interface DiscordWebhookPayload {
  embeds: DiscordEmbed[];
  username?: string;
  avatar_url?: string;
}

export async function sendDiscordWebhook(
  webhookUrl: string,
  payload: DiscordWebhookPayload
): Promise<boolean> {
  try {
    console.log('Sending Discord webhook to:', webhookUrl);
    
    const response = await fetch(webhookUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error('Discord webhook failed:', response.status, response.statusText, errorText);
      return false;
    }

    console.log('Discord webhook sent successfully');
    return true;
  } catch (error) {
    console.error('Error sending Discord webhook:', error);
    return false;
  }
}

export function createFeedbackNotificationEmbed(
  username: string,
  rating: number,
  description: string,
  userId: string,
  avatarUrl?: string
): DiscordEmbed {
  const stars = '⭐'.repeat(rating);
  const statusEmoji = '⏳';
  
  return {
    title: `${statusEmoji} New Feedback Submitted`,
    description: `A new feedback has been submitted and is waiting for approval.`,
    color: 0x0099ff,
    fields: [
      {
        name: '👤 User',
        value: `\`${username}\``,
        inline: true,
      },
      {
        name: '⭐ Rating',
        value: stars,
        inline: true,
      },
      {
        name: '🆔 User ID',
        value: `\`${userId}\``,
        inline: true,
      },
      {
        name: '📄 Description',
        value: description.length > 1024 
          ? description.substring(0, 1021) + '...' 
          : description,
        inline: false,
      },
      {
        name: '📋 Status',
        value: '**Pending Approval**',
        inline: false,
      },
    ],
    timestamp: new Date().toISOString(),
    footer: {
      text: 'PoloCloud Feedback System • Click to review',
      icon_url: 'https://github.com/HttpMarco/polocloud/blob/development/.img/img.png?raw=true',
    },
    author: {
      name: 'PoloCloud Feedback System',
      icon_url: 'https://github.com/HttpMarco/polocloud/blob/development/.img/img.png?raw=true',
    },
  };
}
