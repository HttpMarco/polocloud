import { useEffect } from "react";
    import useTimeout from "./useTimeout";
    
    export default function useDebounce(
  callback: () => void,
  delay: number,
  dependencies: unknown[]
) {
      const { reset, clear } = useTimeout(callback, delay);
      useEffect(reset, [...dependencies, reset]);
      useEffect(clear, [clear]);
    }