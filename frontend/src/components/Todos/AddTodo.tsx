import { Button, Container, FormControl, TextField } from "@material-ui/core";
import { Add } from "@material-ui/icons";
import { ChangeEvent, FC, useState } from "react";

const AddTodo: FC<{ addTodo: (text: string) => void }> = ({ addTodo }) => {
  const [text, setText] = useState("");
  const [open, setOpen] = useState(false);
  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => setText(e.target.value);
  const createTodo = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    addTodo(text);
    setText("");
    if (text.trim()) setOpen(true);
  };

  return (
    <div>
      <Container maxWidth="sm">
        <form onSubmit={createTodo} className="add-todo">
          <FormControl fullWidth={true}>
            <TextField
              label="I will do this"
              variant="standard"
              onChange={handleChange}
              required={true}
              value={text}
            />
            <Button
              variant="contained"
              color="primary"
              style={{ marginTop: 5 }}
              type="submit"
            >
              <Add />
              Add
            </Button>
          </FormControl>
        </form>
      </Container>
    </div>
  );
};

export default AddTodo;
