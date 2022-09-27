import React, { useState, createContext } from "react";

export const UserContext = createContext();

export const UserContextProvider = (props) => {
  const [data, setData] = useState([]);

  return (
    <UserContext.Provider value={{ contextData: [data, setData] }}>
      {props.children}
    </UserContext.Provider>
  );
};
