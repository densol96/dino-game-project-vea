import { useNewMessagesContext } from '../../context/NewMessagesProvider';
import PlayerRatings from './PlayerRatings';
import styles from './Ratings.module.scss';
import { useState, useEffect } from 'react';

const type = {
  players: 0,
  clans: 1,
};

function Ratings() {
  const [activeTab, setActiveTab] = useState(type.players);

  const { checkIfNewMessages } = useNewMessagesContext();
  useEffect(() => {
    checkIfNewMessages();
  });

  return (
    <div className={styles.container}>
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
        >
          Clans
        </button>
      </header>
      <main className={styles.data}>
        {activeTab === type.players ? <PlayerRatings /> : <>Test</>}
      </main>
      <div className={styles.searchByName}>
        <label htmlFor="search">Search by username: </label>
        <input type="text" className="input" id="search" />
        <button className="brown-btn btn">Search</button>
      </div>
    </div>
  );
}

export default Ratings;
