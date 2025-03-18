import { ReactNode, RefObject, useRef } from "react";
import useOutsideClick from "../../hooks/outsideClick";

interface Props {
  children: ReactNode;
  show: boolean;
  close: () => void;
}

const DialogBase = ({ children, show, close }: Props) => {
  const refObject = useRef(null);
  useOutsideClick([refObject as RefObject<HTMLElement>], () => close(), show);

  return (
    <div
      className={`fixed z-100 w-screen h-screen top-0 left-0 bg-black-alpha-95 flex flex-col justify-center items-center ${
        show ? "" : "hidden"
      }`}
    >
      <div ref={refObject}>{children}</div>
    </div>
  );
};

export default DialogBase;
