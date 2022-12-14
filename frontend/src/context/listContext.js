/* eslint-disable react/prop-types */
import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { errorToast } from '../components/toastr/Tostr';
import {
  CATCH_MESSAGE,
  LIST_NAMES,
  METHODS,
  RESPONSE_MESSAGE,
  URL_ENDPOINTS
} from '../constants/constant';
import { request } from '../utils/axiosUtils';
import { mapOptions } from '../utils/Utils';
export const ListContext = createContext(null);

export const ListProvider = ({ children }) => {
  const [locationOptions, setLocationOptions] = useState([]);
  const [skillOptions, setSkillOptions] = useState([]);

  useEffect(() => {
    const requestOptions = {
      url: URL_ENDPOINTS.GET_LISTING,
      method: METHODS.GET
    };

    const responseData = request(requestOptions);

    responseData
      .then((response) => {
        if (response.data.STATUS === RESPONSE_MESSAGE.SUCCESS) {
          response.data.DATA.forEach((lists) => {
            if (lists.type === LIST_NAMES.SKILLS) {
              setSkillOptions(mapOptions(lists.items, lists.type));
            }
            if (lists.type === LIST_NAMES.LOCATION) {
              setLocationOptions(mapOptions(lists.items, lists.type));
            }
          });
        } else {
          errorToast(CATCH_MESSAGE.ERROR);
        }
      })
      .catch((err) => {
        err && errorToast(CATCH_MESSAGE.ERROR);
      });
  }, []);
  const listing = useMemo(
    () => ({
      skillOptions,
      locationOptions
    }),
    [skillOptions, locationOptions]
  );

  return <ListContext.Provider value={listing}>{children}</ListContext.Provider>;
};

export const useListContext = () => {
  return useContext(ListContext);
};
