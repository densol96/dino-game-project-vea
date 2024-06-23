import { useNavigate } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import * as _ from 'lodash';
import {
  reduceValidationErrors,
  useResponseResult,
  handleBadRequest,
} from '../../helpers/helpers';
import NotificationMessage from '../notificationMessage/NotificationMessage';
import styles from './Farm.module.scss';
import LocationDescription from '../../components/LocationDescription';

function Farm() {
  const { user, setUserFullInfo } = useUserContext();
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);

  const [timeString, setTimeString] = useState('');
  const refJobInterval = useRef(null);

  const workingUntilDate = new Date(user.workingUntil);
  const currentDate = new Date();

  const isOnJob = workingUntilDate >= currentDate;

  const SHORT_JOB_DURATION = 1;
  const LONG_JOB_DURATION = 12;

  const SHORT_JOB_REWARD = 10;
  const LONG_JOB_REWARD = 40;

  const startInterval = () => {
    if (!refJobInterval.current) {
      refJobInterval.current = setInterval(() => {
        const dateNow = new Date();
        const ms = workingUntilDate.getTime() - dateNow.getTime();
        if (ms < 0) {
          clearInterval(refJobInterval.current);
          refJobInterval.current = null;
          setTimeString('');
          finishJob();
        } else setTimeString(getFormattedTimeFromMs(ms));
      }, 1000);
    }
  };

  const getFormattedTimeFromMs = (ms) => {
    // provide only new Date().getTime() since it doesnt consider timezone
    let result = '';
    let seconds = Math.floor(ms / 1000);
    let minutes = Math.floor(seconds / 60);
    let hours = Math.floor(minutes / 60);
    let secondsStr = _.padStart((seconds % 60).toFixed(), 2, '0');
    let minutesStr = _.padStart((minutes % 60).toFixed(), 2, '0');
    hours = hours % 24;
    if (hours) {
      result = `${hours}:${minutesStr}:${secondsStr}`;
    } else {
      result = `${minutesStr}:${secondsStr}`;
    }
    return result;
  };

  const finishJob = async () => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/job/finish/${user.id}`;

    try {
      const response = await axios.post(API_ENDPOINT);
      setUserFullInfo();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const onStartShortFarm = () => {
    postStartJob(SHORT_JOB_DURATION, SHORT_JOB_REWARD);
  };

  const onStartLongFarm = () => {
    postStartJob(LONG_JOB_DURATION, LONG_JOB_REWARD);
  };

  const postStartJob = async (duration, reward) => {
    const API_ENDPOINT = 'http://localhost:8080/api/v1/job/start';

    try {
      const response = await axios.post(API_ENDPOINT, {
        playerId: user.id,
        hoursDuration: duration,
        rewardCurrency: reward,
      });
      setUserFullInfo();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  if (isOnJob) startInterval();

  const description = (
    <>
      <span>
        The farm is the easiest way to earn currency! No need to fight anyone –
        just peaceful, productive work. You have the choice between a short or
        long contract:
      </span>
      <ul>
        <li>
          <span className="bold">Short Contract (1 hour):</span> Engage in tasks
          like feeding animals or harvesting crops. A quick way to earn some
          resources and experience.
        </li>
        <li>
          <span className="bold">Long Contract (12 hours):</span> Take on more
          substantial projects such as fieldwork or construction. The rewards
          are much greater!
        </li>
      </ul>
      <span>
        It's simple: the longer you work, the more you earn. Contribute to the
        farm and watch your efforts pay off!
      </span>
    </>
  );

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        forDisplay={forDisplay}
        errors={errors}
        resultDispatch={resultDispatch}
      />
      <main className={styles.container}>
        <LocationDescription
          localStorageName="farmDescriptionOpen"
          heading="Welcome to the dinosaur farm"
          src="/farm.jpg"
          alt="Image of the dinosaur farm"
          description={description}
        />
        {isOnJob && (
          <div className={styles.timerMessage}>
            <ion-icon id={styles.icon} name="hourglass-outline"></ion-icon>
            <span>
              You are currently working on the farm! Your shift ends in{' '}
              {timeString}
            </span>
          </div>
        )}
        {!isOnJob && (
          <div className={styles.action}>
            <button className="btn brown-btn" onClick={onStartShortFarm}>
              Start short farm
            </button>
            <button className="btn brown-btn" onClick={onStartLongFarm}>
              Start long farm
            </button>
          </div>
        )}
      </main>
    </>
  );
}

export default Farm;
