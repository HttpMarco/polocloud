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
import { Label } from '@/components/ui/label';
import { CreateGroupObject } from '@/layout/groups/create/CreateGroupLayout';
import { SetupStepProps } from '@/lib/steps/SetupStepProps';

export const BasicsStep: React.FC<SetupStepProps<CreateGroupObject>> = ({
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
        <CardTitle>Create a new Group</CardTitle>
        <CardDescription>
          Give basic information about your new Group.
        </CardDescription>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col space-y-1.5">
          <Label htmlFor="organizationName">Name</Label>
          <Input
            disabled={!isOnFocus}
            id="organizationName"
            placeholder="Name of the Group"
            value={object?.name || ''}
            className="w-full"
            onChange={(e) => setObject({ ...object, name: e.target.value })}
          />
        </div>
      </CardContent>
      <CardFooter className="border-t px-6 py-4">
        <Button disabled={object?.name.trim() === '' || !isOnFocus} onClick={onNext}>
          Weiter
        </Button>
      </CardFooter>
    </Card>
  );
};
