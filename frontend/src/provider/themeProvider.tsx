import { createContext, useEffect, useState } from "react";

export const ThemeContext = createContext<ThemeI | null>(null);

export const ThemeProvider = ({ children }: PropsI) => {
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

  return (
    <ThemeContext.Provider value={{ isDark, changeTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};
