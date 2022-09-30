import React, { useState, createContext } from 'react';
import PropTypes from 'prop-types';

export const UserContext = createContext();

export const UserContextProvider = (props) => {
  const [data, setData] = useState([]);

  return (
    <UserContext.Provider value={{ contextData: [data, setData] }}>
      {props.children}
    </UserContext.Provider>
  );
};

UserContextProvider.propTypes = {
  children: PropTypes.elementType
};
