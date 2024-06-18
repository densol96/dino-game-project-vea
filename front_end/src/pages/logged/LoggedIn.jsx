import { useNavigate, Outlet } from 'react-router-dom';

import { useEffect } from 'react';

import { useUserContext } from '../../context/UserProvider';

import styles from './LoggedIn.module.scss';

function Profile() {
  const { user, setUserFullInfo, logoutUser } = useUserContext();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate('/');
    }
  }, [user]);

  console.log(user);

  return user ? (
    <div className={styles.container}>
      <header className={styles.header}>
        <img className={styles.mainLogo} src="/dino-logo.png" />
        <div className={styles.profileInfo}>
          <p>
            <span className={styles.profileInfo__Title}>Username: </span>
            <span className={styles.profileInfo__Value}>{user.username}</span>
          </p>
          <p>
            <span className={styles.profileInfo__Title}>Type: </span>
            <span className={styles.profileInfo__Value}>{user.dinoType}</span>
          </p>
          <p>
            <span className={styles.profileInfo__Title}>Level:</span>
            <span className={styles.profileInfo__Value}>{user.level}</span>
          </p>
          <p>
            <span className={styles.profileInfo__Title}>Currency:</span>
            <span className={styles.profileInfo__Value}>{user.currency}</span>
          </p>
        </div>
      </header>
      <section className={styles.section}>
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
        <main className={styles.main}>
          <Outlet />
        </main>
      </section>
    </div>
  ) : (
    <p>isLoading............</p>
  );
}

export default Profile;
