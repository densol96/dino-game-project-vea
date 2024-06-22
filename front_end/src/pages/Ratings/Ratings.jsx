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

const type = {
  players: 0,
  clans: 1,
};

function Ratings() {
  const [activeTab, setActiveTab] = useState(type.players);
  const { checkIfNewMessages } = useNewMessagesContext();
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);

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
        <div className={styles.searchByName}>
          <label htmlFor="search">Search by username: </label>
          <input type="text" className="input" id="search" />
          <button className="brown-btn btn">Search</button>
        </div>
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
