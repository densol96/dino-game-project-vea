import styles from './WriteMail.module.scss';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import {
  BASE_API_URL,
  handleBadRequest,
  reduceValidationErrors,
  useResponseResult,
} from '../../helpers/helpers';
import axios from 'axios';
import { headersWithToken } from '../../context/UserProvider';
import NotificationMessage from '../notificationMessage/NotificationMessage';

async function sendMail(body, resultDispatch) {
  resultDispatch({ type: 'IS_LOADING' });
  const API_ENDPOINT = `${BASE_API_URL}/mail/send`;
  try {
    const response = await axios.post(API_ENDPOINT, body, headersWithToken());
    resultDispatch({
      type: 'SUCCESS',
      payload: {
        heading: 'Your mail has been successfully sent',
        message: response.data.message,
      },
    });
  } catch (e) {
    console.log(e);
    handleBadRequest(e, resultDispatch);
  }
}

function WriteMail() {
  const navigate = useNavigate();
  const [to, setTo] = useState('');
  const [title, setTitle] = useState('');
  const [messageText, setMessageText] = useState('');
  const [
    { success, error, forDisplay, isLoading, buttonClickable },
    resultDispatch,
  ] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);
  const body = { to, title, messageText };

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        resultDispatch={resultDispatch}
        forDisplay={forDisplay}
        errors={errors}
      />
      <div className={styles.message}>
        <h2 className="heading">Write message</h2>
        <div className={styles.inputUnit}>
          <label htmlFor="to">To:</label>
          <input
            id="to"
            className={`input ${styles.toInput}`}
            type="text"
            onChange={(e) => setTo(e.target.value)}
          />
        </div>
        <div className={styles.inputUnit}>
          <label htmlFor="to">Title:</label>
          <input
            className={`input ${styles.titleInput}`}
            type="text"
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div className={styles.messageUnit}>
          <label htmlFor="to">Message:</label>
          <textarea
            className={`input ${styles.textInput}`}
            onChange={(e) => setMessageText(e.target.value)}
          />
        </div>

        <div className={styles.btns}>
          <button
            className="btn brown-btn"
            onClick={() => sendMail(body, resultDispatch)}
          >
            Send
          </button>
          <button
            className="btn brown-btn--reversed"
            onClick={() => navigate(-1)}
          >
            Go back
          </button>
        </div>
      </div>
    </>
  );
}

export default WriteMail;
