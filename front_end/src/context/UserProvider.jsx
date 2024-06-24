import { createContext, useContext, useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';

const headersWithToken = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem('dino_jwt')}`,
  },
});

const UserContext = createContext();

function UserProvider({ children }) {
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem('last_user_data');
    return storedUser ? JSON.parse(storedUser) : undefined;
  });

  async function setUserFullInfo() {
    const API_ENDPOINT = 'http://localhost:8080/api/v1/auth/me';

    try {
      const response = await axios.get(API_ENDPOINT, headersWithToken());
      setUser(response.data);
      localStorage.setItem('last_user_data', JSON.stringify(response.data));
    } catch (e) {
      // Did not pass the auth on the server => keep user undefined in the context of the React App
      setUser(undefined);
      localStorage.removeItem('last_user_data');
    }
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
      {children}
    </UserContext.Provider>
  );
}

function useUserContext() {
  return useContext(UserContext);
}

export { useUserContext, UserProvider, headersWithToken };
