import { createContext, useContext, useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode';

const UserContext = createContext();

function UserProvider({ children }) {
  const [user, setUser] = useState(() => {
    const jwt = localStorage.getItem('dino_jwt');
    if (jwt) {
      const { sub } = jwtDecode(jwt);
      return sub;
    }
    return undefined;
  });

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}

function useUserContext() {
  return useContext(UserContext);
}

export { useUserContext, UserProvider };
