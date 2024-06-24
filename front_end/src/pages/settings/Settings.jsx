import { useContext, useEffect, useState } from 'react';
import { useUserContext, headersWithToken } from '../../context/UserProvider';
import { useNewMessagesContext } from '../../context/NewMessagesProvider';
import styles from './Settings.module.scss';
import axios from 'axios';
import {
  useResponseResult,
  reduceValidationErrors,
} from '../../helpers/helpers';
import { useNavigate, useLocation } from 'react-router-dom';
import NotificationMessage from '../notificationMessage/NotificationMessage';

const BASE_URL = 'http://localhost:8080/api/v1/settings';

async function change(
  path,
  body,
  resultDispatch,
  setUserFullInfo,
  setIsLoading,
  toLogout
) {
  // path is either username, description, email, password
  if (path === 'password' && body.confirmPassword !== body.newPassword) {
    resultDispatch({
      type: 'ERROR',
      payload: {
        heading: 'Invalid user input',
        message: 'Password and password confirm do not match!',
        type: '',
        errors: [],
      },
    });
    setTimeout(() => {
      resultDispatch({ type: 'CLOSE' });
    }, 5000);
    return;
  }

  const API_ENDPOINT = `${BASE_URL}/${path}`;
  try {
    setIsLoading(true);
    resultDispatch({ type: 'IS_LOADING' });
    const response = await axios.post(API_ENDPOINT, body, headersWithToken());
    resultDispatch({
      type: 'SUCCESS',
      payload: { heading: 'Success', message: response.data.message },
    });
    if (toLogout) {
      setTimeout(() => {
        toLogout();
      }, 3000);
    } else {
      setUserFullInfo();
      setTimeout(() => {
        resultDispatch({ type: 'CLOSE' });
      }, 5000);
    }
  } catch (e) {
    if (e.code === 'ERR_BAD_REQUEST') {
      const error = e.response.data;
      resultDispatch({
        type: 'ERROR',
        payload: {
          heading: error.name,
          message: error.message,
          type: error.type,
          errors: error.errors,
        },
      });
    } else if (e.code === 'ERR_NETWORK') {
      resultDispatch({
        type: 'ERROR',
        payload: {
          heading: 'Service is currently unavailable',
          message:
            'Registration is currently unavailable! Please,try again later!',
          type: 'ERR_NETWORK',
          errors: [],
        },
      });
    }
  } finally {
    setIsLoading(false);
  }
}

function Settings() {
  const { user, logoutUser } = useUserContext();
  const [description, setDescription] = useState('');
  const [descriptionIsLoading, setDescriptionIsLoading] = useState(false);

  const [email, setEmail] = useState('');
  const [emailIsLoading, setEmailIsLoading] = useState(false);

  const [username, setUsername] = useState('');
  const [usernameIsLoading, setUsernameIsLoading] = useState(false);

  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordIsLoading, setPasswordIsLoading] = useState(false);

  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);

  const { setUserFullInfo } = useUserContext();
  const location = useLocation();

  const navigate = useNavigate();
  function toLogout() {
    logoutUser();
    navigate('/login');
  }

  const { checkIfNewMessages } = useNewMessagesContext();
  useEffect(() => {
    checkIfNewMessages();
  });

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        forDisplay={forDisplay}
        errors={errors}
        resultDispatch={resultDispatch}
      />
      <div className={styles.container}>
        <div className={styles.descriptionSettings}>
          <h2 className={styles.heading}>Change user description</h2>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              change(
                'description',
                { description },
                resultDispatch,
                setUserFullInfo,
                setDescriptionIsLoading,
                false
              );
            }}
          >
            <textarea
              className={`${styles.textarea} input`}
              name="user-description"
              id="user-description"
              rows="5"
              cols="33"
              placeholder={
                user.description
                  ? user.description
                  : 'Currently, your user info is empty..'
              }
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            ></textarea>
            <button className="btn brown-btn">Change description</button>
          </form>
        </div>
        <div className={styles.emailSettings}>
          <h2 className={styles.heading}>Change email</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'email',
                { email },
                resultDispatch,
                setUserFullInfo,
                setEmailIsLoading,
                false
              );
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="New email"
              onChange={(e) => setEmail(e.target.value)}
              value={email}
            />
            <button className="btn brown-btn">Change email</button>
          </form>
        </div>
        <div className={styles.usernameSettings}>
          <h2 className={styles.heading}>Change username</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'username',
                { username },
                resultDispatch,
                setUserFullInfo,
                setUsernameIsLoading,
                toLogout
              );
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="New username"
              onChange={(e) => setUsername(e.target.value)}
              value={username}
            />
            <button className="btn brown-btn">Change username</button>
          </form>
        </div>
        <div className={styles.passwordSettings}>
          <h2 className={styles.heading}>Change password</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'password',
                { newPassword, oldPassword, confirmPassword },
                resultDispatch,
                setUserFullInfo,
                setPasswordIsLoading,
                toLogout
              );
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="Old password"
              onChange={(e) => setOldPassword(e.target.value)}
              value={oldPassword}
            />
            <input
              type="text"
              className="input"
              placeholder="New password"
              onChange={(e) => setNewPassword(e.target.value)}
              value={newPassword}
            />
            <input
              type="text"
              className="input"
              placeholder="Confirm new password"
              onChange={(e) => setConfirmPassword(e.target.value)}
              value={confirmPassword}
            />
            <button className="btn brown-btn">Change pasword</button>
          </form>
        </div>
      </div>
    </>
  );
}

export default Settings;
