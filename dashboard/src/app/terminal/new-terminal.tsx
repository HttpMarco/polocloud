'use client';

import { useState, useEffect, useRef } from 'react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Badge } from '@/components/ui/badge';
import { Terminal, Send, Trash2, WifiOff, Loader2, Activity } from 'lucide-react';
import { motion } from 'framer-motion';
import GlobalNavbar from '@/components/global-navbar';
import { useCentralWebSocket } from '@/hooks/useCentralWebSocket';
import { usePermissions } from '@/hooks/usePermissions';

export default function NewTerminalPage() {
  const [command, setCommand] = useState('');
  const [backendIp, setBackendIp] = useState<string>('');
  const [, setToken] = useState<string>('');
  const logsEndRef = useRef<HTMLDivElement>(null);

  const { hasPermission } = usePermissions();
  const canSendCommands = hasPermission('polocloud.terminal.command');

  useEffect(() => {
    const storedBackendIp = localStorage.getItem('backendIp');
    const storedToken = document.cookie
      .split('; ')
      .find(row => row.startsWith('token='))
      ?.split('=')[1];

    if (storedBackendIp) {
      setBackendIp(decodeURIComponent(storedBackendIp));
    }
    if (storedToken) {
      setToken(storedToken);
    }
  }, []);

  const {
    terminalLogs: logs,
    connectionInfo,
    isConnected,
    sendTerminalCommand: sendCommand,
    clearTerminalLogs: clearLogs
  } = useCentralWebSocket();

  useEffect(() => {
    logsEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [logs]);

  const handleSendCommand = () => {
    if (!command.trim() || !isConnected || !canSendCommands) return;
    
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
            Terminal
          </motion.h1>
          <motion.p 
            className="text-lg text-muted-foreground"
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.1 }}
          >
            Execute commands on your backend server
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
                  <p>No logs yet. Send a command to get started.</p>
                  <p className="text-xs mt-2 text-gray-500">Connected to {backendIp || 'backend'}</p>
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
                          .replace(/polocloud@([a-zA-Z0-9.-]+) » /g, '<span class="text-blue-400">polocloud</span><span class="text-gray-400">@</span><span class="text-white">$1</span> <span class="text-gray-400">»</span> ')
                          .replace(/The group ([a-zA-Z0-9_-]+) has been/g, 'The group <span class="text-blue-400">$1</span> has been')
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
                <span className="text-white">3.0.0-pre.6-SNAPSHOT</span>
                <span className="text-gray-400"> » </span>
              </div>

              <Input
                value={command}
                onChange={(e) => setCommand(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder={
                  !canSendCommands 
                    ? "No permission to send commands" 
                    : isConnected 
                      ? "Enter command..." 
                      : "Not connected"
                }
                disabled={!isConnected || !canSendCommands}
                className={`font-mono bg-transparent border-none text-white focus-visible:ring-0 focus-visible:ring-offset-0 px-0 ${
                  !canSendCommands ? 'placeholder-red-500' : 'placeholder-gray-500'
                }`}
              />

              <Button 
                onClick={handleSendCommand}
                disabled={!command.trim() || !isConnected || !canSendCommands}
                size="sm"
                variant="ghost"
                className={`${
                  !canSendCommands 
                    ? 'text-red-500 cursor-not-allowed' 
                    : 'text-gray-400 hover:text-white'
                }`}
              >
                <Send className="h-4 w-4" />
              </Button>
            </div>
            
            
          </div>
        </Card>
      </div>

    </div>
  );
}
