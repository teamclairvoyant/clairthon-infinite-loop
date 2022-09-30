/* eslint-disable react/prop-types */
import { Redirect } from 'react-router-dom';

//url for production
export var url = '';
if (process.env.NODE_ENV === 'development') {
  url = '';
} else {
  url = window.location.host.split('/')[1];
  if (url) {
    url = `/${window.location.host.split('/')[1]}`;
  } else url = process.env.PUBLIC_URL; /// ADD YOUR CPANEL SUB-URL
}

//Function to validate and return errors for a form
export const checkForm = (formData) => {
  let errorState = {};
  Object.keys(formData).forEach((item) => {
    if (formData[item] === null || formData[item] === '') {
      errorState[item] = 'This field is required';
    }
  });
  return errorState;
};

export const mapOptions = (optionValue, type) => {
  return optionValue.map((item) => ({ value: item, label: item, type: type }));
};

//Function that returns the first or first two letters from a name
export const findUpper = (string) => {
  let extractedString = [];

  for (var i = 0; i < string.length; i++) {
    if (string.charAt(i) === string.charAt(i).toUpperCase() && string.charAt(i) !== ' ') {
      extractedString.push(string.charAt(i));
    }
  }
  if (extractedString.length > 1) {
    return extractedString[0] + extractedString[1];
  } else {
    return extractedString[0];
  }
};

//Function that calculates the from current date
export const setDeadline = (days) => {
  let todayDate = new Date();
  var newDate = new Date(todayDate);
  newDate.setDate(newDate.getDate() + days);
  return newDate;
};

// Function to structure date ex : Jun 4, 2011;
export const getDateStructured = (date) => {
  let d = date.getDate();
  let m = date.getMonth();
  let y = date.getFullYear();
  let final = monthNames[m] + ' ' + d + ', ' + y;
  return final;
};

// Function to structure date ex: YYYY-MM-DD
export const setDateForPicker = (rdate) => {
  let d = rdate.getDate();
  d < 10 && (d = '0' + d);
  let m = rdate.getMonth() + 1;
  m < 10 && (m = '0' + m);
  let y = rdate.getFullYear();

  rdate = y + '-' + m + '-' + d;
  return rdate;
};

// Function to structure date ex: YYYY-MM-DD
export const getDate = (rdate) => {
  if (!rdate) return null;
  let d = rdate.getDate();
  d < 10 && (d = '0' + d);
  let m = rdate.getMonth() + 1;
  m < 10 && (m = '0' + m);
  let y = rdate.getFullYear();

  rdate = y + '-' + m + '-' + d;
  return rdate;
};

export const getDateNTime = (date, time) => {
  if (!date || !time) return null;
  return getDate(date) + getTime(time);
};

// Function to structure time ex: YYYY-MM-DD
export const getTime = (rdate) => {
  if (!rdate) return 'T00:00:00+5:30';
  let h = rdate.getHours();
  h < 10 && (h = '0' + h);
  let m = rdate.getMinutes();
  m < 10 && (m = '0' + m);
  rdate = 'T' + h + ':' + m + ':00+05:30';
  return rdate;
};

// Set deadlines for projects
export const setDeadlineDays = (deadline) => {
  var currentDate = new Date();
  var difference = deadline.getTime() - currentDate.getTime();
  var days = Math.ceil(difference / (1000 * 3600 * 24));
  return days;
};

//Date formatter function Example : 10-02-2004
export const dateFormatterAlt = (date, reverse) => {
  let d = date.getDate();
  let m = date.getMonth();
  let y = date.getFullYear();
  reverse ? (date = m + '-' + d + '-' + y) : (date = y + '-' + d + '-' + m);
  return date;
};

//Date formatter function
export const dateFormatter = (date, reverse) => {
  var dateformat = date.split('-');
  //var date = dateformat[1]+"-"+dateformat[2]+"-"+dateformat[0];
  reverse
    ? (date = dateformat[2] + '-' + dateformat[0] + '-' + dateformat[1])
    : (date = dateformat[1] + '-' + dateformat[2] + '-' + dateformat[0]);

  return date;
};

//todays Date
export const todaysDate = new Date();

//current Time
export const currentTime = () => {
  var hours = todaysDate.getHours();
  var minutes = todaysDate.getMinutes();
  var ampm = hours >= 12 ? 'PM' : 'AM';
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? '0' + minutes : minutes;
  var strTime = hours + ':' + minutes + ' ' + ampm;
  return strTime;
};

//Percentage calculation
export const calcPercentage = (str1, str2) => {
  let result = Number(str2) / Number(str1);
  result = result * 100;
  return Math.floor(result);
};

export const truncate = (str, n) => {
  return str.length > n
    ? str.substr(0, n - 1) + ' ' + truncate(str.substr(n - 1, str.length), n)
    : str;
};

export const RedirectAs404 = ({ location }) => (
  <Redirect to={Object.assign({}, location, { state: { is404: true } })} />
);

// returns upload url
export const getUploadParams = () => {
  return { url: 'https://httpbin.org/post' };
};

export const bulkActionOptions = [
  { value: 'slot', label: 'Search Slot' },
  { value: 'calendar', label: 'View Calendar' },
  { value: 'remove', label: 'Remove Interviewer' }
];

// Converts KB to MB
export const bytesToMegaBytes = (bytes) => {
  let result = bytes / (1024 * 1024);
  return result.toFixed(2);
};

export const monthNames = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December'
];
