import { SortAsc, SortDesc } from "lucide-react";
import { Dispatch, RefObject, SetStateAction, useRef } from "react";
import useOutsideClick from "../../hooks/outsideClick";

interface SortMenuI {
  ignoreClick: RefObject<HTMLElement>[];
  showSort: boolean;
  setShowSort: Dispatch<SetStateAction<boolean>>;
  selectedSort: {
    selectedSort: SelectedSortE;
    selectedSortOrder: SelectedSortOrderE;
  };
  setSelectedSort: Dispatch<
    SetStateAction<{
      selectedSort: SelectedSortE;
      selectedSortOrder: SelectedSortOrderE;
    }>
  >;
}

export enum SelectedSortE {
  NONE,
  BY_CREATION_DATE,
  ALPHABETICALLY,
  BY_DUE_DATE,
  STARRED,
}

export enum SelectedSortOrderE {
  NOT_SELECTED,
  ASC,
  DESC,
}

export const SortMenu = ({
  ignoreClick,
  showSort,
  setShowSort,
  selectedSort,
  setSelectedSort,
}: SortMenuI) => {
  const sortMenuRef = useRef(null);
  useOutsideClick(
    [sortMenuRef, ...ignoreClick],
    () => setShowSort(false),
    showSort
  );

  const onClick = (clickedSort: SelectedSortE) => {
    if (
      selectedSort.selectedSortOrder === SelectedSortOrderE.NOT_SELECTED ||
      selectedSort.selectedSort !== clickedSort
    ) {
      setSelectedSort({
        selectedSort: clickedSort,
        selectedSortOrder: SelectedSortOrderE.ASC,
      });
    } else if (selectedSort.selectedSortOrder === SelectedSortOrderE.ASC) {
      setSelectedSort({
        selectedSort: clickedSort,
        selectedSortOrder: SelectedSortOrderE.DESC,
      });
    } else {
      setSelectedSort({
        selectedSort: SelectedSortE.NONE,
        selectedSortOrder: SelectedSortOrderE.NOT_SELECTED,
      });
    }
  };

  return (
    <div
      ref={sortMenuRef}
      className={`absolute top-8 right-0 z-20 p-3 flex flex-col gap-2.5 opacity-90 dark:bg-light-black bg-white border-primary rounded-2xl border-2 ${
        showSort ? "" : "hidden"
      }`}
    >
      <SortItem
        text="By creation date"
        isSelected={
          selectedSort.selectedSort === SelectedSortE.BY_CREATION_DATE
        }
        selectedOrder={selectedSort.selectedSortOrder}
        onClick={() => onClick(SelectedSortE.BY_CREATION_DATE)}
      />
      <SortItem
        text="Alphabetically"
        isSelected={selectedSort.selectedSort === SelectedSortE.ALPHABETICALLY}
        selectedOrder={selectedSort.selectedSortOrder}
        onClick={() => onClick(SelectedSortE.ALPHABETICALLY)}
      />
      <SortItem
        text="By due date"
        isSelected={selectedSort.selectedSort === SelectedSortE.BY_DUE_DATE}
        selectedOrder={selectedSort.selectedSortOrder}
        onClick={() => onClick(SelectedSortE.BY_DUE_DATE)}
      />
      <SortItem
        text="Starred"
        isSelected={selectedSort.selectedSort === SelectedSortE.STARRED}
        selectedOrder={selectedSort.selectedSortOrder}
        onClick={() => onClick(SelectedSortE.STARRED)}
      />
    </div>
  );
};

const SortItem = ({
  text,
  isSelected,
  selectedOrder,
  onClick,
}: {
  text: string;
  isSelected: boolean;
  selectedOrder: SelectedSortOrderE;
  onClick: () => void;
}) => {
  return (
    <div
      className="flex justify-between items-center gap-16 cursor-pointer"
      onClick={onClick}
    >
      <p className="dark:text-white text-black">{text}</p>
      {isSelected ? (
        selectedOrder === SelectedSortOrderE.ASC ? (
          <SortAsc className="dark:text-white text-black" />
        ) : selectedOrder === SelectedSortOrderE.DESC ? (
          <SortDesc className="dark:text-white text-black" />
        ) : null
      ) : null}
    </div>
  );
};
