import dayjs from "dayjs";

export const mailIdOptions = [
  { value: "test@testgmail.com", label: "test@testgmail.com" },
  { value: "test1@testgmail.com", label: "test1@testgmail.com" },
];

export const dateOptions = {
  date: dayjs(),
  dateHourLater: dayjs().hour(1),
};
