interface MainContextI {
  errorAlert: string;
  successAlert: string;
  showSettings: boolean;
  showAbout: boolean;
  showCalendar: boolean;
  setErrorAlert: React.Dispatch<React.SetStateAction<string>>;
  setSuccessAlert: React.Dispatch<React.SetStateAction<string>>;
  setShowSettings: React.Dispatch<React.SetStateAction<boolean>>;
  setShowAbout: React.Dispatch<React.SetStateAction<boolean>>;
  changeShowCalendar: () => void;
}
