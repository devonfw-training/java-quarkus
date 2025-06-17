import { RefObject, useEffect } from "react";

const useDisableSelect = (refToDisableSelect: RefObject<HTMLElement>) => {
  useEffect(() => {
    refToDisableSelect.current!.onselectstart = () => {
      return false;
    };
  }, [refToDisableSelect]);
};

export default useDisableSelect;
