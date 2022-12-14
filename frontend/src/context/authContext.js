import { createContext, useContext, useMemo } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';
import PropTypes from 'prop-types';
const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useLocalStorage('user', null);
  const [accessToken, setAccessToken] = useLocalStorage('accessToken', null);

  // call this function when you want to authenticate the user
  const login = async ({ data, history }) => {
    setUser(data);
    history.push('/interview-list');
  };

  const storeToken = async (data) => {
    setAccessToken(data);
  };

  // call this function to sign out logged in user
  const logout = (history) => {
    setUser(null);
    setAccessToken(null);

    history.push('/auth-login');
  };

  const value = useMemo(
    () => ({
      user,
      login,
      logout,
      storeToken,
      accessToken
    }),
    [user, accessToken]
  );
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  return useContext(AuthContext);
};

AuthProvider.propTypes = {
  children: PropTypes.any
};
