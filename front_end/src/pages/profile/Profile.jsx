import { useNavigate } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import { useEffect } from 'react';

import styles from './Profile.module.scss';

function Profile() {
  const { user, setUserFullInfo, logoutUser } = useUserContext();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate('/');
    }
  }, [user]);

  return user ? (
    <div className={styles.container}>
      <header className={styles.header}>
        <img className={styles.mainLogo} src="/dino-logo.png" />
        <div className={styles.profileInfo}>
          <div className={styles.basicInfo}>
            <p className={styles.gridHeading}>Username</p>
            <p className={styles.gridValue}>{user.username}</p>

            <p className={styles.gridHeading}>Level</p>
            <p className={styles.gridValue}>{user.level}</p>

            <p className={styles.gridHeading}>Dino type</p>
            <p className={styles.gridValue}>{user.dinoType}</p>

            <p className={styles.gridHeading}>Clan</p>
            <p className={styles.gridValue}>
              {user.clanTitle || 'Not in clan'}
            </p>
          </div>
          <div className={styles.resourcesInfo}>
            <p>Gold</p>
            <p>1555</p>
            <p>Fish</p>
            <p>935</p>
          </div>
        </div>
      </header>
      <main>
        <aside className={styles.aside}>
          <ul>
            <li className={styles.listItem}>
              <ion-icon name="accessibility-outline"></ion-icon>
              <span className={styles.listItem__text}>Profile</span>
              <ion-icon name="accessibility-outline"></ion-icon>
            </li>
            <li className={styles.listItem}>
              <ion-icon name="ribbon-outline"></ion-icon>
              <span className={styles.listItem__text}>Arena</span>
              <ion-icon name="ribbon-outline"></ion-icon>
            </li>
            <li className={styles.listItem}>
              <ion-icon name="leaf-outline"></ion-icon>
              <span className={styles.listItem__text}>Farm</span>
              <ion-icon name="leaf-outline"></ion-icon>
            </li>
            <li className={styles.listItem}>
              <ion-icon name="people-circle-outline"></ion-icon>
              <span className={styles.listItem__text}>Clan</span>
              <ion-icon name="people-circle-outline"></ion-icon>
            </li>
          </ul>

          <ul>
            <li className={styles.listItem}>
              <ion-icon name="settings-outline"></ion-icon>
              <span className={styles.listItem__text}>Settings</span>
              <ion-icon name="settings-outline"></ion-icon>
            </li>
            <li
              className={styles.listItem}
              onClick={() => {
                logoutUser();
                navigate('/');
              }}
            >
              <ion-icon name="key-outline"></ion-icon>
              <span className={styles.listItem__text}>Logout</span>
              <ion-icon name="key-outline"></ion-icon>
            </li>
          </ul>
        </aside>
      </main>
    </div>
  ) : (
    <p>isLoading............</p>
  );
}

export default Profile;
