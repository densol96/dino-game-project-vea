import { useEffect, useState } from 'react';
import styles from './Mail.module.scss';
import { useNavigate, NavLink } from 'react-router-dom';
import { styleNavLink, formatDate } from '../../helpers/helpers';
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

async function getNumOfPagesForIncomingMail(mailType, setPagesTotal) {
  const GET_MAIL_API = `${BASE_URL}/get-number-pages-${mailType}`;

  try {
    const response = await axios.get(GET_MAIL_API, headersWithToken());
    setPagesTotal(+response.data);
  } catch (e) {
    console.log('💥💥💥💥' + e);
  }
}

function Mail() {
  const [mailType, setMailType] = useState(type.in);
  const [page, setPage] = useState(1);
  const [pagesTotal, setPagesTotal] = useState(1);
  const [mailForDisplay, setMailForDisplay] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    getNumOfPagesForIncomingMail(mailType, setPagesTotal);
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
      <table className={styles.table}>
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
              <tr className={letter.isUnread ? styles.unreadMail : ''} key={i}>
                <td>{mailType === type.in ? letter.from : letter.to}</td>
                <td>{letter.title}</td>
                <td>{formatDate(letter.sentAt)}</td>
                <td className={styles.actionTd}>
                  <NavLink
                    to={`/in/mail/read/${letter.id}`}
                    className={() => styles.viewIcon}
                  >
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
        <div className={styles.paginationHolder}>
          <div className="pagination">
            {
              <button
                className={`${page === 1 ? styles.hide : ''} ${styles.btnNav}`}
              >
                <ion-icon
                  onClick={() => {
                    setPage((page) => page - 1);
                  }}
                  id="icon"
                  name="chevron-back-outline"
                ></ion-icon>
              </button>
            }
            {pagesTotal > 1 && <span>{page}</span>}
            {
              <button
                className={`${page === pagesTotal ? styles.hide : ''} ${
                  styles.btnNav
                }`}
              >
                <ion-icon
                  onClick={() => {
                    setPage((page) => page + 1);
                  }}
                  id="icon"
                  name="chevron-forward-outline"
                ></ion-icon>
              </button>
            }
          </div>
        </div>
      </table>
    </div>
  );
}

export default Mail;
