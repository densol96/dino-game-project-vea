import styles from './Arena.module.scss';
import LocationDescription from '../../components/LocationDescription';
import { NavLink, useOutletContext } from 'react-router-dom';

function ArenaMain() {
  const { isOnJob, isOnAttackCoolDown } = useOutletContext();

  const description = (
    <>
      <span>
        Welcome to the Arena, where dinosaurs settle their scores! Train and
        upgrade your stats to increase your chances of victory.
      </span>
      <ul>
        <li>
          <span className="bold">Battle Rules:</span> Attack other dinosaurs
          every 15 minutes. If you win, you take gold from the defeated
          opponent. The defeated dinosaur gains an hour of immunity from further
          attacks.
        </li>
        <li>
          <span className="bold">Leaderboard:</span> Compete to climb the ranks
          and become the top dinosaur warrior! Check the{' '}
          <NavLink to="/in/ratings">ratings</NavLink>!
        </li>
      </ul>
      <span>May the best dinosaur win! Good luck!</span>
    </>
  );

  return (
    <>
      <LocationDescription
        localStorageName="arenaDescriptionOpen"
        heading="Welcome to the arena"
        src="/arena.jpg"
        alt="Image of the dinosaur arena"
        description={description}
      />

      <div className={styles.container}>
        {isOnJob && (
          <p className={styles.message}>
            You cannot attack while being on farm!
          </p>
        )}
        {!isOnJob && isOnAttackCoolDown && (
          <p className={styles.message}>
            You cannot attack while being on cool down from the last attack!
          </p>
        )}
        {!isOnJob && !isOnAttackCoolDown && (
          <div className={styles.actionContainer}>
            <NavLink to="/in/arena/search">
              <button className="btn brown-btn">Find an opponent</button>
            </NavLink>
          </div>
        )}
      </div>
    </>
  );
}

export default ArenaMain;
