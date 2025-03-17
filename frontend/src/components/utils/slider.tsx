interface Props {
  state: boolean;
  onClick: () => void;
}

const Slider = (props: Props) => {
  return (
    <div
      className={`flex items-center p-1 px-0.5 h-6 w-12 border-2 border-light-primary rounded-full transition-all cursor-pointer ${
        props.state ? "border-primary bg-light-primary" : ""
      }`}
      onClick={() => props.onClick()}
    >
      <span
        className={`rounded-full bg-primary h-4 w-4 transition-all ${
          props.state ? "translate-x-6" : ""
        }`}
      ></span>
    </div>
  );
};

export default Slider;
