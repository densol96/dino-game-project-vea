import styles from './WriteMail.module.scss';
import { useNavigate } from 'react-router-dom';

function WriteMail() {
  const navigate = useNavigate();
  return (
    <div className={styles.message}>
      <h2 className="heading">Write message</h2>
      <div className={styles.inputUnit}>
        <label htmlFor="to">To:</label>
        <input id="to" className={`input ${styles.toInput}`} type="text" />
      </div>
      <div className={styles.inputUnit}>
        <label htmlFor="to">Title:</label>
        <input className={`input ${styles.titleInput}`} type="text" />
      </div>
      <div className={styles.messageUnit}>
        <label htmlFor="to">Message:</label>
        <textarea className={`input ${styles.textInput}`} />
      </div>

      <div className={styles.btns}>
        <button className="btn brown-btn">Send</button>
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

export default WriteMail;
