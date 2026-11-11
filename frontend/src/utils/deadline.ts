/**
 * Helpers for the deadline of an item. The deadline is a local date and time without time zone like "2026-11-22T14:30".
 *
 * The user may enter only a date. As the backend has only a date with time, such a deadline is marked by a time of
 * 00:00:00.001 (midnight plus one millisecond). A deadline at exactly midnight ("00:00:00") is a real time of 00:00.
 */
const DATE_ONLY_TIME = "00:00:00.001";

/** Tolerates trailing zeros, as the database may return more digits like "00:00:00.001000". */
const isDateOnly = (deadline: string): boolean =>
  /T00:00:00\.0010*$/.test(deadline);

/** Splits a deadline into the values for a date input and a time input (the time is empty for a date without time). */
export const splitDeadline = (
  deadline: string | null
): { date: string; time: string } => {
  if (!deadline) {
    return { date: "", time: "" };
  }
  return {
    date: deadline.substring(0, 10),
    time: isDateOnly(deadline) ? "" : deadline.substring(11, 16),
  };
};

/** Joins the values of a date input and a time input to a deadline, or null if there is no date. */
export const joinDeadline = (date: string, time: string): string | null =>
  date ? `${date}T${time || DATE_ONLY_TIME}` : null;

/** Formats a deadline for the user: only the date if it has no time, otherwise date and time. */
export const formatDeadline = (deadline: string): string => {
  const value = new Date(deadline);
  return isDateOnly(deadline)
    ? value.toLocaleDateString()
    : value.toLocaleString();
};
