import { useBreadcrumbPage } from '@/components/system/breadcrumb/hook/useBreadcrumbPage';
import CurrentProgressTrack from '@/components/system/step/CurrentProgressTrack';
import { AdvancedStep } from '@/layout/groups/create/steps/AdvancedStep';
import { BasicsStep } from '@/layout/groups/create/steps/BasicsStep';
import { SelectMemoryStep } from '@/layout/groups/create/steps/SelectMemoryStep';
import { SelectPlatformStep } from '@/layout/groups/create/steps/SelectPlatformStep';
import { SelectVersionStep } from '@/layout/groups/create/steps/SelectVersionStep';
import { getPlatformIcon } from '@/lib/api/entities/Platform';
import { SetupStepProps } from '@/lib/steps/SetupStepProps';
import { cn } from '@/lib/utils';
import { ArrowLeft } from 'lucide-react';
import React, { useEffect, useRef, useState } from 'react';
import { useLocation } from 'wouter';

interface OrganizationSetupStep {
  component: React.FC<SetupStepProps<any>>;
  label: string;
}

export interface CreateGroupObject {
  name: string;
  platform?: string;
  version?: string;
  maxMemory: number;
  fallbackAvailable: boolean;
  staticService: boolean;
  fallback: boolean;
  minOnlineServices: number;
  maxOnlineServices: number;
}

const steps: OrganizationSetupStep[] = [
  {
    component: BasicsStep,
    label: 'Basics',
  },
  {
    component: SelectPlatformStep,
    label: 'Select Platform',
  },
  {
    component: SelectVersionStep,
    label: 'Select Version',
  },
  {
    component: SelectMemoryStep,
    label: 'Select Memory',
  },
  {
    component: AdvancedStep,
    label: 'Advanced Settings',
  },
];

const NewOrganizationRoute = () => {
  useBreadcrumbPage({
    items: [
      {
        activeHref: '/groups',
        href: '/groups',
        label: 'Groups',
      },
      {
        activeHref: '/groups/create',
        href: '/groups/create',
        label: 'New Group',
      },
    ],
  });
  const [, setLocation] = useLocation();
  const [currentStep, setCurrentStep] = useState(0);
  const [object, setObject] = useState<CreateGroupObject>({
    name: '',
    fallbackAvailable: false,
    fallback: false,
    staticService: false,
    maxMemory: 512,
    maxOnlineServices: 1,
    minOnlineServices: 1,
  });

  const stepRefs = useRef<(HTMLDivElement | null)[]>([]);

  useEffect(() => {
    if (stepRefs.current[currentStep] && currentStep > 0) {
      stepRefs.current[currentStep]?.scrollIntoView({
        behavior: 'smooth',
        block: 'center',
      });
    }
  }, [currentStep]);

  function onNext(toStep: number) {
    if (toStep === currentStep || toStep < 0 || toStep > steps.length - 1) return;
    setCurrentStep(toStep);
  }

  return (
    <div className="flex flex-col flex-1 relative">
      <div className="bg-secondary w-full h-64 absolute top-0 left-0 z-0 border-b" />
      <div className="flex flex-col max-w-6xl w-full mx-auto p-8 z-10">
        <div>
          <button
            onClick={() => setLocation('/safe')}
            className="flex flex-row space-x-2 items-center hover:bg-primary-foreground px-2 py-1 rounded-lg transition-all cursor-pointer"
          >
            <ArrowLeft className="size-4" />
            <p className="text-sm">Back</p>
          </button>
          <p className="text-3xl font-semibold">Lets start...</p>
          <p className="text-muted-foreground text-sm">
            Please follow the following steps to create a new group.
          </p>
        </div>

        <div className="flex flex-col lg:flex-row justify-between space-x-8 pt-14">
          <div className="flex flex-col w-full lg:w-64 space-y-8 lg:sticky top-4 self-center lg:self-start">
            <div className="rounded-lg p-4 flex flex-row items-center space-x-2 bg-primary-foreground shadow-md">
              {object.platform && getPlatformIcon(object.platform) && (
                <img
                  src={getPlatformIcon(object.platform)}
                  className="size-6 object-contain"
                  alt="Platform Icon"
                />
              )}
              <p>
                {object && object.name.trim() == '' ? 'New Group' : object?.name}
              </p>
            </div>

            <CurrentProgressTrack
              currentStep={currentStep}
              labels={steps.map((step) => step.label)}
              onProgressClick={(step) => step < currentStep && onNext(step)}
            />

            <div className="border-t w-full p-2 flex flex-col">
              <p className="text-xs uppercase text-muted-foreground">
                Will be created on Cluster
              </p>
              <div className="flex flex-row items-center space-x-2 p-2">
                <div
                  className={cn(
                    'flex aspect-square size-6 items-center group-data-[collapsible=icon]:size-8 rounded-lg justify-center animate-pulse text-sidebar-primary-foreground bg-green-500'
                  )}
                >
                  <p className="font-bold text-lg">P</p>
                </div>
                <p className="text-sm">Productive</p>
              </div>
            </div>
          </div>

          <div className="flex-1 flex flex-col space-y-8 pb-32 relative">
            {steps.map((step, index) => {
              return (
                <div key={index} ref={(el) => (stepRefs.current[index] = el)}>
                  <step.component
                    object={object}
                    setObject={setObject}
                    disabled={index > currentStep}
                    onNext={() => onNext(index + 1)}
                    isOnFocus={index === currentStep}
                  />
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};

export default NewOrganizationRoute;
