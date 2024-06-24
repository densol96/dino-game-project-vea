import styles from './AdminManage.module.scss';
import NotificationMessage from '../notificationMessage/NotificationMessage';
import {
  useResponseResult,
  reduceValidationErrors,
  BASE_API_URL,
  handleBadRequest,
} from '../../helpers/helpers';
import { useEffect, useState, useRef, useReducer } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import { headersWithToken } from '../../context/UserProvider';

async function getUserDetailedInfo(id, resultDispatch, userDispatch, navigate) {
  resultDispatch({ type: 'IS_LOADING' });
  const API_ENDPOINT = `${BASE_API_URL}/users/detailed-user-info/${id}`;
  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    userDispatch({ type: 'INIT', payload: response.data });
  } catch (e) {
    handleBadRequest(e, resultDispatch);
    setTimeout(() => {
      navigate('/in/profile');
    }, 2000);
  }
}

async function change() {}

function userReducer(state, action) {
  switch (action.type) {
    case 'accountDisabled':
      return { ...state, accountDisabled: action.payload };
    case 'description':
      return { ...state, description: action.payload };
    case 'email':
      return { ...state, email: action.payload };
    case 'isEmailConfirmed':
      return { ...state, isEmailConfirmed: action.payload };
    case 'lastLoggedIn':
      return { ...state, lastLoggedIn: action.payload };
    case 'registrationDate':
      return { ...state, registrationDate: action.payload };
    case 'tempBanDateTime':
      return { ...state, tempBanDateTime: action.payload };
    case 'username':
      return { ...state, username: action.payload };
    case 'password':
      return { ...state, password: action.payload };
    case 'confirmPassword':
      return { ...state, confirmPassword: action.payload };
    case 'INIT':
      return { ...action.payload, password: '', confirmPassword: '' };
    default:
      throw new Error('💥💥💥 Check your userReducer in AdminManahe.jsx');
  }
}

const userInitialState = {
  accountDisabled: false,
  description: null,
  email: '',
  isEmailConfirmed: false,
  lastLoggedIn: null,
  registrationDate: '',
  tempBanDateTime: null,
  username: '',
  password: '',
  confirmPassword: '',
};

function AdminManage() {
  const [
    { success, error, forDisplay, isLoading, buttonClickable },
    resultDispatch,
  ] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);
  const clickedButton = useRef(undefined);

  const { id } = useParams();

  const [
    {
      accountDisabled,
      description,
      email,
      isEmailConfirmed,
      lastLoggedIn,
      registrationDate,
      tempBanDateTime,
      username,
      password,
      confirmPassword,
    },
    userDispatch,
  ] = useReducer(userReducer, userInitialState);
  const navigate = useNavigate();

  useEffect(() => {
    getUserDetailedInfo(id, resultDispatch, userDispatch, navigate);
  }, []);

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
        <h2 className={styles.mainHeading}>Manage the user: {username}</h2>
        <div className={styles.general}>
          <p>
            <span className={styles.general__Title}>Registration date:</span>
            <span>{new Date(registrationDate).toLocaleDateString()}</span>
          </p>
          <p>
            <span>Last logged in:</span>
            <span>
              {`${new Date(lastLoggedIn).toLocaleTimeString()} ${new Date(
                lastLoggedIn
              ).toLocaleDateString()}` || 'Has not logged in yet'}
            </span>
          </p>
        </div>
        <div className={styles.descriptionSettings}>
          <h2 className={styles.heading}>Change user description</h2>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              change('description', { description }, resultDispatch, false);
            }}
          >
            <textarea
              className={`${styles.textarea} input`}
              name="user-description"
              id="user-description"
              rows="5"
              cols="33"
              placeholder={
                !description && 'Currently, your user info is empty..'
              }
              value={description}
              onChange={(e) =>
                userDispatch({ type: 'description', payload: e.target.value })
              }
            ></textarea>
            <button className="btn brown-btn">Change description</button>
          </form>
        </div>
        <div className={styles.emailSettings}>
          <h2 className={styles.heading}>Change email</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change('email', { email }, resultDispatch, false);
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="New email"
              onChange={(e) =>
                userDispatch({ type: 'email', payload: e.target.value })
              }
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
              await change('username', { username }, resultDispatch);
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="New username"
              onChange={(e) =>
                userDispatch({ type: 'username', payload: e.target.value })
              }
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
                { password, confirmPassword },
                resultDispatch
              );
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="New password"
              onChange={(e) =>
                userDispatch({ type: 'password', payload: e.target.value })
              }
              value={password}
            />
            <input
              type="text"
              className="input"
              placeholder="Confirm new password"
              onChange={(e) =>
                userDispatch({
                  type: 'confirmPassword',
                  payload: e.target.value,
                })
              }
              value={confirmPassword}
            />
            <button className="btn brown-btn">Change pasword</button>
          </form>
        </div>
        <div className={styles.setting}>
          <h2 className={styles.heading}>Is account disabled?</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'accountDisabled',
                { accountDisabled },
                resultDispatch
              );
            }}
          >
            <input
              className={styles.checkbox}
              type="checkbox"
              checked={accountDisabled}
              onChange={(e) =>
                userDispatch({
                  type: 'accountDisabled',
                  payload: e.target.checked,
                })
              }
            />
            <button className="btn brown-btn">Change status</button>
          </form>
        </div>
        <div className={styles.setting}>
          <h2 className={styles.heading}>Is email confirmed?</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'isEmailConfirmed',
                { isEmailConfirmed },
                resultDispatch
              );
            }}
          >
            <input
              className={styles.checkbox}
              type="checkbox"
              checked={isEmailConfirmed}
              onChange={(e) =>
                userDispatch({
                  type: 'isEmailConfirmed',
                  payload: e.target.checked,
                })
              }
            />
            <button className="btn brown-btn">Change status</button>
          </form>
        </div>
        <div className={styles.setting}>
          <h2 className={styles.heading}>Temporary ban:</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'tempBanDateTime',
                { tempBanDateTime },
                resultDispatch
              );
            }}
          >
            <input
              className={styles.tempBan}
              type="datetime-local"
              min={new Date().toISOString().slice(0, 16)}
              value={tempBanDateTime}
              onChange={(e) =>
                userDispatch({
                  type: 'tempBanDateTime',
                  payload: e.target.value,
                })
              }
            />
            <button className="btn brown-btn">Change temporary ban</button>
          </form>
        </div>
      </div>
    </>
  );
}

export default AdminManage;
