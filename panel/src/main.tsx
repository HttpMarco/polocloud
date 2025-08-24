import {StrictMode} from 'react';
import {createRoot} from 'react-dom/client';
import './index.css';
import App from './App.tsx';
import {ThemeProvider} from '@/lib/providers/theme-provider.tsx';
import {QueryClient, QueryClientProvider} from '@tanstack/react-query';

const queryClient = new QueryClient();

createRoot(document.getElementById('root')!).render(
    <QueryClientProvider client={queryClient}>
        <StrictMode>
            <ThemeProvider defaultTheme="dark">
                <App/>
            </ThemeProvider>
        </StrictMode>
    </QueryClientProvider>
);
