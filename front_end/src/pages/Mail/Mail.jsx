import { useEffect, useState } from 'react';
import styles from './Mail.module.scss';
import { useNavigate, NavLink } from 'react-router-dom';
import {
  styleNavLink,
  formatDate,
  handleBadRequest,
  reduceValidationErrors,
  useResponseResult,
  BASE_API_URL,
} from '../../helpers/helpers';
import axios from 'axios';
import { headersWithToken } from '../../context/UserProvider';
import NotificationMessage from '../notificationMessage/NotificationMessage';

const BASE_URL = `http://localhost:8080/api/v1/mail`;

const type = {
  in: 'incoming',
  out: 'outgoing',
};

const sortByTypes = {
  ALL: 'all',
  READ: 'read',
  UNREAD: 'unread',
};

async function deleteMail(id, navigate, resultDispatch, additionalInfo = '') {
  const API_ENDPOINT = `${BASE_API_URL}/mail/${id}`;

  try {
    const response = await axios.delete(API_ENDPOINT, headersWithToken());
    resultDispatch({
      type: 'SUCCESS',
      payload: {
        heading: 'Letter deleted',
        message: response.data.message + additionalInfo,
      },
    });
    setTimeout(() => {
      navigate('/in/mail/all');
      // window.location.reload();
    }, 2000);
  } catch (e) {
    console.log('💥💥💥' + e);
    handleBadRequest(e, resultDispatch);
  }
}

async function getMail(
  mailType,
  page,
  setMailForDisplay,
  resultDispatch,
  sortBy
) {
  const GET_MAIL_API = `${BASE_URL}/get-all-${mailType}?page=${
    page || 1
  }&sortBy=${sortBy}`;
  console.log(GET_MAIL_API);
  try {
    const response = await axios.get(GET_MAIL_API, headersWithToken());
    setMailForDisplay(response.data);
  } catch (e) {
    console.log('💥💥💥' + e);
    handleBadRequest(e, resultDispatch);
  }
}

async function getNumOfPages(mailType, setPagesTotal, sortBy) {
  const GET_MAIL_API = `${BASE_URL}/get-number-pages-${mailType}?sortBy=${sortBy}`;

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
  const [sortBy, setSortBy] = useState(sortByTypes.ALL);
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);
  const navigate = useNavigate();

  useEffect(() => {
    getNumOfPages(mailType, setPagesTotal, sortBy);
    getMail(mailType, page, setMailForDisplay, resultDispatch, sortBy);
  }, [mailType, page, sortBy, success, error]);

  useEffect(() => {
    if (mailForDisplay.length === 0 && page > 1) {
      setPage((page) => page - 1);
    }
  }, [mailForDisplay]);

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        resultDispatch={resultDispatch}
        forDisplay={forDisplay}
        errors={errors}
      />
      <div>
        <header className="header">
          <button
            className={`header__Button ${mailType === type.in ? 'active' : ''}`}
            onClick={() => {
              setMailType(type.in);
              setSortBy(sortByTypes.ALL);
            }}
          >
            Incoming mail
          </button>
          <button
            className={`header__Button ${
              mailType === type.out ? 'active' : ''
            }`}
            onClick={() => {
              setMailType(type.out);
              setSortBy(sortByTypes.ALL);
            }}
          >
            Outgoing mail
          </button>
        </header>
        <div className="optionLine">
          <div className={`sortByOption ${mailType === type.in ? '' : 'hide'}`}>
            <label>Sort mail By:</label>
            <select onChange={(e) => setSortBy(e.target.value)}>
              <option value="all">All</option>
              <option value="read">Read</option>
              <option value="unread">Unread</option>
            </select>
          </div>
          <NavLink className={styles.writeLink} to="/in/mail/write">
            <span>Write a message</span>
          </NavLink>
        </div>
        {mailForDisplay.length === 0 ? (
          <p className={styles.noMailMessage}>
            Currently, no any {mailType} mail meeting your criteria in your box
          </p>
        ) : (
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
                return (
                  <tr
                    className={letter.isUnread ? styles.unreadMail : ''}
                    key={i}
                  >
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
                        onClick={() =>
                          deleteMail(letter.id, navigate, resultDispatch)
                        }
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
                    className={`${page === 1 ? 'hide' : ''} ${styles.btnNav}`}
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
                    className={`${
                      page === pagesTotal || pagesTotal === 0 ? styles.hide : ''
                    } ${styles.btnNav}`}
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
        )}
      </div>
    </>
  );
}

export { Mail, deleteMail };
