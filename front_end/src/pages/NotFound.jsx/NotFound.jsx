import { NavLink } from 'react-router-dom';
import styles from './NotFound.module.scss';

function NotFound() {
  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Page Not Found</h2>
      <NavLink className={styles.link} to="/">
        Go to main page
      </NavLink>
    </div>
  );
}

export default NotFound;
