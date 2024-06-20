import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

import styles from './ModalLogin.module.scss';
import { useUserContext } from '../../../../context/UserProvider';

async function sendSignInRequest(data, resultDisptach, navigate, setUser) {
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
    const user = jwtDecode(jwt).sub;
    setUser(user);
    navigate('/profile');
  } catch (e) {
    console.log(e);
    if (e.code === 'ERR_BAD_REQUEST') {
      const error = e.response.data;
      resultDisptach({
        type: 'ERROR',
        payload: {
          heading: error.name,
          message: error.message,
          type: error.type,
          errors: error.errors,
        },
      });
    } else if (e.code === 'ERR_NETWORK') {
      resultDisptach({
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
  }
}

function ModalLogin({ closeLogin, resultDispatch }) {
  // FOR SIGN IN
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const { setUser } = useUserContext();

  return (
    <div className={styles['modal-login']}>
      <div className={styles['login-container']}>
        <button
          className={styles['login-container__close-btn']}
          onClick={closeLogin}
        >
          X
        </button>
        <h2 className={styles['login-container__heading']}>
          Enter your details to Sign In:
        </h2>
        <form className={styles['login-container__form']}>
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
        </form>
        <div className={`${styles['login-container__buttons']}`}>
          <button
            className={`btn brown-btn ${styles.btn}`}
            onClick={() =>
              sendSignInRequest(
                { username, password },
                resultDispatch,
                navigate,
                setUser
              )
            }
          >
            Sign In
          </button>
          <button className={`btn brown-btn--reversed ${styles.btn}`}>
            Forgot password
          </button>
        </div>
      </div>
    </div>
  );
}

export default ModalLogin;
