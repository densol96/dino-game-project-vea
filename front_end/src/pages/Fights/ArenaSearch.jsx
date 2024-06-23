import styles from './ArenaSearch.module.scss';
import {
  handleBadRequest,
  BASE_API_URL,
  extractStats,
  useResponseResult,
} from '../../helpers/helpers';
import { useNavigate, useOutletContext, NavLink } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { headersWithToken } from '../../context/UserProvider';
import LoadingSpinner from '../../components/Spinner/LoadingSpinner';

async function findOpponent(
  setFoundOpponent,
  resultDispatch,
  loaderDispatch,
  navigate
) {
  loaderDispatch({ type: 'IS_LOADING' });
  const API_ENDPOINT = `${BASE_API_URL}/combat/find/one`;
  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    setFoundOpponent(response.data);
    loaderDispatch({ type: 'SUCCESS', payload: { heading: '', message: '' } });
  } catch (e) {
    handleBadRequest(e, resultDispatch);
    setTimeout(() => {
      navigate('/in/arena');
    }, 4000);
  }
}

function ArenaSearch() {
  const [foundOpponent, setFoundOpponent] = useState(null);
  const { resultDispatch, isOnJob, isOnAttackCoolDown, user, setUserFullInfo } =
    useOutletContext();
  const canFight = !isOnJob && !isOnAttackCoolDown;
  const navigate = useNavigate();
  const [{ isLoading }, loaderDispatch] = useResponseResult();
  const { armor, agility, damage, health, criticalHitPercentage } =
    foundOpponent?.playerStats ? foundOpponent.playerStats : {};
  const max = Math.max(
    ...extractStats(user?.playerStats ? user.playerStats : [])
  );

  useEffect(() => {
    setUserFullInfo();
    if (!canFight) navigate('/in/arena');
  }, [foundOpponent]);

  useEffect(() => {
    canFight && findOpponent(setFoundOpponent, resultDispatch, loaderDispatch);
  }, []);

  return (
    <>
      {!foundOpponent || isLoading ? (
        <p className={styles.isLoading}>
          <LoadingSpinner width={40} height={40} />
          Wait a second please.. We are proccessing your request.......
        </p>
      ) : (
        <>
          <header className={styles.publicHeader}>
            <div className={styles.publicHeader__Container}>
              <h3 className={styles.publicHeader__Container__heading}>
                Username:{' '}
              </h3>
              <p className={styles.publicHeader__Container__value}>
                {foundOpponent.username}
              </p>
            </div>
            <div className={styles.publicHeader__Container}>
              <h3 className={styles.publicHeader__Container__heading}>
                Level:{' '}
              </h3>
              <p className={styles.publicHeader__Container__value}>
                {foundOpponent.level}
              </p>
            </div>
          </header>
          <div className={styles.container}>
            <main className={styles.main}>
              <img
                className={styles.profileImage}
                src={
                  foundOpponent.dinoType === 'carnivore'
                    ? '/carnivore-logo.png'
                    : '/herbivore-logo.png'
                }
                alt="Profile avatar"
              />

              <div className={styles.skills}>
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
            </main>
            <div className={styles.btnsContainer}>
              <NavLink to={`/in/arena/result/${foundOpponent.id}`}>
                <button className="btn brown-btn">Attack</button>
              </NavLink>
              <button
                className="btn brown-btn--reversed"
                onClick={() =>
                  findOpponent(setFoundOpponent, resultDispatch, loaderDispatch)
                }
              >
                Look for someone else
              </button>
            </div>
          </div>
        </>
      )}
    </>
  );
}

export default ArenaSearch;
