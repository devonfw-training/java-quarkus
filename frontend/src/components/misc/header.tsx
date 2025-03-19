import { InfoIcon, SettingsIcon } from "lucide-react";
import { useContext, useEffect, useRef } from "react";
import { Link } from "wouter";
import { MainContext } from "../../provider/mainProvider";
import { Settings } from "../menus/settingsMenu";
import About from "../overlay/aboutOverlay";

export default function Header() {
  const { showSettings, setShowAbout, setShowSettings } =
    useContext(MainContext)!;
  const headerRef = useRef(null);
  const aboutRef = useRef(null);
  const settingsButtonRef = useRef(null);
  const aboutButtonRef = useRef(null);

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
          <h1 className="text-xl font-semibold text-white">MY TODOS</h1>
        </Link>
        <div className="flex gap-7 items-center">
          <button
            className="flex gap-2 items-center cursor-pointer"
            onClick={() => setShowSettings(!showSettings)}
            ref={settingsButtonRef}
          >
            <SettingsIcon className="h-4 w-4 text-white" />
            <h1 className="text-base font-semibold text-white">SETTINGS</h1>
          </button>
          <button
            className="flex gap-2 items-center cursor-pointer"
            onClick={() => setShowAbout(true)}
            ref={aboutButtonRef}
          >
            <InfoIcon className="h-4 w-4 text-white" />
            <h1 className="text-base font-semibold text-white">ABOUT</h1>
          </button>
        </div>
      </header>
      <Settings ignoreClick={[settingsButtonRef]} />
      <About ref={aboutRef} ignoreClick={[aboutButtonRef]} />
    </div>
  );
}
