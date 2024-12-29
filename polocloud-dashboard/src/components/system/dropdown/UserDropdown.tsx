import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { PopoverClose } from '@radix-ui/react-popover';
import { Cloud, LogOut, Settings } from 'lucide-react';
import { useLocation } from 'wouter';
import { cn } from '../../../lib/utils';
import { Popover, PopoverContent, PopoverTrigger } from '../../ui/popover';

const UserDropdown = () => {
  const [, setLocation] = useLocation();

  return (
    <Popover>
      <PopoverTrigger>
        <Avatar className="size-8">
          <AvatarImage src="https://github.com/iPommes.png" alt="@shadcn" />
          <AvatarFallback>CN</AvatarFallback>
        </Avatar>
      </PopoverTrigger>
      <PopoverContent className="p-0 w-72">
        <div className="flex flex-row space-x-2 items-center border-b p-2">
          <Avatar>
            <AvatarImage src="https://github.com/iPommes.png" alt="@shadcn" />
            <AvatarFallback>CN</AvatarFallback>
          </Avatar>
          <div>
            <div className="flex flex-row justify-between w-full pr-2">
              <p className="font-semibold truncate">iPommes</p>
            </div>
            <div className="flex flex-row items-center space-x-2">
              <p className="text-xs font-medium w-44 truncate">Administrator</p>
            </div>
            <p className="text-xs w-44 truncate">PoloCloud-Account</p>
          </div>
        </div>
        <div className="p-2 border-b">
          <PopoverClose
            onClick={() => setLocation('/settings')}
            className="flex flex-row space-x-1 items-center hover:bg-foreground/10 rounded-lg w-full p-1.5 px-2 transition-all"
          >
            <Settings className={cn('size-4', 'text-cyan-500')} />
            <p className="text-sm pl-1">Account Einstellungen</p>
          </PopoverClose>
          <PopoverClose
            className="flex flex-row space-x-1 items-center hover:bg-foreground/10 rounded-lg w-full p-1.5 px-2 transition-all"
            onClick={() => setLocation('/manage/organization')}
          >
            <Cloud className={cn('size-4', 'text-cyan-500')} />
            <p className="text-sm pl-1">Cloud verwalten</p>
          </PopoverClose>
        </div>
        <div className="p-2 space-y-1.5">
          <button className="flex flex-row space-x-1 items-center hover:bg-foreground/10 rounded-lg w-full p-1.5 px-2 transition-all">
            <LogOut className="size-4 text-red-600" />
            <p className="text-sm text-red-600 pl-1">Abmelden</p>
          </button>
        </div>
      </PopoverContent>
    </Popover>
  );
};
export default UserDropdown;
