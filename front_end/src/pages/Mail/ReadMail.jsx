import styles from './ReadMail.module.scss';
import { useNavigate } from 'react-router-dom';

function ReadMail() {
  const navigate = useNavigate();

  return (
    <div className={styles.message}>
      <h2 className="heading">Read message</h2>
      <div className={styles.message__title}>
        <h3 className={styles.message__title_heading}>Title: </h3>
        <p className={styles.message__title_content}>Welcome to the game</p>
      </div>
      <div className={styles.message__title}>
        <h3 className={styles.message__title_heading}>From: </h3>
        <p className={styles.message__title_content}>denisolo</p>
      </div>
      <div className={styles.message__title}>
        <h3 className={styles.message__title_heading}>Received on: </h3>
        <p className={styles.message__title_content}>20/06/24 14:37</p>
      </div>
      <p className={styles.message__text}>
        Hello my dear friend! How r u?Hello my dear friend! How r u? Hello my
        dear friend! How r u?
      </p>
      <div className={styles.btns}>
        <button className="btn brown-btn">Reply</button>
        <button className="btn brown-btn--reversed">Delete</button>
        <button
          className="btn brown-btn--reversed"
          onClick={() => navigate(-1)}
        >
          Go back
        </button>
      </div>
    </div>
  );
}

export default ReadMail;
