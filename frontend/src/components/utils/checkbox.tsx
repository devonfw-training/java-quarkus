import { Check } from "lucide-react";

interface Props {
  state: boolean;
  onClick: () => void;
}

const Checkbox = (props: Props) => {
  return (
    <div
      className={`flex items-center justify-center h-6 w-6 border-2 border-light-primary rounded-md transition-all cursor-pointer ${
        props.state ? "border-primary bg-light-primary" : ""
      }`}
      onClick={() => props.onClick()}
    >
      <span
        className={`w-full h-full flex justify-center items-center transition-all ${
          props.state ? "" : "hidden"
        }`}
      >
        <Check className="text-primary w-4 h-4" />
      </span>
    </div>
  );
};

export default Checkbox;
