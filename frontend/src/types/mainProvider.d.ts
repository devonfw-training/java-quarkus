interface MainContextInterface {
  errorAlert: string;
  showSettings: boolean;
  showAbout: boolean;
  showCalendar: boolean;
  setErrorAlert: React.Dispatch<React.SetStateAction<string>>;
  changeShowSettings: () => void;
  setShowAbout: React.Dispatch<React.SetStateAction<boolean>>;
  changeShowCalendar: () => void;
}
