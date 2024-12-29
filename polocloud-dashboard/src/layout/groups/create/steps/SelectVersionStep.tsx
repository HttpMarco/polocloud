import LoadingSpinner from '@/components/system/LoadingSpinner';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { CreateGroupObject } from '@/layout/groups/create/CreateGroupLayout';
import usePlatformVersions from '@/lib/hooks/api/usePlatformVersions';
import { SetupStepProps } from '@/lib/steps/SetupStepProps';
import { cn } from '@/lib/utils';
import { Info } from 'lucide-react';
export const SelectVersionStep: React.FC<SetupStepProps<CreateGroupObject>> = ({
  onNext,
  isOnFocus,
  object,
  setObject,
}) => {
  const { data: versions, isLoading } = usePlatformVersions({
    id: object.platform,
  });

  return (
    <Card
      className="aria-disabled:opacity-50 aria-disabled:pointer-events-none aria-disabled:cursor-not-allowed bg-primary-foreground transition-all"
      aria-disabled={!isOnFocus}
    >
      <CardHeader>
        <CardTitle>Select Version</CardTitle>
        <CardDescription>
          Select from different version of the platform to create your new Group.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!object.platform ? (
          <div className="flex flex-col items-center justify-center space-y-2">
            <Info />
            <div>
              <p className="text-lg font-semibold">Waiting for selection</p>
              <p className="text-sm text-muted-foreground">
                Please select a platform first
              </p>
            </div>
          </div>
        ) : !versions || isLoading ? (
          <LoadingSpinner />
        ) : (
          <div className="flex flex-col space-y-4">
            {versions.versions.map((version) => (
              <div
                onClick={() => setObject({ ...object, version: version })}
                className={cn(
                  'p-4 border rounded-md items-center justify-center flex flex-col cursor-pointer transition-all',
                  object.version === version && 'bg-muted border-white shadow-md'
                )}
                key={version}
              >
                <p className="capitalize font-semibold text-xl">{version}</p>
              </div>
            ))}
          </div>
        )}
      </CardContent>
      <CardFooter className="border-t px-6 py-4">
        <Button
          disabled={object.version == undefined || !isOnFocus}
          onClick={onNext}
        >
          Weiter
        </Button>
      </CardFooter>
    </Card>
  );
};
