 import { useEffect, useRef, useCallback } from "react";

    type TimeoutHandle = ReturnType<typeof setTimeout> | undefined;
    
    export default function useTimeout(
      callback: () => void,
      delay: number
    ): {
      reset: () => void;
      clear: () => void;
    } {
      const callbackRef = useRef(callback);
      const timeoutRef = useRef<TimeoutHandle>();
    
      useEffect(() => {
        callbackRef.current = callback;
      }, [callback]);
    
      const set = useCallback(() => {
        timeoutRef.current = setTimeout(() => callbackRef.current(), delay);
      }, [delay]);
    
      const clear = useCallback(() => {
        if (timeoutRef.current) {
          clearTimeout(timeoutRef.current);
        }
      }, []);
    
      useEffect(() => {
        set();
        return clear;
      }, [delay, set, clear]);
    
      const reset = useCallback(() => {
        clear();
        set();
      }, [clear, set]);
    
      return { reset, clear };
    }