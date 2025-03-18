import { GitHub, Group, YouTube } from "@material-ui/icons";
import { X } from "lucide-react";
import { forwardRef, RefObject, useContext } from "react";
import useOutsideClick from "../../hooks/outsideClick";
import { MainContext } from "../../provider/mainProvider";

interface Props {
  ignoreClick: RefObject<HTMLElement>[];
}

const About = forwardRef<HTMLDivElement, Props>((props, ref) => {
  const { showAbout, setShowAbout } = useContext(MainContext)!;

  useOutsideClick(
    [ref as RefObject<HTMLElement>, ...props.ignoreClick],
    () => setShowAbout(false),
    showAbout
  );

  return (
    <div
      ref={ref}
      className={`absolute z-100 w-screen bg-black opacity-95 flex flex-col justify-center items-center ${
        showAbout ? "" : "hidden"
      }`}
    >
      <X
        className="absolute top-10 right-10 text-white h-10 w-10 cursor-pointer"
        onClick={() => setShowAbout(false)}
      />
      <h1 className="text-white text-4xl font-bold">About</h1>
      <p className="text-white mt-6 text-xl max-w-96 text-center font-medium">
        <b>My Todo's</b> is a simple Todo App built using React.js and Quarkus
        as the backend.
      </p>
      <p className="text-white mt-12 font-medium text-xl">
        You can find devonfw here:
      </p>
      <div className="flex flex-row gap-5 mt-2.5">
        <a
          href="https://github.com/devonfw/"
          className="text-white text-2xl"
          target="_blank"
          rel="noopener noreferrer"
        >
          <GitHub fontSize="large" className="text-white" />
        </a>
        <a
          href="https://www.youtube.com/channel/UCtb1p-24jus-QoXy49t9Xzg"
          style={{
            color: "#FD0000",
            textDecoration: "none",
            fontSize: "24px",
          }}
          target="_blank"
          rel="noopener noreferrer"
        >
          <YouTube fontSize="large" />
        </a>
        <a
          href="https://teams.microsoft.com/l/team/19%3af92c481ec30345a28a5434bc530a882a%40thread.skype/conversations?groupId=503df57a-d454-4eec-b3bc-d6d87c7c24f8&tenantId=76a2ae5a-9f00-4f6b-95ed-5d33d77c4d61"
          style={{
            color: "#702ecd",
            textDecoration: "none",
            fontSize: "24px",
          }}
          target="_blank"
          rel="noopener noreferrer"
        >
          <Group fontSize="large" />
        </a>
      </div>
    </div>
  );
});

export default About;
