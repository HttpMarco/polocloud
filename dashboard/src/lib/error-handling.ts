
export interface ErrorContext {
  component?: string;
  action?: string;
  userId?: string;
  additionalData?: Record<string, unknown>;
}

export function logError(
  error: unknown, 
  context: ErrorContext = {},
  fallbackMessage: string = 'An error occurred'
): void {
  const isDevelopment = process.env.NODE_ENV === 'development';
  
  if (isDevelopment) {
    console.error(`[${context.component || 'Unknown'}] ${context.action || 'Action'}:`, {
      error,
      context,
      timestamp: new Date().toISOString()
    });
  } else {
    console.error(`[${context.component || 'Unknown'}] ${fallbackMessage}`);
  }
}


export function handleApiError<T = unknown>(
  error: unknown,
  context: ErrorContext,
  fallbackData: T | null = null
): { data: T | null; error: string | null } {
  logError(error, context, 'API request failed');
  
  return {
    data: fallbackData,
    error: process.env.NODE_ENV === 'development' 
      ? (error instanceof Error ? error.message : 'Unknown error')
      : 'Request failed'
  };
}

export function handleWebSocketError(
  error: unknown,
  context: ErrorContext
): void {
  logError(error, { ...context, action: 'WebSocket' }, 'WebSocket connection failed');
}

export function handleUIError(
  error: unknown,
  context: ErrorContext,
  setError?: (error: string) => void
): void {
  logError(error, context, 'UI operation failed');
  
  if (setError) {
    const userMessage = process.env.NODE_ENV === 'development'
      ? (error instanceof Error ? error.message : 'Unknown error')
      : 'Something went wrong. Please try again.';
    
    setError(userMessage);
  }
}

export async function safeAsync<T>(
  operation: () => Promise<T>,
  context: ErrorContext,
  fallback: T
): Promise<T> {
  try {
    return await operation();
  } catch (error) {
    logError(error, context, 'Async operation failed');
    return fallback;
  }
}
