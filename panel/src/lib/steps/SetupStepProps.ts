export interface SetupStepProps<T> {
  onNext: () => void;
  disabled: boolean;
  isOnFocus: boolean;
  object: T;
  setObject: (object: T) => void;
}
