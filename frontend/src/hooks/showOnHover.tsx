import { RefObject, useEffect } from "react";

const useShowOnHover = (
  refToObserve: RefObject<HTMLElement>,
  refToShow: RefObject<HTMLElement>
) => {
  useEffect(() => {
    if (null != refToObserve.current) {
      refToObserve.current!.addEventListener("mouseover", () => {
        refToShow.current!.classList.add("flex");
        refToShow.current!.classList.remove("hidden");
      });

      refToObserve.current!.addEventListener("mouseout", () => {
        refToShow.current!.classList.remove("flex");
        refToShow.current!.classList.add("hidden");
      });
    }
  }, [refToObserve, refToShow]);
};

export default useShowOnHover;
