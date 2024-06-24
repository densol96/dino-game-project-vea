import { useNavigate, NavLink } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import { useEffect, useState } from 'react';
import { useNewMessagesContext } from '../../context/NewMessagesProvider';
import { Outlet } from 'react-router-dom';

import axios from 'axios';
import {
  reduceValidationErrors,
  useResponseResult,
  handleBadRequest,
} from '../../helpers/helpers';
import NotificationMessage from '../notificationMessage/NotificationMessage';

function Arena() {
  const { user, setUserFullInfo } = useUserContext();
  const [{ success, error, forDisplay, idLoading }, resultDispatch] =
    useResponseResult();
  const errors = reduceValidationErrors(error.errors);

  const [combat, setCombat] = useState(null);

  const workingUntilDate = new Date(user.workingUntil);
  const cannotAttackAgainUntilDate = new Date(user.cannotAttackAgainUntil);
  const currentDate = new Date();

  const isOnJob = workingUntilDate >= currentDate;
  const isOnAttackCoolDown = cannotAttackAgainUntilDate >= currentDate;

  const { checkIfNewMessages } = useNewMessagesContext();

  const rerenderStatsAfterDb = () => {
    setUserFullInfo();
  };

  useEffect(() => {
    checkIfNewMessages();
    rerenderStatsAfterDb();
  }, []);

  console.log('I am here');

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        forDisplay={forDisplay}
        errors={errors}
        resultDispatch={resultDispatch}
      />
      <Outlet
        context={{
          isOnJob,
          isOnAttackCoolDown,
          resultDispatch,
          user,
          setUserFullInfo,
          idLoading,
        }}
      />
    </>
  );
}

export default Arena;
