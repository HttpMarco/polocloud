import { useEffect, useRef } from "react";
  
  export default function useUpdateEffect(callback: () => void, dependencies: unknown[]) {
    const firstRenderRef = useRef(true);
  
    useEffect(() => {
      if (firstRenderRef.current) {
        firstRenderRef.current = false;
        return;
      }
      return callback();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [callback, ...dependencies]);
  }