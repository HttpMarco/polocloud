import { cn } from '@/lib/utils';

interface CurrentProgressTrackProps {
  currentStep: number;
  labels: string[];
  onProgressClick: (step: number) => void;
}

const CurrentProgressTrack: React.FC<CurrentProgressTrackProps> = ({
  currentStep,
  labels,
  onProgressClick,
}) => {
  return (
    <div className="flex flex-col p-2">
      {labels.map((label, index) => (
        <div
          onClick={() => onProgressClick(index)}
          className="flex flex-col cursor-pointer"
          key={index}
        >
          <div className="flex flex-row items-center space-x-2">
            <div
              className={cn(
                'size-4 rounded-full bg-primary transition-all',
                currentStep !== index && 'opacity-30'
              )}
            />
            <p
              className={cn(
                'transition-all text-primary text-sm',
                currentStep !== index && 'opacity-30'
              )}
            >
              {label}
            </p>
          </div>

          {index < labels.length - 1 && (
            <div className="w-px ml-2 h-8 py-2 flex-grow bg-muted-foreground"></div>
          )}
        </div>
      ))}
    </div>
  );
};

export default CurrentProgressTrack;
