import styles from './Ratings.module.scss';
import { useEffect, useState } from 'react';
import {
  useResponseResult,
  reduceValidationErrors,
  BASE_API_URL,
  handleBadRequest,
  capitalize,
} from '../../helpers/helpers';
import axios from 'axios';
import { headersWithToken } from '../../context/UserProvider';
import Pagination from '../../components/Pagination';
import NotificationMessage from '../notificationMessage/NotificationMessage';

const dioTypeFilterOptions = {
  ALL: 'all',
  CARNIVORE: 'carnivore',
  HERBIVORE: 'herbivore',
};

const sortDirectionsOptions = {
  ASC: 'asc',
  DESC: 'desc',
};

const sortByOptions = {
  EXPERIENCE: 'experience',
  STOLEN: 'stolen',
};

async function getNumOfPages(dinoType, setPagesTotal, resultDispatch) {
  const API_ENDPOINT = `${BASE_API_URL}/ratings/pages-total?dinoType=${dinoType}`;
  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    console.log(response.data);
    setPagesTotal(+response.data);
  } catch (e) {
    handleBadRequest(e, resultDispatch);
  }
}

async function getRatings(
  page,
  sortBy,
  dinoType,
  sortDirection,
  resultDispatch,
  setPlayersForDisplay
) {
  resultDispatch({ type: 'IS_LOADING' });
  const API_ENDPOINT = `${BASE_API_URL}/ratings/players?page=${page}&sortBy=${sortBy}&dinoType=${dinoType}&sortDirection=${sortDirection}`;
  try {
    const response = await axios.get(API_ENDPOINT, headersWithToken());
    setPlayersForDisplay(response.data);
    console.log(response.data);
  } catch (e) {
    handleBadRequest(e, resultDispatch);
  }
}

function calculateWinrate(total, won) {
  return total === 0 ? 'Not fought' : `${Math.round((won * 100) / total)}%`;
}

function PlayerRatings({ resultDispatch }) {
  const [page, setPage] = useState(1);
  const [pagesTotal, setPagesTotal] = useState(1);

  const [playersForDisplay, setPlayersForDisplay] = useState([]);

  const [sortBy, setSortBy] = useState(sortByOptions.EXPERIENCE);
  const [dinoType, setDinoType] = useState(dioTypeFilterOptions.ALL);
  const [sortDirection, setSortDirection] = useState(
    sortDirectionsOptions.DESC
  );

  useEffect(() => {
    getNumOfPages(dinoType, setPagesTotal, resultDispatch);
    getRatings(
      page,
      sortBy,
      dinoType,
      sortDirection,
      resultDispatch,
      setPlayersForDisplay
    );
  }, [page, sortBy, dinoType, sortDirection]);

  return (
    <>
      <div className={styles.optionLine}>
        <div className={styles.sortByOption}>
          <label>Sort by:</label>
          <select onChange={(e) => setSortBy(e.target.value)}>
            <option value="experience">Experience</option>
            <option value="stolen">Currency stolen</option>
          </select>
          <select onChange={(e) => setSortDirection(e.target.value)}>
            <option value="desc">Best</option>
            <option value="asc">Worst</option>
          </select>
        </div>
        <div className={styles.sortByType}>
          <label>Type:</label>
          <select onChange={(e) => setDinoType(e.target.value)}>
            <option value="all">All</option>
            <option value="carnivore">Carnivore</option>
            <option value="herbivore">Herbivore</option>
          </select>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <th>Username</th>
            <th>Type</th>
            <th>Experience</th>
            <th>Total fights</th>
            <th>Total won</th>
            <th>Win-rate</th>
          </tr>
        </thead>
        <tbody>
          {playersForDisplay?.map((player) => {
            return (
              <tr>
                <td>{player.username}</td>
                <td>{capitalize(player.type)}</td>
                <td>{player.experience}</td>
                <td>{player.totalFights}</td>
                <td>{player.fightsWon}</td>
                <td>
                  {calculateWinrate(player.totalFights, player.fightsWon)}
                </td>
              </tr>
            );
          })}
        </tbody>
        <div className="paginationHolder">
          <Pagination
            styles={styles}
            page={page}
            pagesTotal={pagesTotal}
            setPage={setPage}
          />
        </div>
      </table>
    </>
  );
}

export default PlayerRatings;
