import { useBreadcrumbPage } from '@/components/system/breadcrumb/hook/useBreadcrumbPage';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Instance } from '@/lib/api/entities/instance/Instance';
import { useInstance } from '@/lib/providers/InstanceProvider';
import axios, { AxiosError } from 'axios';
import { useState } from 'react';
import { toast } from 'sonner';
import uuid4 from 'uuid4';
import { Link, useLocation } from 'wouter';

export default function ConnectInstancePage() {
  const [, setLocation] = useLocation();
  const { connectInstance } = useInstance();
  useBreadcrumbPage({
    items: [
      {
        activeHref: '/instance/connect',
        href: '/instance/connect',
        label: 'Connect instance',
      },
    ],
  });
  const [isProcessing, setIsProcessing] = useState(false);

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsProcessing(true);
    toast.promise(
      async () => {
        try {
          const formData = new FormData(event.currentTarget);
          const instanceName = formData.get('instanceName') as string;
          const username = formData.get('username');
          const password = formData.get('password');
          // const hostname = formData.get('hostname');
          const hostname = 'localhost';

          await axios.post(`http://${hostname}:5173/polocloud/api/v1/auth/login`, {
            username,
            password,
          });

          const instance = new Instance({
            hostname,
            id: uuid4(),
            name: instanceName,
            secret: 'secret',
          });
          connectInstance(instance);
          setLocation('/');
        } catch (error: any) {
          if (error instanceof AxiosError) {
            if (error.response?.status === 401) {
              throw new Error('Invalid credentials');
            }
          }
          throw error;
        }
      },
      {
        loading: 'Connecting...',
        success: 'Connected!',
        error: (error) => `Error: ${error}`,
        finally: () => setIsProcessing(false),
      }
    );
  }

  return (
    <div className="flex flex-1 items-center justify-center">
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl">Connect an Instance</CardTitle>
          <CardDescription>
            Please enter your credentials below to connect to your instance.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit}>
            <div className="grid gap-4">
              <div className="grid gap-2">
                <Label htmlFor="email">Instanzname</Label>
                <Input
                  id="instanceName"
                  name="instanceName"
                  type="text"
                  placeholder="Name"
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="email">Username</Label>
                <Input
                  name="username"
                  id="username"
                  type="text"
                  placeholder="John Doe"
                  required
                />
              </div>
              <div className="grid gap-2 pb-2">
                <div className="flex items-center">
                  <Label htmlFor="password">Password</Label>
                </div>
                <Input
                  name="password"
                  id="password"
                  type="password"
                  placeholder="••••••••"
                  required
                />
              </div>

              <div className="pt-4 grid gap-4">
                <div className="grid gap-2">
                  <Label htmlFor="email">Hostname</Label>
                  <Input
                    id="hostname"
                    name="hostname"
                    type="text"
                    placeholder="127.0.0.1"
                    required
                    disabled
                    value="localhost"
                  />
                </div>

                <div className="grid gap-2 pb-2">
                  <div className="flex items-center">
                    <Label htmlFor="password">Secret</Label>
                  </div>
                  <Input
                    disabled
                    id="password"
                    type="password"
                    placeholder="••••••••"
                    required
                  />
                </div>
              </div>

              <Button disabled={isProcessing} type="submit" className="w-full">
                Connect
              </Button>
            </div>
            <div className="mt-4 text-center text-sm text-muted-foreground">
              Don&apos;t know how to connect?{' '}
              <Link href="#" className="underline">
                Guide
              </Link>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
