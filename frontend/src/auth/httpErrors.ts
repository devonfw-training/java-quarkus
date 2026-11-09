/**
 * Builds the text for the error alert of a failed backend request.
 * The backend answers with HTTP 403 if the logged in user lacks the permission for the requested action.
 * In that case the user is told so, instead of a generic error.
 */
export const errorAlertMessage = (status: number, message: string): string =>
  status === 403 ? `${message.replace(/!$/, "")} (No permissions)!` : message;
