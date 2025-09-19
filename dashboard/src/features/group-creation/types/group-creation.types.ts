export interface Platform {
  name: string;
  type: string;
  versions: Array<{ version: string }>;
}

export interface GroupCreateData {
  name: string;
  platform: {
    name: string;
    version: string;
  };
  memory: {
    min: string;
    max: string;
  };
  templates: string[];
  advanced: {
    percentageNewService: string;
    minServicesOnline: string;
    maxServicesOnline: string;
    fallback: boolean;
    static: boolean;
  };
}

export interface GroupPreviewProps {
  formData: GroupCreateData;
  currentStep: number;
}

export interface GroupDetailsStepProps {
  formData: {
    name: string;
  };
  onNameChange: (name: string) => void;
}

export interface PlatformSelectionStepProps {
  platforms: Platform[];
  selectedPlatform: string;
  onPlatformSelect: (platformName: string) => void;
}

export interface VersionSelectionStepProps {
  selectedPlatform: Platform | undefined;
  selectedVersion: string;
  onVersionSelect: (version: string) => void;
}

export interface MemoryConfigurationStepProps {
  formData: {
    memory: {
      min: string;
      max: string;
    };
  };
  onMemoryChange: (type: 'min' | 'max', value: string) => void;
}

export interface TemplateSelectionStepProps {
  formData: {
    templates: string[];
  };
  onTemplateAdd: (template: string) => void;
  onTemplateRemove: (template: string) => void;
}

export interface AdvancedSettingsStepProps {
  formData: {
    advanced: {
      percentageNewService: string;
      minServicesOnline: string;
      maxServicesOnline: string;
      fallback: boolean;
      static: boolean;
    };
  };
  onAdvancedChange: (field: keyof GroupCreateData['advanced'], value: string | boolean) => void;
}

export interface GroupCreatedAnimationProps {
  isVisible: boolean;
  onAnimationComplete: () => void;
}
