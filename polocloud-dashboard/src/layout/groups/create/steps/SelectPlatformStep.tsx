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
import { getPlatformIcon } from '@/lib/api/entities/Platform';
import usePlatforms from '@/lib/hooks/api/usePlatforms';
import { SetupStepProps } from '@/lib/steps/SetupStepProps';
import { cn } from '@/lib/utils';
export const SelectPlatformStep: React.FC<SetupStepProps<CreateGroupObject>> = ({
  onNext,
  isOnFocus,
  object,
  setObject,
}) => {
  const { data: platforms, isLoading } = usePlatforms();

  return (
    <Card
      className="aria-disabled:opacity-50 aria-disabled:pointer-events-none aria-disabled:cursor-not-allowed bg-primary-foreground transition-all"
      aria-disabled={!isOnFocus}
    >
      <CardHeader>
        <CardTitle>Select Platform</CardTitle>
        <CardDescription>
          Select from a variety of platforms to create your new Group.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!platforms || isLoading ? (
          <LoadingSpinner />
        ) : (
          <div className="grid grid-cols-3 gap-4">
            {platforms.platforms.map((platform) => (
              <div
                onClick={() => setObject({ ...object, platform })}
                className={cn(
                  'p-4 border rounded-md items-center justify-center flex flex-col cursor-pointer transition-all',
                  object.platform === platform && 'bg-muted border-white shadow-md'
                )}
                key={platform}
              >
                {getPlatformIcon(platform) && (
                  <img
                    src={getPlatformIcon(platform)}
                    className="size-8 object-contain"
                    alt=""
                  />
                )}
                <p className="capitalize font-semibold text-xl">{platform}</p>
              </div>
            ))}
          </div>
        )}
      </CardContent>
      <CardFooter className="border-t px-6 py-4">
        <Button
          disabled={object.platform == undefined || !isOnFocus}
          onClick={onNext}
        >
          Weiter
        </Button>
      </CardFooter>
    </Card>
  );
};
