import { Check } from "lucide-react";
import { Dispatch, RefObject, SetStateAction, useRef } from "react";
import useOutsideClick from "../../hooks/outsideClick";

interface FilterMenuI {
  ignoreClick: RefObject<HTMLElement>[];
  showFilter: boolean;
  setShowFilter: Dispatch<SetStateAction<boolean>>;
  selectedFilter: SelectedFilterE;
  setSelectedFilter: Dispatch<SetStateAction<SelectedFilterE>>;
}

export enum SelectedFilterE {
  NONE,
  WITH_DUE_DATE,
  WITHOUT_DUE_DATE,
  ONLY_STARRED,
  NOT_STARRED,
}

export const FilterMenu = ({
  ignoreClick,
  showFilter,
  setShowFilter,
  selectedFilter,
  setSelectedFilter,
}: FilterMenuI) => {
  const filterMenuRef = useRef(null);
  useOutsideClick(
    [filterMenuRef, ...ignoreClick],
    () => setShowFilter(false),
    showFilter
  );

  return (
    <div
      ref={filterMenuRef}
      className={`absolute top-8 right-8 z-20 p-3 flex flex-col gap-2.5 opacity-90 dark:bg-light-black bg-white border-primary rounded-2xl border-2 ${
        showFilter ? "" : "hidden"
      }`}
    >
      <FilterItem
        text="With due date"
        isSelected={selectedFilter === SelectedFilterE.WITH_DUE_DATE}
        onClick={() =>
          setSelectedFilter(
            selectedFilter === SelectedFilterE.WITH_DUE_DATE
              ? SelectedFilterE.NONE
              : SelectedFilterE.WITH_DUE_DATE
          )
        }
      />
      <FilterItem
        text="Without due date"
        isSelected={selectedFilter === SelectedFilterE.WITHOUT_DUE_DATE}
        onClick={() =>
          setSelectedFilter(
            selectedFilter === SelectedFilterE.WITHOUT_DUE_DATE
              ? SelectedFilterE.NONE
              : SelectedFilterE.WITHOUT_DUE_DATE
          )
        }
      />
      <FilterItem
        text="Only starred"
        isSelected={selectedFilter === SelectedFilterE.ONLY_STARRED}
        onClick={() =>
          setSelectedFilter(
            selectedFilter === SelectedFilterE.ONLY_STARRED
              ? SelectedFilterE.NONE
              : SelectedFilterE.ONLY_STARRED
          )
        }
      />
      <FilterItem
        text="Not starred"
        isSelected={selectedFilter === SelectedFilterE.NOT_STARRED}
        onClick={() =>
          setSelectedFilter(
            selectedFilter === SelectedFilterE.NOT_STARRED
              ? SelectedFilterE.NONE
              : SelectedFilterE.NOT_STARRED
          )
        }
      />
    </div>
  );
};

const FilterItem = ({
  text,
  isSelected,
  onClick,
}: {
  text: string;
  isSelected: boolean;
  onClick: () => void;
}) => {
  return (
    <div
      className="flex justify-between items-center gap-16 cursor-pointer"
      onClick={onClick}
    >
      <p className="dark:text-white text-black">{text}</p>
      {isSelected ? <Check className="dark:text-white text-black" /> : null}
    </div>
  );
};
