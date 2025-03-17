import { InfoIcon, SettingsIcon } from "lucide-react";
import { useContext, useEffect, useRef } from "react";
import { Link } from "wouter";
import { DeleteConfirmContext } from "../context/DeleteConfirmContext";
import { MainContext } from "../context/MainContext";
import { ThemeContext } from "../context/ThemeContext";
import About from "../pages/About";
import Slider from "./Slider";

export default function Header() {
  const { showSettings, changeShowSettings, setShowAbout } =
    useContext(MainContext)!;
  const { isDeleteConfirmation, changeDeleteConfirm } =
    useContext(DeleteConfirmContext)!;
  const { isDark, changeTheme } = useContext(ThemeContext)!;

  const headerRef = useRef(null);
  const aboutRef = useRef(null);

  useEffect(() => {
    const header: any = headerRef.current;
    const about: any = aboutRef.current;

    if (header && about) {
      about.style.top = `${header.height}px`; // Adjust as needed
      about.style.height = `calc(100% - ${header.offsetHeight}px)`;
    }
  });

  return (
    <div>
      <header
        ref={headerRef}
        className="p-6 pr-12 flex items-center justify-between w-full bg-primary"
      >
        <Link href="/" className="flex gap-2 items-center">
          <img src="logo.png" alt="Logo" className="h-6 w-6" />
          <h1 className="text-xl font-semibold text-white">MY TODO'S</h1>
        </Link>
        <div className="flex gap-7 items-center">
          <button
            className="flex gap-2 items-center cursor-pointer"
            onClick={changeShowSettings}
          >
            <SettingsIcon className="h-4 w-4 text-white" />
            <h1 className="text-base font-semibold text-white">SETTINGS</h1>
          </button>
          <button
            className="flex gap-2 items-center cursor-pointer"
            onClick={() => setShowAbout(true)}
          >
            <InfoIcon className="h-4 w-4 text-white" />
            <h1 className="text-base font-semibold text-white">ABOUT</h1>
          </button>
        </div>
      </header>
      {/**
       * SETTINGS DROPDOWN
       */}
      <div
        className={`absolute top-16 right-36 z-10 p-3 flex flex-col gap-2.5 bg-white border-primary rounded-2xl border-2 ${
          showSettings ? "" : "hidden"
        }`}
      >
        <div className="flex justify-between items-center gap-2.5">
          <p className="font-light">Darkmode</p>
          <Slider state={isDark} onClick={changeTheme} />
        </div>
        <div className="flex justify-between items-center gap-2.5">
          <p className="font-light">Disable delete confirmation</p>
          <Slider state={isDeleteConfirmation} onClick={changeDeleteConfirm} />
        </div>
      </div>
      <About ref={aboutRef} />
    </div>
  );
}
