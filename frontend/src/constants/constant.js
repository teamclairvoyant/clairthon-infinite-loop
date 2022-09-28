export const URL_ENDPOINTS = {
  BASE_URL: "https://clairthon.herokuapp.com/api/v1",
  LOGIN: "auth/login",
  INTERVIEWER: "interviewer",
  INTERVIEWER_SEARCH: "interviewer/search",
  GET_INTERVIEWER_EVENTS: "calendar/events",
  GET_INTERVIEWER_FREE_SLOTS: "calendar/filter",
  CREATE_EVENT: "calendar/createEvent",
  GET_LISTING: "listing",
};

export const METHODS = {
  GET: "GET",
  POST: "POST",
};

export const RESPONSE_MESSAGE = {
  SUCCESS: "Success",
  ERROR: "Error",
};

export const LIST_NAMES = {
  SKILLS: "SKILLS",
  LOCATION: "LOCATION",
};

export const COPY = {
  INTERVIEWER_LIST: "Interviewer Lists",
  TOTAL_INTERVIEWER: "You have total {0} interviewers",
  UPDATE_INTERVIEWER: "Update Interviewer",
  CANCEL: "Cancel",
  NAME: "Name",
  EMAIL: "Email",
  PHONE: "Phone",
  EXPERIENCE: "Experience",
  EMPLOYEE_NO: "Employee No",
  SKILLS: "Skills",
  ADD_INTERVIEWER: "Add Interviewer",
  NO_DATA_FOUND: "No data found",
  EDIT: "Edit",
  REMOVE_INTERVIEWER: "Remove Interviewer",
  WELCOME_MESSAGE: "Welcome to Interview Slot Planner",
  FILTER_INTERVIEWERS: "Filter Interviewers",
  LOCATION: "Location",
  INTERVIEWER_ADDED: "Interviewer added Successfully !",
  INTERVIEWER_UPDATED: "Interviewer updated Successfully !",
  APPLY_FILTER: "Apply Filter",
  RESET_FILTER: "Reset Filter",
  EMAIL_ID_NOT_FOUND: "404 Not Found",
  SELECT_DATE: "Select Date",
  VIEW_INTERVIEWERS_CALENDAR: "Interviewers Calendar",
  EMAIL_ID_NOT_FOUND_MESSAGE: "Invalid Email Id, please check once !",
  SELECT_INTERVIEWER_MESSAGE: "Please select the interviewer",
  ADD_EVENT: "Add Event",
  TITLE: "Title",
  DESCRIPTION: "Description",
  ATTENDEES: "Attendees",
  SINGLE_DATE_ERROR: "single date is not allowed !",
  FROM_DATE_GREATER_ERROR:
    "start date can not be greater or equal than end date !",
  REQUIRED_FIELD_FILTER_ERROR: "Atleast one filter option is required !",
  REQUIRED_ERROR_MESSAGE: "This field is required",
  INVALID_EMAIL_ADDRESS: "invalid email address",
  REQUIRED_DATE_ERROR_MESSAGE: "Please, add all the date fields",
  INTERVIEW_SCHEDULE_SUCCESSFULLY: "Interview scheduled successfully !",
  START: "Start",
  END_TIME: "End Time",
  START_TIME: "Start Time",
  START_DATE: "Start Date",
  END_DATE: "End Date",
  DATE: "Date",
  PLACEHOLDER_NAME: "Enter Name",
  PLACEHOLDER_EMPLOYEE_NO: "Enter Employee No",
  PLACEHOLDER_EMAIL: "Enter Email",
  PLACEHOLDER_PHONE: "Enter Phone",
  PLACEHOLDER_LOCATION: "Enter Location",
  PLACEHOLDER_EXPERIENCE: "Enter Experience",
  PLACEHOLDER_SKILLS: "Enter Skills",
  EVENT_IDENTIFIER: "iplanner",
};

export const TEST_ID = {
  ADD_EVENT: {
    FORM: "addEventForm",
  },
  COMMON: {
    SUMBIT_BUTTON: "submitButton",
    ON_FORM_CANCEL_ANCHOR: "formCancelAnchor",
    ON_FORM_CANCEL: "formCancel",
  },
  PAGINATION: {
    NEXT_PAGE: "nextPage",
    PREV_PAGE: "prevPage",
    ITEM_PAGE: "itemPage",
  },
  // ADD_EVENT: {

  // }
};

export const TEST_CASES_CONSTANT_STRING = {
  COMMON_MODAL_TEXT: "Common Modal text is here !",
};
