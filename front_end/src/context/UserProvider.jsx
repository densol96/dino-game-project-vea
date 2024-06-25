import { createContext, useContext, useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
import LoadingSpinner from '../components/Spinner/LoadingSpinner';
import { sortedUniq } from 'lodash';

const headersWithToken = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem('dino_jwt')}`,
  },
});

const UserContext = createContext();

function UserProvider({ children }) {
  const [user, setUser] = useState('');
  const [isGettingInit, setIsGettingInit] = useState(true);

  async function setUserFullInfo() {
    const API_ENDPOINT = 'http://localhost:8080/api/v1/auth/me';

    try {
      const response = await axios.get(API_ENDPOINT, headersWithToken());
      setUser(response.data);
    } catch (e) {
      console.log(e);
      // Did not pass the auth on the server => keep user undefined in the context of the React App
      setUser(undefined);
    }
    setIsGettingInit(false);
  }

  function logoutUser() {
    localStorage.removeItem('dino_jwt');
    setUser(undefined);
  }

  useEffect(() => {
    setUserFullInfo();
  }, []);

  return (
    <UserContext.Provider value={{ user, setUserFullInfo, logoutUser }}>
      {isGettingInit && <LoadingSpinner width={48} height={48} />}
      {!isGettingInit && children}
    </UserContext.Provider>
  );
}

function useUserContext() {
  return useContext(UserContext);
}

export { useUserContext, UserProvider, headersWithToken };
