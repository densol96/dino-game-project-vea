import { useEffect, useState } from 'react';
import styles from '../Profile/Profile.module.scss';
import NotificationMessage from '../notificationMessage/NotificationMessage';
import {
  useResponseResult,
  reduceValidationErrors,
  extractStats,
  BASE_API_URL,
  handleBadRequest,
} from '../../helpers/helpers';
import axios from 'axios';
import { headersWithToken, useUserContext } from '../../context/UserProvider';
import { useParams, NavLink } from 'react-router-dom';

async function getUser(id, resultDispatch, setUser) {
  resultDispatch({ type: 'IS_LOADING' });
  const API_ENDPOINT = `${BASE_API_URL}/ratings/users/${id}`;
  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    console.log(response.data);
    setUser(response.data);
  } catch (e) {
    handleBadRequest(e, resultDispatch);
  }
}

function PublicProfile() {
  const { id } = useParams();
  const [user, setUser] = useState('');
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);
  const { agility, armor, damage, health, criticalHitPercentage } =
    user?.playerStats ? user.playerStats : {};
  const { combatsTotal, combatsWon, currencyWon, currencyLost } =
    user?.combatStats ? user.combatStats : {};
  const max = Math.max(
    ...extractStats(user?.playerStats ? user.playerStats : [])
  );
  const { user: loggedInUser } = useUserContext();

  console.log(user);

  useEffect(() => {
    getUser(id, resultDispatch, setUser);
  }, [id, resultDispatch]);

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        forDisplay={forDisplay}
        errors={errors}
        resultDispatch={resultDispatch}
      />
      {!user ? (
        <p>Wait just one second.. Page is loading...</p>
      ) : (
        <>
          <header className={styles.publicHeader}>
            <div className={styles.publicHeader__Container}>
              <h3 className={styles.publicHeader__Container__heading}>
                Username:{' '}
              </h3>
              <p className={styles.publicHeader__Container__value}>
                {user.username}
              </p>
            </div>
            <div className={styles.publicHeader__Container}>
              <h3 className={styles.publicHeader__Container__heading}>
                Level:{' '}
              </h3>
              <p className={styles.publicHeader__Container__value}>
                {user.level}
              </p>
            </div>
          </header>
          <div className={styles.profileGrid}>
            <div className={styles.profileGrid__Image}>
              <img
                src={
                  user.dinoType === 'carnivore'
                    ? '/carnivore-logo.png'
                    : '/herbivore-logo.png'
                }
                alt="Profile avatar"
              />
            </div>

            <div className={styles.profileGrid__Description}>
              <h2 className={styles.miniHeader}>Profile description:</h2>
              <p className={styles.profileGrid__Description__Text}>
                {' '}
                {user.description
                  ? user.description
                  : 'Currently, the user info is empty.'}
              </p>
            </div>

            <div className={styles.profileGrid__BattleStats}>
              <div className={styles.skill}>
                <img
                  className={styles.skill__Icon}
                  src="/damage-icon.png"
                  alt="Damage"
                />
                <p className={styles.skill__IconName}>Damage</p>
                <p className={styles.skill__Value}>{damage}</p>
                <progress max={max} value={damage}></progress>
              </div>
              <div className={styles.skill}>
                <img
                  className={styles.skill__Icon}
                  src="/armor-icon.png"
                  alt="Armor"
                />
                <p className={styles.skill__IconName}>Armor</p>
                <p className={styles.skill__Value}>{armor}</p>
                <progress max={max} value={armor}></progress>
              </div>
              <div className={styles.skill}>
                <img
                  className={styles.skill__Icon}
                  src="/agility-icon.png"
                  alt="Agility"
                />
                <p className={styles.skill__IconName}>Agility</p>
                <p className={styles.skill__Value}>{agility}</p>
                <progress max={max} value={agility}></progress>
              </div>
              <div className={styles.skill}>
                <img
                  className={styles.skill__Icon}
                  src="/health-icon.png"
                  alt="Health"
                />
                <p className={styles.skill__IconName}>Health</p>
                <p className={styles.skill__Value}>{health}</p>
                <progress max={max} value={health}></progress>
              </div>
              <div className={styles.skill}>
                <img
                  className={styles.skill__Icon}
                  src="/critical-hit-icon.png"
                  alt="Critical hit"
                />
                <p className={styles.skill__IconName}>Critical hit chance</p>
                <p className={styles.skill__Value}>{criticalHitPercentage}</p>
                <progress max={max} value={criticalHitPercentage}></progress>
              </div>
            </div>
            <div className={styles.profileGrid__Statistics}>
              <h2 className={styles.miniHeader}>Profile statistics:</h2>
              <div className={styles.profileGrid__Statistics__One}>
                <ion-icon name="thumbs-up"></ion-icon>
                <p>Battles won: </p>
                <p>{combatsWon}</p>
              </div>
              <div className={styles.profileGrid__Statistics__One}>
                <ion-icon name="thumbs-down"></ion-icon>
                <p>Battles lost: </p>
                <p>{combatsTotal - combatsWon}</p>
              </div>
              <div className={styles.profileGrid__Statistics__One}>
                <ion-icon id={styles.wonMoney} name="logo-usd"></ion-icon>
                <p>Currency stolen: </p>
                <p>{currencyWon}</p>
              </div>
              <div className={styles.profileGrid__Statistics__One}>
                <ion-icon id={styles.lostMoney} name="logo-usd"></ion-icon>
                <p>Currency lost: </p>
                <p>{currencyLost}</p>
              </div>
            </div>
          </div>

          <div className={styles.forAdmin}>
            {loggedInUser.role === 'ADMIN' && user.role !== 'ADMIN' && (
              <NavLink to={`/in/admin/manage/${user.id}`}>
                <button className="btn brown-btn">Manage this user</button>
              </NavLink>
            )}
          </div>
        </>
      )}
    </>
  );
}

export default PublicProfile;
