import axios from "axios";
import { URL_ENDPOINTS } from "../constants/constant";

const client = axios.create({
  baseURL: URL_ENDPOINTS.BASE_URL,
});

export const request = ({ ...options }) => {
  const bearerToken = JSON.parse(localStorage.getItem("accessToken"));
  const user = JSON.parse(localStorage.getItem("user"));
  if (bearerToken) client.defaults.headers.common.Authorization = bearerToken;
  if (bearerToken) client.defaults.headers.common.TOKEN = bearerToken;
  if (user) client.defaults.headers.common.LOGIN_ID = user.login_user;

  const onSuccess = (success) => success;

  const onError = (error) => {
    return error;
  };

  return client(options).then(onSuccess).catch(onError);
};
