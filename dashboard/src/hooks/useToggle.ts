import { useState } from "react";
  
  export default function useToggle(defaultValue: boolean) {
    const [value, setValue] = useState<boolean>(defaultValue);
  
    function toggleValue(value: boolean | undefined) {
      setValue((currentValue: boolean) =>
        typeof value === "boolean" ? value : !currentValue
      );
    }
  
    return [value, toggleValue];
  }