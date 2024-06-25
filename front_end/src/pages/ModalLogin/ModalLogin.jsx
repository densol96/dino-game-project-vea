import { useState } from 'react';
import { useNavigate, useOutletContext } from 'react-router-dom';

import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

import styles from './ModalLogin.module.scss';
import { useUserContext } from '../../context/UserProvider';
import { handleBadRequest } from '../../helpers/helpers';
import LoadingSpinner from '../../components/Spinner/LoadingSpinner';

async function sendSignInRequest(
  data,
  resultDisptach,
  navigate,
  setUserFullInfo
) {
  resultDisptach({ type: 'IS_LOADING' });
  const API_ENDPOINT = 'http://localhost:8080/api/v1/auth/login';
  const { username, password } = data;

  // CHECK FOR EMPTY FIELDS
  if (!username || !password) {
    resultDisptach({ type: 'CLOSE' });
    setTimeout(() => {
      resultDisptach({
        type: 'ERROR',
        payload: {
          heading: 'Invalid user input',
          message: 'Please, make sure all the required fields are filled in!',
        },
      });
    }, 500);

    return;
  }

  // SEND POST REQUEST
  try {
    const response = await axios.post(API_ENDPOINT, {
      username,
      password,
    });
    const { jwt } = response.data;
    localStorage.setItem('dino_jwt', jwt);
    await setUserFullInfo();
    navigate('/in');
  } catch (e) {
    console.log(e);
    handleBadRequest(e, resultDisptach);
  }
}

function ModalLogin() {
  // FOR SIGN IN
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const { setUserFullInfo } = useUserContext();
  const { resultDispatch, isLoading } = useOutletContext();

  function login(e) {
    e.preventDefault();
    sendSignInRequest(
      { username, password },
      resultDispatch,
      navigate,
      setUserFullInfo
    );
  }

  return (
    <div className={styles['modal-login']}>
      <div className={styles['login-container']}>
        <button
          className={styles['login-container__close-btn']}
          onClick={() => navigate(-1)}
        >
          X
        </button>
        <h2 className={styles['login-container__heading']}>
          Enter your details to Sign In:
        </h2>
        <form className={styles['login-container__form']} onSubmit={login}>
          <label>Username:</label>
          <input
            type="text"
            name="username"
            id="username"
            className="input"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <label>Password:</label>
          <input
            type="password"
            name="password"
            id="password"
            className="input"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button style={{ display: 'none' }}></button>
        </form>
        <div className={`${styles['login-container__buttons']}`}>
          <button className={`btn brown-btn ${styles.btn}`} onClick={login}>
            {!isLoading ? 'Sign In' : <LoadingSpinner width={18} height={18} />}
          </button>
          <button
            className={`btn brown-btn--reversed ${styles.btn}`}
            onClick={() => navigate('/forgot')}
          >
            Forgot password
          </button>
        </div>
      </div>
    </div>
  );
}

export default ModalLogin;
