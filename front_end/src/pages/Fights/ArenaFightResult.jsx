import styles from './ArenaFightResult.module.scss';
import { BASE_API_URL, handleBadRequest } from '../../helpers/helpers';
import {
  useNavigate,
  useOutlet,
  useOutletContext,
  useParams,
} from 'react-router-dom';
import axios from 'axios';
import { headersWithToken, useUserContext } from '../../context/UserProvider';
import { useEffect, useState } from 'react';
import LoadingSpinner from '../../components/Spinner/LoadingSpinner';

async function attack(defenderId, resultDispatch, setResult, setUserFullInfo) {
  resultDispatch({ type: 'IS_LOADING' });
  const API_ENDPOINT = `${BASE_API_URL}/combat/attack/one/${defenderId}`;
  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    setResult(response.data);
    setUserFullInfo();
    resultDispatch({ type: 'CLOSE' });
  } catch (e) {
    console.log('💥💥💥 check ArenaFightReesult.attack' + e);
    handleBadRequest(e, resultDispatch);
  }
}

const fightResult = {
  win: 'win',
  loss: 'loss',
};

function ArenaFightResult() {
  const { isLoading, resultDispatch, isOnJob, isOnAttackCoolDown } =
    useOutletContext();
  const { id: attackOnId } = useParams();
  const [result, setResult] = useState(null);
  const { setUserFullInfo, user } = useUserContext();
  const canAttack = !isOnJob && !isOnAttackCoolDown;
  const won = result?.winner === user?.username;
  const navigate = useNavigate();

  useEffect(() => {
    if (!canAttack) {
      resultDispatch({
        type: 'ERROR',
        payload: {
          heading: 'Action forbidden',
          message: 'You can not attack yet!',
        },
      });
      setTimeout(() => {
        navigate('/in/arena');
      }, 4000);
    } else {
      attack(attackOnId, resultDispatch, setResult, setUserFullInfo);
    }
  }, []);

  return (
    <>
      {isLoading || !result ? (
        <p className={styles.isLoading}>
          <LoadingSpinner width={40} height={40} />
          Wait a second please.. We are proccessing your request.......
        </p>
      ) : (
        <div className={styles.container}>
          <img
            className={`${styles.resultLogo} ${
              won ? styles.wonOutline : styles.lostOutline
            }`}
            src={`${
              result.winner === user.username ? '/win.jpg' : '/loss.jpeg'
            }`}
            alt="Victory/loss logo"
          />
          <div
            className={`${styles.results} ${won ? styles.won : styles.loss}`}
          >
            <h2 className={styles.results__Heading}>
              {won ? 'Congrats, you won!' : 'Unfortunately, you lost..'}
            </h2>

            <p>
              {won ? 'You have earned ' : 'You have lost '}
              {Math.abs(result.winnerCurrencyChange)} currency
            </p>
            {won && (
              <p>You have gained {Math.abs(result.winnerExpReward)} XP</p>
            )}
          </div>
        </div>
      )}
    </>
  );
}

export default ArenaFightResult;
