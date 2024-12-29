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
import { SetupStepProps } from '@/lib/steps/SetupStepProps';
import { cn } from '@/lib/utils';

const memoryOptions = [512, 1024, 2048, 4096];

export const SelectMemoryStep: React.FC<SetupStepProps<CreateGroupObject>> = ({
  onNext,
  isOnFocus,
  object,
  setObject,
}) => {
  return (
    <Card
      className="aria-disabled:opacity-50 aria-disabled:pointer-events-none aria-disabled:cursor-not-allowed bg-primary-foreground transition-all"
      aria-disabled={!isOnFocus}
    >
      <CardHeader>
        <CardTitle>Select Memory</CardTitle>
        <CardDescription>Please allocate the memory for your Group.</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col space-y-4">
          {memoryOptions.map((memory) => (
            <div
              onClick={() => setObject({ ...object, maxMemory: memory })}
              className={cn(
                'p-4 border rounded-md items-center justify-center flex flex-col cursor-pointer transition-all',
                object.maxMemory === memory && 'bg-muted border-white shadow-md'
              )}
              key={memory}
            >
              <p className="font-semibold text-xl">{memory}mb</p>
            </div>
          ))}
        </div>
      </CardContent>
      <CardFooter className="border-t px-6 py-4">
        <Button
          disabled={object.maxMemory == undefined || !isOnFocus}
          onClick={onNext}
        >
          Weiter
        </Button>
      </CardFooter>
    </Card>
  );
};
