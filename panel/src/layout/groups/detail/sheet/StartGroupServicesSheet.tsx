import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { ClusterGroupCard } from '@/layout/groups/card/ClusterGroupCard';
import useStartGroupServicesSheetStore from '@/layout/groups/detail/sheet/StartGroupServicesSheetStore';
import useStartClusterServices from '@/lib/hooks/api/useStartClusterServices';
import { toast } from 'sonner';

export function StartGroupServicesSheet() {
  const { open, setOpen, group } = useStartGroupServicesSheetStore();
  const { mutateAsync } = useStartClusterServices();

  function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    if (!group) return;

    const formData = new FormData(e.currentTarget);
    const count = formData.get('count') as string;
    if (!count || Number.parseInt(count) < 0) {
      toast.warning('Please enter a valid, positive count.');
      return;
    }

    toast.promise(
      async () => mutateAsync({ count: count as unknown as number, group }),
      {
        loading: 'Services will be started.',
        success: 'Request was sent successfully.',
        error: 'There was an error while starting these services.',
      }
    );
    setOpen(false);
  }

  if (!group) {
    return null;
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetContent>
        <form onSubmit={onSubmit}>
          <SheetHeader>
            <SheetTitle>Start services</SheetTitle>
            <SheetDescription>
              Select how many services you want to start of this group.
            </SheetDescription>
          </SheetHeader>

          <div className="grid gap-4 py-4">
            <ClusterGroupCard group={group} />
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="count" className="text-right">
                Count
              </Label>
              <Input
                min={1}
                defaultValue={1}
                id="count"
                name="count"
                type="number"
                placeholder="Count of services..."
                className="col-span-3"
              />
            </div>
          </div>
          <SheetFooter>
            <Button type="submit">Start services</Button>
          </SheetFooter>
        </form>
      </SheetContent>
    </Sheet>
  );
}
