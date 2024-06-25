import { useState } from 'react';
import styles from './PasswordForgot.module.scss';
import { useOutletContext } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import LoadingSpinner from '../../components/Spinner/LoadingSpinner';
import { BASE_API_URL, handleBadRequest } from '../../helpers/helpers';
import axios from 'axios';

function PasswordForgot() {
  const [email, setEmail] = useState('');
  const { resultDispatch, isLoading } = useOutletContext();
  const navigate = useNavigate();

  async function getToken(e) {
    e.preventDefault();
    resultDispatch({ type: 'IS_LOADING' });
    const API_ENDPOINT = `${BASE_API_URL}/auth/forgot-password/${email}`;
    try {
      const response = await axios.get(API_ENDPOINT);
      console.log(response.data);
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
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
          Enter your email:
        </h2>
        <form className={styles['login-container__form']} onSubmit={getToken}>
          <label>Email:</label>
          <input
            type="text"
            name="username"
            id="username"
            className="input"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button style={{ display: 'none' }}></button>
        </form>
        <div className={`${styles['login-container__buttons']}`}>
          <button className={`btn brown-btn ${styles.btn}`} onClick={getToken}>
            {!isLoading ? (
              'Get reset token'
            ) : (
              <LoadingSpinner width={18} height={18} />
            )}
          </button>
        </div>
      </div>
    </div>
  );
}

export default PasswordForgot;
