'use client'

import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Settings, User, Lock, Palette } from "lucide-react";
import { GeneralTab } from "@/components/sidebar/general-tab";
import { SecurityTab } from "@/components/sidebar/security-tab";
import { AppearanceTab } from "@/components/sidebar/appearance-tab";

interface UserData {
  username: string;
  userUUID: string;
  role: { id: number; label: string; hexColor: string } | null;
}

interface SettingsDialogProps {
  isOpen: boolean;
  onClose: () => void;
  userData: UserData;
}

export function SettingsDialog({ isOpen, onClose, userData }: SettingsDialogProps) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-4xl bg-background border border-border">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold text-foreground flex items-center space-x-2">
            <Settings className="w-5 h-5" />
            <span>Settings</span>
          </DialogTitle>
        </DialogHeader>
        
        <Tabs defaultValue="account" className="w-full">
          <TabsList className="grid w-full grid-cols-3 bg-muted border border-border">
            <TabsTrigger value="account" className="flex items-center space-x-2">
              <User className="w-4 h-4" />
              <span>Account</span>
            </TabsTrigger>
            <TabsTrigger value="security" className="flex items-center space-x-2">
              <Lock className="w-4 h-4" />
              <span>Security</span>
            </TabsTrigger>
            <TabsTrigger value="appearance" className="flex items-center space-x-2">
              <Palette className="w-4 h-4" />
              <span>Appearance</span>
            </TabsTrigger>
          </TabsList>
          
          <TabsContent value="account" className="space-y-4 mt-6">
            <GeneralTab user={userData} skinHeadUrl={`https://mineskin.eu/helm/${userData.username}/64`} />
          </TabsContent>
          
          <TabsContent value="security" className="space-y-6 mt-6">
            <SecurityTab />
          </TabsContent>
          
          <TabsContent value="appearance" className="space-y-4 mt-6">
            <AppearanceTab />
          </TabsContent>
        </Tabs>
      </DialogContent>
    </Dialog>
  );
}
