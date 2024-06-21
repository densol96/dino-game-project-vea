import { useNavigate } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import { useEffect } from 'react';
import { useNewMessagesContext } from '../../context/NewMessagesProvider';

import styles from './Profile.module.scss';

function extractStats(obj) {
  const arr = [];
  for (let key in obj) {
    arr.push(obj[key]);
  }
  return arr;
}

function Profile() {
  const { user } = useUserContext();
  const { agility, armor, damage, health, criticalHitPercentage } =
    user.playerStats;
  const max = Math.max(...extractStats(user.playerStats));

  const { checkIfNewMessages } = useNewMessagesContext();
  useEffect(() => {
    checkIfNewMessages();
  });

  return (
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
          {!user.description
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
            <p className={styles.price}>{damage * 5}</p>
          </div>
          <ion-icon id={styles.addSkill} name="add-circle-outline"></ion-icon>
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
            <p className={styles.price}>{armor * 5}</p>
          </div>
          <ion-icon id={styles.addSkill} name="add-circle-outline"></ion-icon>
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
            <p className={styles.price}>{agility * 5}</p>
          </div>
          <ion-icon id={styles.addSkill} name="add-circle-outline"></ion-icon>
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
            <p className={styles.price}>{health * 5}</p>
          </div>
          <ion-icon id={styles.addSkill} name="add-circle-outline"></ion-icon>
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
            <p className={styles.price}>{criticalHitPercentage * 5}</p>
          </div>
          <ion-icon id={styles.addSkill} name="add-circle-outline"></ion-icon>
        </div>
      </div>
      <div className={styles.profileGrid__Statistics}>
        <h2 className={styles.miniHeader}>Profile statistics:</h2>
        <div className={styles.profileGrid__Statistics__One}>
          <ion-icon name="thumbs-up"></ion-icon>
          <p>Battles won: </p>
          <p>5</p>
        </div>
        <div className={styles.profileGrid__Statistics__One}>
          <ion-icon name="thumbs-down"></ion-icon>
          <p>Battles lost: </p>
          <p>3</p>
        </div>
        <div className={styles.profileGrid__Statistics__One}>
          <ion-icon id={styles.wonMoney} name="logo-usd"></ion-icon>
          <p>Currency stolen: </p>
          <p>300</p>
        </div>
        <div className={styles.profileGrid__Statistics__One}>
          <ion-icon id={styles.lostMoney} name="logo-usd"></ion-icon>
          <p>Currency lost: </p>
          <p>155</p>
        </div>
      </div>
    </div>
  );
}

export default Profile;
