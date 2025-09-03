'use client';

import { useState, useEffect, useRef } from 'react';
import { useParams } from 'next/navigation';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Terminal, Send, Trash2, WifiOff, Loader2, Activity } from 'lucide-react';
import { motion } from 'framer-motion';
import GlobalNavbar from '@/components/global-navbar';
import { useServiceWebSocket } from '@/hooks/useServiceWebSocket';


export default function ServiceScreenPage() {
  const params = useParams();
  const serviceName = params.service as string;
  const [command, setCommand] = useState('');
  const logsEndRef = useRef<HTMLDivElement>(null);

  const {
    logs,
    connectionInfo,
    isConnected,
    sendCommand,
    clearLogs
  } = useServiceWebSocket(serviceName, undefined, undefined, true);

  useEffect(() => {
    logsEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [logs]);

  const handleSendCommand = () => {
    if (!command.trim() || !isConnected) return;
    
    sendCommand(command);
    setCommand('');
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      handleSendCommand();
    }
  };

  const StatusBadge = () => {
    if (isConnected) {
      return (
        <Badge variant="secondary" className="px-2 py-1 text-xs bg-green-500/10 text-green-500 border-green-500/20 hover:bg-green-500/20">
          <Activity className="w-3 h-3 mr-1" />
          Connected
        </Badge>
      );
    }
    
    if (connectionInfo.status === 'connecting' || connectionInfo.status === 'reconnecting') {
      return (
        <Badge variant="secondary" className="px-2 py-1 text-xs bg-yellow-500/10 text-yellow-500 border-yellow-500/20 hover:bg-yellow-500/20">
          <Loader2 className="w-3 h-3 mr-1 animate-spin" />
          Connecting...
        </Badge>
      );
    }
    
    return (
      <Badge variant="secondary" className="px-2 py-1 text-xs bg-red-500/10 text-red-500 border-red-500/20 hover:bg-red-500/20">
        <WifiOff className="w-3 h-3 mr-1" />
        Disconnected
      </Badge>
    );
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 ultra-smooth-scroll">
      <GlobalNavbar />
      
      <div className="h-2"></div>

      <div className="px-6 py-6">
        <div className="mb-8">
          <motion.h1 
            className="text-4xl font-bold text-foreground mb-3"
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
          >
            {serviceName}
          </motion.h1>
          <motion.p 
            className="text-lg text-muted-foreground"
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.1 }}
          >
            Service console and command execution
          </motion.p>
        </div>

        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <StatusBadge />
          </div>
          
          <div className="flex items-center gap-2">
            <Button
              onClick={clearLogs}
              variant="outline"
              size="sm"
              disabled={logs.length === 0}
            >
              <Trash2 className="h-4 w-4 mr-2" />
              Clear Logs
            </Button>
          </div>
        </div>
      </div>

      <div className="px-6">
        <Card className="flex flex-col h-[600px]">
          <div className="flex-1 p-4 overflow-auto bg-black font-mono text-sm rounded-t-lg">
            {logs.length === 0 ? (
              <div className="flex items-center justify-center h-full text-gray-400">
                <div className="text-center">
                  <Terminal className="h-12 w-12 mx-auto mb-4 text-gray-600" />
                  <p>No logs yet. Service logs will appear here.</p>
                  <p className="text-xs mt-2 text-gray-500">Monitoring service: {serviceName}</p>
                </div>
              </div>
            ) : (
              <>
                {logs.map((log, index) => (
                  <div key={index} className="mb-1">
                    <span 
                      className="text-white"
                      dangerouslySetInnerHTML={{
                        __html: log
                          .replace(/(INFO):/g, '<span class="text-blue-400">$1</span>:')
                          .replace(/(WARN):/g, '<span class="text-yellow-400">$1</span>:')
                          .replace(/(ERROR):/g, '<span class="text-red-400">$1</span>:')
                          .replace(/(DEBUG):/g, '<span class="text-gray-400">$1</span>:')
                          .replace(/(\d{2}:\d{2}:\d{2})/g, '<span class="text-gray-400">$1</span>')
                          .replace(/- ([a-zA-Z0-9_-]+)( \()/g, '- <span class="text-blue-400">$1</span>$2')
                          .replace(/The service ([a-zA-Z0-9_-]+) is/g, 'The service <span class="text-blue-400">$1</span> is')
                          .replace(/The service ([a-zA-Z0-9_-]+) has been/g, 'The service <span class="text-blue-400">$1</span> has been')
                          .replace(/ - ([a-zA-Z0-9_-]+) \(state=/g, ' - <span class="text-blue-400">$1</span> (state=')
                          .replace(/\b([A-Z_]+[A-Z_]*[A-Z])\b/g, '<span class="text-blue-400">$1</span>')
                      }}
                    />
                  </div>
                ))}
                <div ref={logsEndRef} />
              </>
            )}
          </div>

          <div className="border-t border-gray-800 p-4 bg-black rounded-b-lg">
            <div className="flex items-center gap-2">
              <div className="font-mono text-sm whitespace-nowrap">
                <span className="text-blue-400">polocloud</span>
                <span className="text-gray-400">@</span>
                <span className="text-white">{serviceName}</span>
                <span className="text-gray-400"> » </span>
              </div>

              <Input
                value={command}
                onChange={(e) => setCommand(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder={isConnected ? "Enter service command..." : "Not connected"}
                disabled={!isConnected}
                className="font-mono bg-transparent border-none text-white placeholder-gray-500 focus-visible:ring-0 focus-visible:ring-offset-0 px-0"
              />

              <Button 
                onClick={handleSendCommand}
                disabled={!command.trim() || !isConnected}
                size="sm"
                variant="ghost"
                className="text-gray-400 hover:text-white"
              >
                <Send className="h-4 w-4" />
              </Button>
            </div>
            
            {!isConnected && (
              <div className="text-sm text-red-400 mt-2 flex items-center gap-2">
                <WifiOff className="h-4 w-4" />
                Not connected to service. Please check your connection.
              </div>
            )}
          </div>
        </Card>
      </div>

      {process.env.NODE_ENV === 'development' && (
        <div className="px-6 pb-6">
          <Card className="p-4">
            <h3 className="font-semibold mb-2 text-sm">Debug Info</h3>
            <pre className="text-xs text-muted-foreground overflow-auto bg-muted p-2 rounded">
              {JSON.stringify({ serviceName, connectionInfo }, null, 2)}
            </pre>
          </Card>
        </div>
      )}
    </div>
  );
}
