import { createContext, ReactNode, useEffect, useState } from "react";

interface Props {
  children: ReactNode;
}

interface ThemeInterface {
  isDark: boolean;
  changeTheme: () => void;
}

export const ThemeContext = createContext<ThemeInterface | null>(null);

export const ThemeProvider = ({ children }: Props) => {
  const [isDark, setIsDark] = useState(
    JSON.parse(localStorage.getItem("darkTheme")!) || false
  );

  const changeTheme = () => {
    setIsDark(!isDark);
  };

  useEffect(() => {
    if (isDark) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
    localStorage.setItem("darkTheme", String(isDark));
  }, [isDark]);

  const themeValue: ThemeInterface = {
    isDark,
    changeTheme,
  };

  return (
    <ThemeContext.Provider value={themeValue}>{children}</ThemeContext.Provider>
  );
};
