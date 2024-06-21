import { useState } from 'react';
import styles from './Mail.module.scss';
import { useNavigate, NavLink } from 'react-router-dom';
import { styleNavLink } from '../../helpers/helpers';

const type = {
  in: 0,
  out: 1,
};

function Mail() {
  const [activeTab, setActiveTab] = useState(type.in);
  const mailExample = {
    from: 'solodavi',
    text: 'Hello solodeni how are youmy friend?!',
  };
  return (
    <div>
      <header className="header">
        <button
          className={`header__Button ${activeTab === type.in ? 'active' : ''}`}
        >
          Incoming mail
        </button>
        <button
          className={`header__Button ${activeTab === type.out ? 'active' : ''}`}
        >
          Outgoing mail
        </button>
      </header>
      <div className="optionLine">
        <div className="sortByOption">
          <label>Sort mail By:</label>
          <select>
            <option value="all">All</option>
            <option value="unread">Read</option>
            <option value="read">Unread</option>
          </select>
        </div>
        <NavLink className={styles.writeLink} to="/in/mail/write">
          <span>Write a message</span>
        </NavLink>
      </div>
      <table>
        <thead>
          <tr>
            <th>From</th>
            <th>Topic</th>
            <th>Date-time</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr className={styles.readMail}>
            <td>admin</td>
            <td>Welcome to the game</td>
            <td>20/06/24 14:37</td>
            <td className={styles.actionTd}>
              <ion-icon class={styles.icon} name="eye"></ion-icon> /
              <ion-icon class={styles.icon} name="trash"></ion-icon>
            </td>
          </tr>
          <tr className={styles.unreadMail}>
            <td>admin</td>
            <td>Welcome to the game</td>
            <td>20/06/24 14:37</td>
            <td className={styles.actionTd}>
              <NavLink className={styleNavLink} to={`/in/mail/read/123`}>
                <ion-icon class={styles.icon} name="eye"></ion-icon>
              </NavLink>
              /<ion-icon class={styles.icon} name="trash"></ion-icon>
            </td>
          </tr>
        </tbody>
      </table>
      <div className="pagination">
        <ion-icon id="icon" name="chevron-back-outline"></ion-icon>
        <span>2</span>
        <ion-icon id="icon" name="chevron-forward-outline"></ion-icon>
      </div>
    </div>
  );
}

export default Mail;
