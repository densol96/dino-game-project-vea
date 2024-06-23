import { useNavigate } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import { useEffect, useState } from 'react';
import { useNewMessagesContext } from '../../context/NewMessagesProvider';
// import { IonIcon } from '@ionic/react';

import styles from './Profile.module.scss';
import axios from 'axios';
import {
  reduceValidationErrors,
  useResponseResult,
  extractStats,
  handleBadRequest,
} from '../../helpers/helpers';
import NotificationMessage from '../notificationMessage/NotificationMessage';

function Profile() {
  const { user, setUserFullInfo } = useUserContext();
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);
  const { agility, armor, damage, health, criticalHitPercentage } =
    user.playerStats;
  const { combatsTotal, combatsWon, currencyWon, currencyLost } =
    user.combatStats;
  const max = Math.max(...extractStats(user.playerStats));
  const PRICE = 5;

  const { checkIfNewMessages } = useNewMessagesContext();
  useEffect(() => {
    checkIfNewMessages();
  }, []);

  const postNewStats = async (
    damageAdded,
    armorAdded,
    agilityAdded,
    healthAdded,
    critAdded
  ) => {
    resultDispatch({ type: 'IS_LOADING' });
    const API_ENDPOINT = 'http://localhost:8080/api/v1/player/ingame-stats';

    try {
      const response = await axios.post(API_ENDPOINT, {
        playerId: user.id,
        currencySpent: PRICE,
        armor: armor + armorAdded,
        agility: agility + agilityAdded,
        health: health + healthAdded,
        damage: damage + damageAdded,
        criticalHitPercentage: criticalHitPercentage + critAdded,
        endurance: 0,
      });
      rerenderStatsAfterDb();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const rerenderStatsAfterDb = () => {
    setUserFullInfo();
  };

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        forDisplay={forDisplay}
        errors={errors}
        resultDispatch={resultDispatch}
      />
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
              : 'Currently, your user info is empty. Check the settings to add a description..'}
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
            <div className={styles.skill__Price}>
              <ion-icon id={styles.price} name="logo-usd"></ion-icon>
              <p className={styles.price}>{PRICE}</p>
            </div>
            <ion-icon
              onClick={() => postNewStats(1, 0, 0, 0, 0)}
              id={styles.addSkill}
              name="add-circle-outline"
            ></ion-icon>
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
            <div className={styles.skill__Price}>
              <ion-icon id={styles.price} name="logo-usd"></ion-icon>
              <p className={styles.price}>{PRICE}</p>
            </div>
            <ion-icon
              onClick={() => postNewStats(0, 1, 0, 0, 0)}
              id={styles.addSkill}
              name="add-circle-outline"
            ></ion-icon>
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
            <div className={styles.skill__Price}>
              <ion-icon id={styles.price} name="logo-usd"></ion-icon>
              <p className={styles.price}>{PRICE}</p>
            </div>
            <ion-icon
              onClick={() => postNewStats(0, 0, 1, 0, 0)}
              id={styles.addSkill}
              name="add-circle-outline"
            ></ion-icon>
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
            <div className={styles.skill__Price}>
              <ion-icon id={styles.price} name="logo-usd"></ion-icon>
              <p className={styles.price}>{PRICE}</p>
            </div>
            <ion-icon
              onClick={() => postNewStats(0, 0, 0, 1, 0)}
              id={styles.addSkill}
              name="add-circle-outline"
            ></ion-icon>
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
            <div className={styles.skill__Price}>
              <ion-icon id={styles.price} name="logo-usd"></ion-icon>
              <p className={styles.price}>{PRICE}</p>
            </div>
            <ion-icon
              onClick={() => postNewStats(0, 0, 0, 0, 1)}
              id={styles.addSkill}
              name="add-circle-outline"
            ></ion-icon>
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
    </>
  );
}

export default Profile;
