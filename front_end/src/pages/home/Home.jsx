import axios from 'axios';

import { useEffect, useReducer, useState } from 'react';
import { useNavigate, Outlet } from 'react-router-dom';

import styles from './Home.module.scss';

import ModalLogin from '../ModalLogin/ModalLogin';
import { useUserContext } from '../../context/UserProvider';
import LoadingSpinner from '../../components/Spinner/LoadingSpinner';

const DinoType = {
  CARNIVORE: 'carnivore',
  HERBIVORE: 'hebivore',
};

const HIDE_MESSAGE_AFTER_MS = 5000;

function signUpReducer(state, action) {
  switch (action.type) {
    case 'SET_USERNAME':
      return { ...state, username: action.value };
    case 'SET_EMAIL':
      return { ...state, email: action.value };
    case 'SET_PASSWORD':
      return { ...state, password: action.value };
    case 'SET_PASSWORD_CONFIRM':
      return { ...state, passwordConfirm: action.value };
    case 'SET_TYPE':
      return { ...state, dinoType: action.value };
    default:
      throw new Error('💥ERROR -> Check signUpReducer');
  }
}

function resultReducer(state, action) {
  switch (action.type) {
    case 'SUCCESS':
      return {
        buttonClickable: false,
        isLoading: false,
        forDisplay: true,
        error: {
          status: false,
          heading: '',
          message: '',
          type: '',
          errors: '',
        },
        success: {
          status: true,
          heading: action.payload.heading,
          message: action.payload.message,
        },
      };
    case 'ERROR':
      return {
        buttonClickable: true,
        isLoading: false,
        forDisplay: true,
        error: {
          status: true,
          heading: action.payload.heading,
          message: action.payload.message,
          type: action.payload.type,
          errors: action.payload.errors,
        },
        success: {
          status: false,
          heading: '',
          message: '',
        },
      };
    case 'IS_LOADING':
      return {
        buttonClickable: false,
        isLoading: true,
        forDisplay: false,
        error: {
          status: false,
          heading: '',
          message: '',
          type: '',
          errors: '',
        },
        success: {
          status: false,
          heading: '',
          message: '',
        },
      };
    case 'BUTTON_CLICKABLE':
      return {
        ...state,
        error: { ...state.error },
        success: { ...state.success },
        buttonClickable: true,
      };
    case 'CLOSE':
      return {
        buttonClickable: true,
        isLoading: false,
        forDisplay: false,
        error: {
          status: false,
          heading: '',
          message: '',
          type: '',
          errors: '',
        },
        success: {
          status: false,
          heading: '',
          message: '',
        },
      };
    default:
      throw new Error('💥 -> Check reponseReducer');
  }
}

async function sendSignUpRequest(data, resultDisptach) {
  const API_ENDPOINT = 'http://localhost:8080/api/v1/auth/register';
  const { username, password, passwordConfirm, email, dinoType } = data;

  // CHECK FOR EMPTY FIELDS
  resultDisptach({ type: 'IS_LOADING' });
  if (!username || !password || !passwordConfirm || !email) {
    setTimeout(() => {
      resultDisptach({ type: 'CLOSE' });
    }, 250);
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

  // CHECK THAT PASSWORDS MATCH
  if (password !== passwordConfirm) {
    setTimeout(() => {
      resultDisptach({ type: 'CLOSE' });
    }, 250);
    setTimeout(() => {
      resultDisptach({
        type: 'ERROR',
        payload: {
          heading: 'Invalid user input',
          message: 'Password and password confirm must match!',
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
      email,
      dinoType,
    });
    resultDisptach({
      type: 'SUCCESS',
      payload: {
        heading: 'Account has been created',
        message: response.data.message,
      },
    });

    // Button will be temporarily disabled for 3 seconds in case user accidently clicks on immediately after registration
    setTimeout(() => {
      resultDisptach({ type: 'BUTTON_CLICKABLE' });
    }, 3000);
  } catch (e) {
    console.log(e);
    if (e.code === 'ERR_BAD_REQUEST' || e.code === 'ERR_BAD_RESPONSE') {
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

function reduceValidationErrors(errors) {
  let parsedErrors = [];
  for (let field in errors) {
    parsedErrors.push(errors[field]);
  }
  return parsedErrors;
}

function Home() {
  const { user } = useUserContext();
  const navigate = useNavigate();

  // FOR SIGN UP
  const [signUpState, dispatchSignUp] = useReducer(signUpReducer, {
    username: '',
    email: '',
    password: '',
    passwordConfirm: '',
    dinoType: DinoType.CARNIVORE,
  });
  const { username, password, email, passwordConfirm, dinoType } = signUpState;

  // RESULT OF THE REQUEST (either /login or /authenticate)
  const [
    { success, error, forDisplay, isLoading, buttonClickable },
    resultDispatch,
  ] = useReducer(resultReducer, {
    buttonClickable: true,
    isLoading: false,
    forDisplay: false,
    error: {
      status: false,
      heading: '',
      message: '',
      type: '',
      errors: '',
    },
    success: {
      status: false,
      heading: '',
      message: '',
    },
  });
  let errors = error.errors ? reduceValidationErrors(error.errors) : [];

  // useEffect(() => {
  //   const timerId = setTimeout(() => {
  //     resultDispatch({ type: 'CLOSE' });
  //   }, HIDE_MESSAGE_AFTER_MS);
  //   return () => clearTimeout(timerId);
  // }, [resultDispatch, error]);

  const [modalIsOpen, setModalIsOpen] = useState(false);
  function closeLogin() {
    setModalIsOpen(false);
  }

  useEffect(() => {
    if (user) {
      navigate('/in');
    }
  }, [user, navigate]);

  if (user) {
    return (
      <p className="isLoading">
        <LoadingSpinner width={40} height={40} />
        Wait a second please.. We are proccessing your request.......
      </p>
    );
  }
  return (
    <>
      {/* NESTED ModalLogin for /login */}
      <Outlet context={{ closeLogin, resultDispatch, isLoading }} />
      {(error.status || success.status) && forDisplay && (
        <div
          className={`${styles['message-container']} ${
            error.status ? styles['error-color'] : styles['success-color']
          }`}
        >
          <p
            className={styles['message-container__close-btn']}
            onClick={() => resultDispatch({ type: 'CLOSE' })}
          >
            X
          </p>
          <h2 className={styles['message-container__heading']}>
            {error.status ? error.heading : success.heading}
          </h2>
          <p className={styles['message-container__content']}>
            {error.status ? error.message : success.message}
          </p>
          <ul>
            {errors.map((e, i) => (
              <li className={styles['message-container__content']} key={i}>
                {e}
              </li>
            ))}
          </ul>
        </div>
      )}

      <div className={styles['container']}>
        <img
          className={styles['central-logo']}
          src="dino-logo.png"
          alt="Dino Battle Main logo"
        />
        <img
          className={`${styles['left-logo']} ${styles['logo']} ${
            dinoType === DinoType.CARNIVORE ? styles['active-type'] : ''
          }`}
          src="carnivore-logo.png"
          alt="Carnivore dinosaur logo"
          onClick={() => {
            dispatchSignUp({ type: 'SET_TYPE', value: DinoType.CARNIVORE });
          }}
        />
        <img
          className={`${styles['right-logo']} ${styles['logo']} ${
            dinoType === DinoType.HERBIVORE ? styles['active-type'] : ''
          }`}
          src="herbivore-logo.png"
          alt="Herbivore dinosaur logo"
          onClick={() => {
            dispatchSignUp({ type: 'SET_TYPE', value: DinoType.HERBIVORE });
          }}
        />
        <h3 className={styles['carnivore-heading']}> Carnivore </h3>
        <h3 className={styles['herbivore-heading']}> Herbivore </h3>
        <main className={styles['main']}>
          <div className={styles['dino-type']}>
            <img
              className={styles['profile-pic']}
              src={`${
                dinoType === DinoType.CARNIVORE
                  ? 'carnivore-logo.png'
                  : 'herbivore-logo.png'
              }`}
              alt="Your dino type profile-pic"
            />
          </div>

          <div className={styles['input-container']}>
            <h2 className={styles['input-container__heading']}>
              Enter your details to start playing:
            </h2>
            <form
              className={styles.form}
              onSubmit={(e) => {
                e.preventDefault();
                sendSignUpRequest(signUpState, resultDispatch);
              }}
            >
              <input
                className="input"
                type="text"
                placeholder="Enter username"
                value={username}
                onChange={(e) =>
                  dispatchSignUp({
                    type: 'SET_USERNAME',
                    value: e.target.value,
                  })
                }
              />
              <input
                className="input"
                type="text"
                placeholder="Enter email"
                value={email}
                onChange={(e) =>
                  dispatchSignUp({
                    type: 'SET_EMAIL',
                    value: e.target.value,
                  })
                }
              />
              <input
                className="input"
                type="text"
                placeholder="Enter password"
                value={password}
                onChange={(e) =>
                  dispatchSignUp({
                    type: 'SET_PASSWORD',
                    value: e.target.value,
                  })
                }
              />
              <input
                className="input"
                type="text"
                placeholder="Confirm password"
                value={passwordConfirm}
                onChange={(e) =>
                  dispatchSignUp({
                    type: 'SET_PASSWORD_CONFIRM',
                    value: e.target.value,
                  })
                }
              />
              <button
                disabled={!buttonClickable}
                className="brown-btn btn"
                onClick={(e) => {
                  e.preventDefault();
                  sendSignUpRequest(signUpState, resultDispatch);
                }}
              >
                {isLoading ? 'Loading...' : 'Register'}
              </button>
            </form>
            <button
              className={styles['link']}
              onClick={() => navigate('/login')}
            >
              Already have an account
            </button>
          </div>
        </main>
      </div>
    </>
  );
}

export default Home;
