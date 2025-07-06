import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Switch } from '@/components/ui/switch';
import { CreateGroupObject } from '@/layout/groups/create/CreateGroupLayout';
import useCreateClusterGroup from '@/lib/hooks/api/useCreateClusterGroup';
import { SetupStepProps } from '@/lib/steps/SetupStepProps';
import { HardDrive, Maximize2, Minimize2, Shield } from 'lucide-react';
import { useState } from 'react';
import { toast } from 'sonner';
import { useLocation } from 'wouter';

export const AdvancedStep: React.FC<SetupStepProps<CreateGroupObject>> = ({
  isOnFocus,
  object,
  setObject,
}) => {
  const [, setLocation] = useLocation();
  const [isProcessing, setIsProcessing] = useState(false);
  const { mutateAsync } = useCreateClusterGroup();

  function onSubmit() {
    toast.promise(
      async () =>
        mutateAsync({
          fallback: object.fallback,
          maxMemory: object.maxMemory,
          maxOnlineServices: object.maxOnlineServices,
          minOnlineServices: object.minOnlineServices,
          name: object.name,
          platform: object.platform || '',
          staticService: object.staticService,
          version: object.version || '',
        }),
      {
        loading: 'Creating Group...',
        success: () => {
          setLocation('/groups');
          return 'Group created successfully';
        },
        error: (error) => {
          console.log(error.response.data);

          if (error.response?.data?.errorCode === 'group/alreadyExists') {
            return 'This group already exists. Please choose a different name.';
          }

          return 'Failed to create Group';
        },
        finally: () => setIsProcessing(false),
      }
    );
  }

  return (
    <Card
      className="aria-disabled:opacity-50 aria-disabled:pointer-events-none aria-disabled:cursor-not-allowed bg-primary-foreground transition-all"
      aria-disabled={!isOnFocus}
    >
      <CardHeader>
        <CardTitle>Advanced Settings</CardTitle>
        <CardDescription>
          Here you can enable or disable advanced settings for your Group.
        </CardDescription>
      </CardHeader>
      <CardContent className="flex flex-col space-y-2">
        <Card className="p-6">
          <div className="flex flex-row items-center justify-between">
            <div className="flex flex-row items-center space-x-2">
              <div className="min-w-12 flex items-center justify-center">
                <HardDrive className="size-10" />
              </div>
              <div>
                <div className="flex flex-row items-center space-x-2">
                  <p className="text-lg font-semibold">Static Service</p>
                </div>
                <p className="text-muted-foreground text-sm">
                  Static services are not reset on a restart.
                </p>
              </div>
            </div>
            <div className="px-4 scale-125 transform-gpu">
              <Switch
                checked={object.staticService}
                onCheckedChange={(checked) =>
                  setObject({ ...object, staticService: checked })
                }
              />
            </div>
          </div>
        </Card>
        {object.fallbackAvailable && (
          <Card className="p-6">
            <div className="flex flex-row items-center justify-between">
              <div className="flex flex-row items-center space-x-2">
                <div className="min-w-12 flex items-center justify-center">
                  <Shield className="size-10" />
                </div>
                <div>
                  <div className="flex flex-row items-center space-x-2">
                    <p className="text-lg font-semibold">Fallback Group</p>
                  </div>
                  <p className="text-muted-foreground text-sm">
                    This group will be used as default group on join.
                  </p>
                </div>
              </div>
              <div className="px-4 scale-125 transform-gpu">
                <Switch
                  checked={object.fallback}
                  onCheckedChange={(checked) =>
                    setObject({ ...object, fallback: checked })
                  }
                />
              </div>
            </div>
          </Card>
        )}
        <Card className="p-6">
          <div className="flex flex-row items-center justify-between">
            <div className="flex flex-row items-center space-x-2">
              <div className="min-w-12 flex items-center justify-center">
                <Minimize2 className="size-10" />
              </div>
              <div>
                <div className="flex flex-row items-center space-x-2">
                  <p className="text-lg font-semibold">Minimal service count</p>
                </div>
                <p className="text-muted-foreground text-sm">
                  How many service should be minimal online?
                </p>
              </div>
            </div>
            <div className="px-4 scale-125 transform-gpu">
              <Input
                type="number"
                min={1}
                max={100}
                defaultValue={1}
                value={object.minOnlineServices}
                onChange={(e) =>
                  setObject({
                    ...object,
                    minOnlineServices: parseInt(e.target.value),
                  })
                }
              />
            </div>
          </div>
        </Card>
        <Card className="p-6">
          <div className="flex flex-row items-center justify-between">
            <div className="flex flex-row items-center space-x-2">
              <div className="min-w-12 flex items-center justify-center">
                <Maximize2 className="size-10" />
              </div>
              <div>
                <div className="flex flex-row items-center space-x-2">
                  <p className="text-lg font-semibold">Maximal service count</p>
                </div>
                <p className="text-muted-foreground text-sm">
                  How many service should be maximal online?
                </p>
              </div>
            </div>
            <div className="px-4 scale-125 transform-gpu">
              <Input
                type="number"
                min={1}
                max={100}
                defaultValue={1}
                value={object.maxOnlineServices}
                onChange={(e) =>
                  setObject({
                    ...object,
                    maxOnlineServices: parseInt(e.target.value),
                  })
                }
              />
            </div>
          </div>
        </Card>
      </CardContent>
      <CardFooter className="border-t px-6 py-4">
        <Button
          disabled={object.maxMemory == undefined || !isOnFocus || isProcessing}
          onClick={onSubmit}
        >
          Create
        </Button>
      </CardFooter>
    </Card>
  );
};
