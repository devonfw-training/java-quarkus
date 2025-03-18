import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@material-ui/core";
import DialogBase from "./dialogBase";

interface DeleteConfirmI {
  open: boolean;
  close: () => void;
  yes: () => void;
}

export const DeleteConfirm = ({ open, close, yes }: DeleteConfirmI) => {
  return (
    <DialogBase show={open} close={close}>
      <div className="flex flex-col dark:bg-black bg-white border-primary border-2 rounded-lg p-6">
        <h1 className="text-4xl text-primary font-black">DELETE ITEM?</h1>
        <p className="text-xl dark:text-white text-black">
          Are you sure you want to delete this item?
        </p>
        <p className="dark:text-white text-black mt-6">
          <span className="text-green font-bold">PROTIP:</span>
          <br />
          You can hold down shift when clicking the <b>delete button</b> to
          bypass this confirmation entirely
        </p>
        <div className="flex flex-row mt-6 justify-end gap-2.5">
          <button
            className="dark:text-white text-black hover:bg-primary hover:text-white border-light-primary border-2 rounded-lg px-3 py-1.5 cursor-pointer"
            onClick={close}
          >
            No
          </button>
          <button
            className="dark:text-white text-black hover:bg-primary hover:text-white border-light-primary border-2 rounded-lg px-3 py-1.5 cursor-pointer"
            onClick={yes}
          >
            Yes
          </button>
        </div>
      </div>
    </DialogBase>
  );
};

export const DeleteAllConfirm = ({ open, close, yes }: DeleteConfirmI) => {
  return (
    <Dialog open={open} onClose={close}>
      <DialogTitle>DELETE ALL ITEMS?</DialogTitle>
      <DialogContent>
        <DialogContentText>
          Are you sure you want to delete all items?
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={close} color="primary">
          No
        </Button>
        <Button onClick={yes} color="primary" variant="contained">
          Yes
        </Button>
      </DialogActions>
    </Dialog>
  );
};
