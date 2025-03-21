import { RefObject, useEffect, useState } from "react";

const useHasOverflow = (ref: RefObject<HTMLElement>) => {
  const [hasOverflow, setHasOverflow] = useState(false);

  useEffect(() => {
    const checkOverflow = () => {
      if (ref.current) {
        setHasOverflow(
          ref.current.scrollHeight > ref.current.clientHeight ||
            ref.current.scrollWidth > ref.current.clientWidth
        );
      }
    };

    const observer = new MutationObserver(checkOverflow);

    if (ref.current) {
      observer.observe(ref.current, { childList: true, subtree: true });
    }

    checkOverflow();
    window.addEventListener("resize", checkOverflow);

    return () => {
      observer.disconnect();
      window.removeEventListener("resize", checkOverflow);
    };
  }, [ref]);

  return hasOverflow;
};

export default useHasOverflow;
