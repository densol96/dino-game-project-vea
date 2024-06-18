import { useUserContext } from '../../context/UserProvider';
import styles from './Profile.module.scss';

function Profile() {
  const { user } = useUserContext();
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
        {user.description
          ? user.description
          : 'Currently, your user info is empty. Check the settings to add a description..'}
      </div>
      <div className={styles.profileGrid__BattleStats}>
        Здесь будут боевые характеристики и их кач
      </div>
      <div className={styles.profileGrid__Statistics}>
        Здесь будет статистика (награбленное, колво боев, побед, порадений и
        прочее)
      </div>
    </div>
  );
}

export default Profile;
