import { useUserContext, headersWithToken } from '../../context/UserProvider';
import { useEffect, useState } from 'react';

import axios from 'axios';
import {
  reduceValidationErrors,
  useResponseResult,
  handleBadRequest,
} from '../../helpers/helpers';
import NotificationMessage from '../notificationMessage/NotificationMessage';

import styles from './Clan.module.scss';

const type = {
  all: 'all',
  my: 'my',
};

const typeSortClanBy = {
  DATE: 'DATE',
  LEVEL_ASC: 'LEVEL_ASC',
  LEVEL_DESC: 'LEVEL_DESC',
  TITLE_ASC: 'TITLE_ASC',
  TITLE_DESC: 'TITLE_DESC',
  OF_MY_LVL: 'OF_MY_LVL',
};

function Clan() {
  const { user, setUserFullInfo } = useUserContext();
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);

  const [tabType, setTabType] = useState(type.all);
  const [allClans, setAllClans] = useState([]);

  const [isCreatingClan, setIsCreatingClan] = useState(false);
  const [isUpdatingClan, setIsUpdatingClan] = useState(false);
  const [myClan, setMyClan] = useState(null);

  const [newClanTitle, setNewClanTitle] = useState('');
  const [newClanDescription, setNewClanDescription] = useState('');
  const [newClanMaxCapacity, setNewClanMaxCapacity] = useState(50);
  const [newClanMinLevel, setNewClanMinlevel] = useState(1);

  const [sortBy, setSortBy] = useState(typeSortClanBy.DATE);

  useEffect(() => {
    getAllClans();
  }, [user]);

  useEffect(() => {
    let endPoint = '';
    switch (sortBy) {
      case typeSortClanBy.LEVEL_ASC:
        endPoint = '/sort-level-asc';
        break;
      case typeSortClanBy.LEVEL_DESC:
        endPoint = '/sort-level-desc';
        break;
      case typeSortClanBy.TITLE_ASC:
        endPoint = '/sort-title-asc';
        break;
      case typeSortClanBy.TITLE_DESC:
        endPoint = '/sort-title-desc';
        break;
      case typeSortClanBy.OF_MY_LVL:
        endPoint = '/minlevel/' + user.level;
        break;
      default:
        endPoint = ''; // for safety
        break;
    }

    getAllClans(endPoint);
  }, [sortBy]);

  const getAllClans = async (endpoint = '') => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/clans${endpoint}`;
    try {
      const response = await axios.get(API_ENDPOINT, headersWithToken());
      setAllClans(response.data);
    } catch (e) {
      setAllClans([]);
      handleBadRequest(e, resultDispatch);
    }
    await getMyClan();
  };

  const getMyClan = async () => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/clans/show/${user.clanId}`;

    if (user.clanId == null) {
      setMyClan(null);
      return;
    }

    try {
      const response = await axios.get(API_ENDPOINT, headersWithToken());
      console.log(response.data);
      setMyClan(response.data);
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const createNewClan = async () => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/clans/create`;

    try {
      const response = await axios.post(
        API_ENDPOINT,
        {
          title: newClanTitle,
          description: newClanDescription,
          maxCapacity: newClanMaxCapacity,
          minPlayerLevel: newClanMinLevel,
        },
        headersWithToken()
      );
      setIsCreatingClan(false);
      nullifyClanInputs();
      rerenderStatsAfterDb();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const rerenderStatsAfterDb = () => {
    setUserFullInfo();
  };

  const handleTitleChange = (event) => {
    setNewClanTitle(event.target.value);
  };

  const handleDescriptionChange = (event) => {
    setNewClanDescription(event.target.value);
  };

  const handleMaxCapacityChange = (event) => {
    setNewClanMaxCapacity(event.target.value);
  };

  const handleMinLevelChange = (event) => {
    setNewClanMinlevel(event.target.value);
  };

  const onStartUpdateClan = () => {
    setIsUpdatingClan(true);
    setNewClanDescription(myClan.description);
    setNewClanMinlevel(myClan.minPlayerLevel);
  };

  const onFinishUpdateClan = async () => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/clans/update`;

    try {
      const response = await axios.put(
        API_ENDPOINT,
        {
          title: myClan.title,
          description: newClanDescription,
          maxCapacity: myClan.maxCapacity,
          minPlayerLevel: newClanMinLevel,
        },
        headersWithToken()
      );
      setIsUpdatingClan(false);
      nullifyClanInputs();
      rerenderStatsAfterDb();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const onCancelUpdateClan = () => {
    setIsUpdatingClan(false);
  };

  const onDeleteClan = async () => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/clans/delete`;

    try {
      const response = await axios.delete(API_ENDPOINT, headersWithToken());
      rerenderStatsAfterDb();
      nullifyClanInputs();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const onExitClan = async () => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/player/exit_clan`;

    try {
      const response = await axios.post(API_ENDPOINT, null, headersWithToken());
      rerenderStatsAfterDb();
      nullifyClanInputs();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const onJoinClan = async (clan) => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/player/join_clan/${clan.id}`;

    try {
      const response = await axios.post(API_ENDPOINT, null, headersWithToken());
      rerenderStatsAfterDb();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const onKickPlayerFromClan = async (playerId) => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/clans/kick_player/${playerId}`;

    try {
      const response = await axios.post(API_ENDPOINT, null, headersWithToken());
      rerenderStatsAfterDb();
      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const nullifyClanInputs = () => {
    setNewClanMinlevel(1);
    setNewClanMaxCapacity(50);
    setNewClanTitle('');
    setNewClanDescription('');
  };

  return (
    <>
      <div className={styles.container}>
        <NotificationMessage
          error={error}
          success={success}
          forDisplay={forDisplay}
          errors={errors}
          resultDispatch={resultDispatch}
        />

        <div className={styles.btns}>
          <button
            className={`btn ${
              tabType === type.all ? 'brown-btn' : 'brown-btn--reversed'
            }`}
            onClick={() => {
              setTabType(type.all);
            }}
          >
            All clans
          </button>

          <button
            className={`btn ${
              tabType === type.my ? 'brown-btn' : 'brown-btn--reversed'
            }`}
            onClick={() => {
              setTabType(type.my);
            }}
          >
            My clan
          </button>
        </div>

        {tabType === type.all && (
          <div className={`${styles.all} ${styles.all}`}>
            <div className={styles.sorterPanel}>
              <label>Sort mail By:</label>
              <select
                disabled={allClans.length === 0}
                className="input"
                onChange={(e) => setSortBy(e.target.value)}
              >
                <option value="DATE">Oldest</option>
                <option value="LEVEL_ASC">Smaller level</option>
                <option value="LEVEL_DESC">Greater level</option>
                <option value="TITLE_ASC">By title</option>
                <option value="TITLE_DESC">By title descending</option>
                <option value="OF_MY_LVL">Of my level</option>
              </select>
            </div>

            {allClans.length === 0 && (
              <div className={`${styles.message} ${styles.bg}`}>
                No clans were created in the game yet... Be the first clan head!
              </div>
            )}
            {allClans.map((clan, id) => {
              return (
                <div key={id} className={`${styles.list} ${styles.bg}`}>
                  <div>{clan.title}</div>
                  <div>{clan.dinoType}</div>
                  <div>{clan.minPlayerLevel}</div>
                  <div>{clan.admin}</div>
                  <div>
                    {clan.amountOfPlayers}/{clan.maxCapacity}
                  </div>
                  <button
                    className="btn brown-btn"
                    onClick={() => onJoinClan(clan)}
                  >
                    Join
                  </button>
                </div>
              );
            })}
          </div>
        )}

        {tabType === type.my && (
          <div>
            <div>
              {(isCreatingClan || isUpdatingClan) && (
                <div>
                  <form className={`${styles.form} ${styles.bg}`}>
                    {isCreatingClan && (
                      <>
                        <label>Clan Title:</label>
                        <input
                          className="input"
                          type="text"
                          value={newClanTitle}
                          onChange={handleTitleChange}
                        />
                      </>
                    )}

                    <label>Clan Description:</label>
                    <input
                      className="input"
                      type="text"
                      value={newClanDescription}
                      onChange={handleDescriptionChange}
                    />

                    {isCreatingClan && (
                      <>
                        <label>Max Capacity:</label>
                        <input
                          className="input"
                          type="number"
                          value={newClanMaxCapacity}
                          onChange={handleMaxCapacityChange}
                        />
                      </>
                    )}
                    <label>Min Level:</label>
                    <input
                      className="input"
                      type="number"
                      value={newClanMinLevel}
                      onChange={handleMinLevelChange}
                    />
                  </form>
                  {isCreatingClan && (
                    <div className={styles.btns}>
                      <button className="btn brown-btn" onClick={createNewClan}>
                        Create
                      </button>
                      <button
                        className="btn brown-btn--reversed"
                        onClick={() => setIsCreatingClan(false)}
                      >
                        Go back
                      </button>
                    </div>
                  )}
                </div>
              )}
              {!(isCreatingClan || isUpdatingClan) && !myClan && (
                <div className={`${styles.message} ${styles.bg}`}>
                  <div>
                    You have joined no clan yet. You can create your own clan or
                    join existing one in "All clans" section
                  </div>
                  <button
                    className="btn brown-btn"
                    onClick={() => setIsCreatingClan(true)}
                  >
                    Create my own clan
                  </button>
                </div>
              )}
            </div>
            {myClan && (
              <div className={styles.clanMain}>
                <div>
                  <span className={styles.miniHeader}>title:</span>{' '}
                  {myClan.title}
                </div>
                <div>
                  <span className={styles.miniHeader}>description:</span>{' '}
                  {myClan.description}
                </div>
                <div>
                  <span className={styles.miniHeader}>Dino-type:</span>{' '}
                  {myClan.dinoType}
                </div>
                {/*admin panel*/}
                {user.id === myClan.adminId && (
                  <div className={styles.admin}>
                    <button className="btn brown-btn" onClick={onDeleteClan}>
                      Delete clan
                    </button>
                    {!isUpdatingClan ? (
                      <button
                        className="btn brown-btn"
                        onClick={onStartUpdateClan}
                      >
                        Edit clan
                      </button>
                    ) : (
                      <div className={styles.openMenu}>
                        <button
                          className="btn brown-btn"
                          onClick={onFinishUpdateClan}
                        >
                          Save changes
                        </button>
                        <button
                          style={{ marginLeft: '1rem' }}
                          className="btn brown-btn"
                          onClick={onCancelUpdateClan}
                        >
                          Cancel
                        </button>
                      </div>
                    )}
                  </div>
                )}

                {myClan.players.map((player, id) => {
                  // players list
                  const username = myClan.usernames[id];
                  return (
                    <div
                      key={id}
                      className={`${styles.list} ${styles.bg} ${styles.members}`}
                    >
                      <div>{username}</div>
                      <div>{player.level}</div>
                      {myClan.adminId === player.id && <div>LEADER</div>}
                      {user.id === myClan.adminId && (
                        <button
                          className="btn brown-btn"
                          onClick={() => onKickPlayerFromClan(player.id)}
                        >
                          Kick from clan
                        </button>
                      )}
                    </div>
                  );
                })}

                <div className={styles.message}>
                  <button className="btn brown-btn" onClick={onExitClan}>
                    Exit clan
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </>
  );
}

export default Clan;
