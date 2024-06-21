import axios from 'axios';
import styles from './ReadMail.module.scss';
import { useNavigate, useParams } from 'react-router-dom';
import { headersWithToken } from '../../context/UserProvider';
import { useEffect, useState } from 'react';
import {
  formatDate,
  useResponseResult,
  reduceValidationErrors,
  handleBadRequest,
} from '../../helpers/helpers';
import { useNewMessagesContext } from '../../context/NewMessagesProvider';

async function getMail(id, setLetter, navigate, checkIfNewMessages) {
  const API_ENDPOINT = `http://localhost:8080/api/v1/mail/${id}`;

  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    setLetter(response.data);
    checkIfNewMessages();
  } catch (e) {
    navigate('/no-letter-with-such-id');
  }
}

async function deleteMail(id, navigate, resultDispatch) {
  const API_ENDPOINT = `http://localhost:8080/api/v1/mail/${id}`;

  try {
    const response = await axios.delete(API_ENDPOINT, headersWithToken());
    resultDispatch({
      type: 'SUCCESS',
      payload: {
        heading: 'Letter deleted',
        message:
          response.data.message +
          '. You will be re-addressed to all mail shortly...',
      },
    });
    setTimeout(() => {
      navigate('/in/mail/all');
    }, 2000);
  } catch (e) {
    console.log('💥💥💥' + e);
    handleBadRequest(e, resultDispatch);
  }
}

function ReadMail() {
  const navigate = useNavigate();
  const params = useParams();
  const mailId = +params.id;
  const [letter, setLetter] = useState('');
  const { checkIfNewMessages } = useNewMessagesContext();
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);

  useEffect(() => {
    getMail(mailId, setLetter, navigate, checkIfNewMessages);
  }, [mailId]);

  return (
    <>
      {(error.status || success.status) && forDisplay && (
        <div
          className={`message-container ${
            error.status ? 'error-color' : 'success-color'
          }`}
        >
          <p
            className="message-container__close-btn"
            onClick={() => resultDispatch({ type: 'CLOSE' })}
          >
            X
          </p>
          <h2 className="message-container__heading">
            {error.status ? error.heading : success.heading}
          </h2>
          <p className="message-container__content">
            {error.status ? error.message : success.message}
          </p>
          <ul>
            {errors?.map((e, i) => (
              <li className="message-container__content" key={i}>
                {e}
              </li>
            ))}
          </ul>
        </div>
      )}
      <div className={styles.message}>
        <h2 className="heading">Read message</h2>
        <div className={styles.message__title}>
          <h3 className={styles.message__title_heading}>Title: </h3>
          <p className={styles.message__title_content}>{letter?.title}</p>
        </div>
        <div className={styles.message__title}>
          <h3 className={styles.message__title_heading}>From: </h3>
          <p className={styles.message__title_content}>{letter?.from}</p>
        </div>
        <div className={styles.message__title}>
          <h3 className={styles.message__title_heading}>Received on: </h3>
          <p className={styles.message__title_content}>
            {formatDate(letter?.sentAt)}
          </p>
        </div>
        <p className={styles.message__text}>{letter?.text}</p>
        <div className={styles.btns}>
          <button className="btn brown-btn">Reply</button>
          <button
            className="btn brown-btn--reversed"
            onClick={() => {
              deleteMail(mailId, navigate, resultDispatch);
            }}
          >
            Delete
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

export default ReadMail;
