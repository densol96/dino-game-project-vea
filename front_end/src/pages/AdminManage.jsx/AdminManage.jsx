import styles from './AdminManage.module.scss';
import NotificationMessage from '../notificationMessage/NotificationMessage';
import {
  useResponseResult,
  reduceValidationErrors,
} from '../../helpers/helpers';
import { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';

async function change() {}

function AdminManage() {
  const [
    { success, error, forDisplay, isLoading, buttonClickable },
    resultDispatch,
  ] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);
  const clickedButton = useRef(undefined);

  const { id } = useParams();

  const [description, setDescription] = useState();
  const [email, setEmail] = useState();
  const [username, setUsername] = useState();
  const [newPassword, setNewPassword] = useState();
  const [confirmPassword, setConfirmPassword] = useState();

  useEffect(() => {});

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        forDisplay={forDisplay}
        errors={errors}
        resultDispatch={resultDispatch}
      />
      {/* <div className={styles.container}>
        <div className={styles.descriptionSettings}>
          <h2 className={styles.heading}>Change user description</h2>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              change('description', { description }, resultDispatch, false);
            }}
          >
            <textarea
              className={`${styles.textarea} input`}
              name="user-description"
              id="user-description"
              rows="5"
              cols="33"
              placeholder={
                user.description
                  ? user.description
                  : 'Currently, your user info is empty..'
              }
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            ></textarea>
            <button className="btn brown-btn">Change description</button>
          </form>
        </div>
        <div className={styles.emailSettings}>
          <h2 className={styles.heading}>Change email</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change('email', { email }, resultDispatch, false);
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="New email"
              onChange={(e) => setEmail(e.target.value)}
              value={email}
            />
            <button className="btn brown-btn">Change email</button>
          </form>
        </div>
        <div className={styles.usernameSettings}>
          <h2 className={styles.heading}>Change username</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'username',
                { username },
                resultDispatch,
                setUserFullInfo,
                setUsernameIsLoading,
                toLogout
              );
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="New username"
              onChange={(e) => setUsername(e.target.value)}
              value={username}
            />
            <button className="btn brown-btn">Change username</button>
          </form>
        </div>
        <div className={styles.passwordSettings}>
          <h2 className={styles.heading}>Change password</h2>
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              await change(
                'password',
                { newPassword, oldPassword, confirmPassword },
                resultDispatch
              );
            }}
          >
            <input
              type="text"
              className="input"
              placeholder="Old password"
              onChange={(e) => setOldPassword(e.target.value)}
              value={oldPassword}
            />
            <input
              type="text"
              className="input"
              placeholder="New password"
              onChange={(e) => setNewPassword(e.target.value)}
              value={newPassword}
            />
            <input
              type="text"
              className="input"
              placeholder="Confirm new password"
              onChange={(e) => setConfirmPassword(e.target.value)}
              value={confirmPassword}
            />
            <button className="btn brown-btn">Change pasword</button>
          </form>
        </div>
      </div> */}
    </>
  );
}

export default AdminManage;
