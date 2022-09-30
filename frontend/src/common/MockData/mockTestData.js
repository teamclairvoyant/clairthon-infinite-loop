import dayjs from "dayjs";
import { filterLocation, filterSkills } from "../listing/ListingData";

export const mailIdOptions = [
  { value: "test@testgmail.com", label: "test@testgmail.com" },
  { value: "test1@testgmail.com", label: "test1@testgmail.com" },
];

export const dateOptions = {
  date: dayjs(),
  dateHourLater: dayjs().hour(1),
};

export const listing = {
  skillOptions: filterSkills,
  locationOptions: filterLocation,
};
