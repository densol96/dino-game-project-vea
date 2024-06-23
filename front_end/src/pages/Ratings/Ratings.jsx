import styles from './Ratings.module.scss';
import { useNewMessagesContext } from '../../context/NewMessagesProvider';
import { useState, useEffect } from 'react';
import {
  handleBadRequest,
  reduceValidationErrors,
  useResponseResult,
  BASE_API_URL,
} from '../../helpers/helpers';
import PlayerRatings from './PlayerRatings';
import NotificationMessage from '../notificationMessage/NotificationMessage';
import Pagination from '../../components/Pagination';
import { headersWithToken } from '../../context/UserProvider';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
const type = {
  players: 0,
  clans: 1,
};

async function getIdByUsername(username, resultDispatch, navigate) {
  resultDispatch({ type: 'IS_LOADING' });
  const API_ENDPOINT = `${BASE_API_URL}/users/id-by/${username}`;
  if (!username) {
    return resultDispatch({
      type: 'ERROR',
      payload: {
        heading: 'Invalid user input',
        message: 'Username cannot be empty',
      },
    });
  }

  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    navigate(`/in/users/${response.data}`);
  } catch (e) {
    console.log(e);
    handleBadRequest(e, resultDispatch);
  }
}

function Ratings() {
  const [activeTab, setActiveTab] = useState(type.players);
  const { checkIfNewMessages } = useNewMessagesContext();
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);
  const [username, setUsername] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    checkIfNewMessages();
  });

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        resultDispatch={resultDispatch}
        forDisplay={forDisplay}
        errors={errors}
      />
      <div className={styles.container}>
        <form
          className={styles.searchByName}
          onSubmit={(e) => {
            e.preventDefault();
            getIdByUsername(username, resultDispatch, navigate);
          }}
        >
          <label htmlFor="search">Search by username: </label>
          <input
            type="text"
            className="input"
            id="search"
            onChange={(e) => setUsername(e.target.value)}
          />
          <button className="brown-btn btn">Search</button>
        </form>
        <header className={styles.header}>
          <button
            className={`${styles.header__Button} ${
              activeTab === type.players ? styles.active : ''
            }`}
          >
            Players
          </button>
          <button
            className={`${styles.header__Button} ${
              activeTab === type.clans ? styles.active : ''
            }`}
            onClick={() =>
              alert(
                'We are planning to introduce clan-ratings in the next game update patch...'
              )
            }
          >
            Clans
          </button>
        </header>

        <main className={styles.data}>
          {activeTab === type.players ? (
            <PlayerRatings resultDispatch={resultDispatch} />
          ) : (
            <>Test</>
          )}
        </main>
      </div>
    </>
  );
}

export default Ratings;
