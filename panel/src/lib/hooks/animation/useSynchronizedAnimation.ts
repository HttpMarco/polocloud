//Thanks to: https://github.com/samselikoff/2022-02-24-use-synchronized-animation

import { useLayoutEffect, useRef } from 'react';

let stashedTime: CSSNumberish | null = null;

export function useSynchronizedAnimation<T>(animationName: string) {
  let ref = useRef<T | null>(null);

  useLayoutEffect(() => {
    let animations = document
      .getAnimations()
      //@ts-ignore
      .filter((animation) => animation.animationName === animationName);

    let myAnimation = animations.find(
      //@ts-ignore
      (animation) => animation.effect?.target === ref.current
    );

    if (myAnimation === animations[0] && stashedTime) {
      myAnimation.currentTime = stashedTime;
    }

    if (myAnimation && myAnimation !== animations[0]) {
      myAnimation.currentTime = animations[0].currentTime;
    }

    return () => {
      if (myAnimation && myAnimation === animations[0]) {
        stashedTime = myAnimation.currentTime;
      }
    };
  }, [animationName]);

  return ref;
}
