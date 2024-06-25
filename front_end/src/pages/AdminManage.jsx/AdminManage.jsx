import styles from './AdminManage.module.scss';
import NotificationMessage from '../notificationMessage/NotificationMessage';
import {
  useResponseResult,
  reduceValidationErrors,
  BASE_API_URL,
  handleBadRequest,
} from '../../helpers/helpers';
import { useEffect, useState, useRef, useReducer } from 'react';
import { Navigate, useNavigate, useParams, NavLink } from 'react-router-dom';
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

async function change(fieldName, id, body, resultDispatch) {
  resultDispatch({ type: 'IS_LOADING' });
  if (fieldName === 'password') {
    if (body.password !== body.confirmPassword)
      return resultDispatch({
        type: 'ERROR',
        payload: { heading: 'Password and confirm-password must match' },
      });
    body = { password: body.password };
  }
  console.log(body);
  const API_ENDPOINT = `${BASE_API_URL}/users/change/${fieldName}/${id}`;
  console.log(API_ENDPOINT);
  try {
    const response = await axios.patch(API_ENDPOINT, body, headersWithToken());
    console.log(response.data);
    resultDispatch({
      type: 'SUCCESS',
      payload: { heading: 'User data changed', message: response.data.message },
    });
  } catch (e) {
    handleBadRequest(e, resultDispatch);
  }
}

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
        <h2 className={styles.mainHeading}>
          Manage the user:{' '}
          <NavLink className={'navLink'} to={`/in/users/${id}`}>
            {username}
          </NavLink>
        </h2>
        <div className={styles.general}>
          <p>
            <span className={styles.general__Title}>Registration date:</span>
            <span>{new Date(registrationDate).toLocaleDateString()}</span>
          </p>
          <p>
            <span className={styles.general__Title}>Last logged in:</span>
            <span>
              {lastLoggedIn
                ? `${new Date(lastLoggedIn).toLocaleTimeString()} ${new Date(
                    lastLoggedIn
                  ).toLocaleDateString()}`
                : 'Has not logged in yet'}
            </span>
          </p>
          <p>
            <span className={styles.general__Title}>Is email confirmed:</span>
            <span>{isEmailConfirmed ? 'Yes' : 'No'}</span>
          </p>
        </div>
        <div className={styles.descriptionSettings}>
          <h2 className={styles.heading}>Change user description</h2>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              change('description', id, { description }, resultDispatch);
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
              await change('email', id, { email }, resultDispatch);
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
              await change('username', id, { username }, resultDispatch);
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
                id,
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
                id,
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
          <h2 className={styles.heading}>Temporary ban:</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'tempBanDateTime',
                id,
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
