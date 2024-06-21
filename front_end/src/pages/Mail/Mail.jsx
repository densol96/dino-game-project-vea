import { useEffect, useState } from 'react';
import styles from './Mail.module.scss';
import { useNavigate, NavLink } from 'react-router-dom';
import { styleNavLink } from '../../helpers/helpers';
import axios from 'axios';
import { headersWithToken } from '../../context/UserProvider';

const BASE_URL = `http://localhost:8080/api/v1/mail`;

const type = {
  in: 'incoming',
  out: 'outgoing',
};

async function getIncomingMail(mailType, page, setMailForDisplay) {
  const GET_MAIL_API = `${BASE_URL}/get-all-${mailType}?page=${page || 1}`;
  console.log(GET_MAIL_API);
  try {
    const response = await axios.get(GET_MAIL_API, headersWithToken());
    setMailForDisplay(response.data);
  } catch (e) {
    console.log(e);
  }
}

function formatDate(date) {
  const d = new Date(date);
  const day = d.getDate() < 10 ? `0${d.getDate()}` : d.getDate();
  const month = d.getMonth() < 10 ? `0${d.getMonth()}` : d.getMonth();
  const hrs = d.getHours() < 10 ? `0${d.getHours()}` : d.getHours();
  const mins = d.getMinutes() < 10 ? `0${d.getMinutes()}` : d.getMinutes();
  const stringDateTime = `${day}/${month}/${d.getFullYear()} ${hrs}:${mins}`;
  return stringDateTime;
}

function Mail() {
  const [mailType, setMailType] = useState(type.in);
  const [page, setPage] = useState(1);
  const [mailForDisplay, setMailForDisplay] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    getIncomingMail(mailType, page, setMailForDisplay);
  }, [mailType, page]);

  return (
    <div>
      <header className="header">
        <button
          className={`header__Button ${mailType === type.in ? 'active' : ''}`}
        >
          Incoming mail
        </button>
        <button
          className={`header__Button ${mailType === type.out ? 'active' : ''}`}
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
            <th>{mailType === type.in ? 'From' : 'To'}</th>
            <th>Title</th>
            <th>Sent on / at</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {mailForDisplay.map((letter, i) => {
            console.log(letter);
            return (
              <tr className={styles.readMail} key={i}>
                <td>{mailType === type.in ? letter.from : letter.to}</td>
                <td>{letter.title}</td>
                <td>{formatDate(letter.sentAt)}</td>
                <td className={styles.actionTd}>
                  <NavLink className={() => styles.viewIcon}>
                    <ion-icon class={styles.icon} name="eye"></ion-icon>
                  </NavLink>
                  /
                  <ion-icon
                    onClick={() => alert('clicked')}
                    class={styles.icon}
                    name="trash"
                  ></ion-icon>
                </td>
              </tr>
            );
          })}
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
