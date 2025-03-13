import { InfoIcon, SettingsIcon } from "lucide-react";
import { Link } from "wouter";

export default function Header() {
  return (
    <header className="p-6 pr-12 flex items-center justify-between w-full bg-primary">
      <Link href="/" className="flex gap-2 items-center">
        <img src="logo.png" alt="Logo" className="h-6 w-6" />
        <h1 className="text-xl font-semibold text-white">MY TODO'S</h1>
      </Link>
      <div className="flex gap-7 items-center">
        <button className="flex gap-2 items-center cursor-pointer">
          <SettingsIcon className="h-4 w-4 text-white" />
          <h1 className="text-base font-semibold text-white">SETTINGS</h1>
        </button>
        <button
          className="flex gap-2 items-center cursor-pointer"
          onClick={() => {}}
        >
          <InfoIcon className="h-4 w-4 text-white" />
          <h1 className="text-base font-semibold text-white">ABOUT</h1>
        </button>
      </div>
    </header>
  );
}
