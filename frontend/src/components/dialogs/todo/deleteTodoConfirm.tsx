import DialogBase from "../dialogBase";

interface DeleteTodoConfirmI {
  open: boolean;
  close: () => void;
  yes: () => void;
}

export const DeleteTodoConfirm = ({ open, close, yes }: DeleteTodoConfirmI) => {
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
