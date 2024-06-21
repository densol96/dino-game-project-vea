import { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { headersWithToken } from './UserProvider';

const NotificationsContext = createContext();
const API_ENDPOINT = 'http://localhost:8080/api/v1/mail/has-new-messages';

function NewMessagesProvider({ children }) {
  const [hasNewMessages, setHasNewMessages] = useState(false);

  async function checkIfNewMessages() {
    try {
      const response = await axios.get(API_ENDPOINT, headersWithToken());
      setHasNewMessages(response.data.hasNewMessages);
    } catch (e) {
      /*
       * If user unauthorised and JWT not getting validated simply do nothing:
       *
       */
    }
  }

  useEffect(() => {
    checkIfNewMessages();
  }, []);

  return (
    <NotificationsContext.Provider
      value={{ hasNewMessages, checkIfNewMessages }}
    >
      {children}
    </NotificationsContext.Provider>
  );
}

function useNewMessagesContext() {
  return useContext(NotificationsContext);
}

export { NewMessagesProvider, useNewMessagesContext };
