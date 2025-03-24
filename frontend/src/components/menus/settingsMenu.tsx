import { RefObject, useContext, useRef } from "react";
import useOutsideClick from "../../hooks/outsideClick";
import { DeleteConfirmContext } from "../../provider/deleteConfirmProvider";
import { MainContext } from "../../provider/mainProvider";
import { ThemeContext } from "../../provider/themeProvider";
import Slider from "../utils/slider";

interface SettingsMenuI {
  ignoreClick: RefObject<HTMLElement>[];
}

export const SettingsMenu = ({ ignoreClick }: SettingsMenuI) => {
  const { showSettings, setShowSettings } = useContext(MainContext)!;
  const { isDeleteConfirmation, changeDeleteConfirm } =
    useContext(DeleteConfirmContext)!;
  const { isDark, changeTheme } = useContext(ThemeContext)!;

  const settingsRef = useRef(null);
  useOutsideClick(
    [settingsRef, ...ignoreClick],
    () => setShowSettings(false),
    showSettings
  );

  return (
    <div
      ref={settingsRef}
      className={`absolute top-16 right-36 z-20 p-3 flex flex-col gap-2.5 dark:bg-light-black bg-white border-primary rounded-2xl border-2 ${
        showSettings ? "" : "hidden"
      }`}
    >
      <div className="flex justify-between items-center gap-2.5">
        <p className="font-light dark:text-white">Darkmode</p>
        <Slider state={isDark} onClick={changeTheme} />
      </div>
      <div className="flex justify-between items-center gap-2.5">
        <p className="font-light dark:text-white">
          Disable delete confirmation
        </p>
        <Slider state={isDeleteConfirmation} onClick={changeDeleteConfirm} />
      </div>
    </div>
  );
};
