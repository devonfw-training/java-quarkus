import { createContext, useState } from "react";

export const DeleteConfirmContext =
  createContext<DeleteConfirmInterface | null>(null);

export const DeleteConfirmProvider = ({ children }: Props) => {
  const [isDeleteConfirmation, setIsDeleteConfirmation] = useState(
    JSON.parse(localStorage.getItem("deleteConfirmation")!) || false
  );

  const changeDeleteConfirm = () => {
    localStorage.setItem("deleteConfirmation", String(!isDeleteConfirmation));
    setIsDeleteConfirmation(!isDeleteConfirmation);
  };

  return (
    <DeleteConfirmContext.Provider
      value={{ isDeleteConfirmation, changeDeleteConfirm }}
    >
      {children}
    </DeleteConfirmContext.Provider>
  );
};
