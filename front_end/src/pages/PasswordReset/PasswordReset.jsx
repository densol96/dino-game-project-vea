import styles from './PasswordReset.module.scss';
import {
  useNavigate,
  useParams,
  useOutletContext,
  useSearchParams,
} from 'react-router-dom';
import { useState } from 'react';
import LoadingSpinner from '../../components/Spinner/LoadingSpinner';
import { BASE_API_URL, handleBadRequest } from '../../helpers/helpers';
import axios from 'axios';

function PasswordReset() {
  const [searchParams, setSearchParams] = useSearchParams();
  const resetToken = searchParams.get('token');
  const navigate = useNavigate();
  const [password, setPassword] = useState();
  const [confirm, setConfirm] = useState();
  const { resultDispatch, isLoading } = useOutletContext();
  async function resetPassword(e) {
    e.preventDefault();
    resultDispatch({ type: 'IS_LOADING' });
    if (password !== confirm) {
      return resultDispatch({
        type: 'ERROR',
        payload: {
          heading: 'Invalid input',
          message: 'Password and password-confirm must match',
        },
      });
    }
    const API_ENDPOINT = `${BASE_API_URL}/auth/password-reset/${resetToken}`;
    console.log(API_ENDPOINT);
    try {
      const response = await axios.post(API_ENDPOINT, { password });
      console.log(response.data);
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Congrats!', message: response.data.message },
      });
      setTimeout(() => {
        navigate('/login');
      }, 3000);
    } catch (e) {
      console.log(e);
      handleBadRequest(e, resultDispatch);
    }
  }

  return (
    <div className={styles['modal-login']}>
      <div className={styles['login-container']}>
        <button
          className={styles['login-container__close-btn']}
          onClick={() => navigate('/')}
        >
          X
        </button>
        <h2 className={styles['login-container__heading']}>
          Enter new password:
        </h2>
        <form
          className={styles['login-container__form']}
          onSubmit={resetPassword}
        >
          <label>New password:</label>
          <input
            type="password"
            name="username"
            id="username"
            className="input"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <label>Password:</label>
          <input
            type="password"
            name="password"
            id="password"
            className="input"
            value={confirm}
            onChange={(e) => setConfirm(e.target.value)}
          />
          <button style={{ display: 'none' }}></button>
        </form>
        <div className={`${styles['login-container__buttons']}`}>
          <button
            className={`btn brown-btn ${styles.btn}`}
            onClick={resetPassword}
          >
            {!isLoading ? (
              'Reset password'
            ) : (
              <LoadingSpinner width={18} height={18} />
            )}
          </button>
        </div>
      </div>
    </div>
  );
}

export default PasswordReset;
