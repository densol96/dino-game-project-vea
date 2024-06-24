import { useUserContext, headersWithToken } from '../../context/UserProvider';
import {useEffect, useState} from 'react';

import axios from "axios";
import {reduceValidationErrors, useResponseResult} from "../../helpers/helpers";
import NotificationMessage from "../notificationMessage/NotificationMessage";


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


    const [tabType, setTabType] = useState(type.my);
    const [allClans, setAllClans] = useState([]);

    const [isCreatingClan, setIsCreatingClan] = useState(false);
    const [isUpdatingClan, setIsUpdatingClan] = useState(false);
    const [myClan, setMyClan] = useState(null);

    const [newClanTitle, setNewClanTitle] = useState("");
    const [newClanDescription, setNewClanDescription] = useState("");
    const [newClanMaxCapacity, setNewClanMaxCapacity] = useState(50);
    const [newClanMinLevel, setNewClanMinlevel] = useState(1);

    const [sortBy, setSortBy] = useState(typeSortClanBy.DATE);


    useEffect(() => {
        console.log(user);
        getAllClans();
    }, [user]);

    useEffect(() => {
        let endPoint = "";
        switch (sortBy) {
            case typeSortClanBy.LEVEL_ASC:
                endPoint = "/sort-level-asc";
                break;
            case typeSortClanBy.LEVEL_DESC:
                endPoint = "/sort-level-desc";
                break;
            case typeSortClanBy.TITLE_ASC:
                endPoint = "/sort-title-asc";
                break;
            case typeSortClanBy.TITLE_DESC:
                endPoint = "/sort-title-desc";
                break;
            case typeSortClanBy.OF_MY_LVL:
                endPoint = "/minlevel/" + user.level;
                break;
            default:
                endPoint = ""; // for safety
                break;
        }

        getAllClans(endPoint);
    }, [sortBy])

    const getAllClans = async (endpoint = "") => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/clans${endpoint}`;

        console.log(API_ENDPOINT);

        try {
            const response = await axios.get(API_ENDPOINT, headersWithToken());
            setAllClans(response.data);

        } catch (e) {
            setAllClans([]);
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
        await getMyClan();
    }

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
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
    }

    const createNewClan = async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/clans/create`;

        try {
            const response = await axios.post(API_ENDPOINT, {
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
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
    }


    const rerenderStatsAfterDb = () => {
        setUserFullInfo();
    }

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
    }

    const onFinishUpdateClan = async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/clans/update`;

        try {
            const response = await axios.put(API_ENDPOINT, {
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
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
    }

    const onCancelUpdateClan = () => {
        setIsUpdatingClan(false);
    }

    const onDeleteClan = async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/clans/delete`;

        try {
            const response = await axios.delete(API_ENDPOINT, headersWithToken() );
            rerenderStatsAfterDb();
            nullifyClanInputs();
            resultDispatch({
                type: 'SUCCESS',
                payload: { heading: 'Success', message: response.data.message },
            });

        } catch (e) {
            console.log(e);
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
    }

    const onExitClan = async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/player/exit_clan`;

        try {
            const response = await axios.post(API_ENDPOINT, null, headersWithToken() );
            rerenderStatsAfterDb();
            nullifyClanInputs();
            resultDispatch({
                type: 'SUCCESS',
                payload: { heading: 'Success', message: response.data.message },
            });

        } catch (e) {
            console.log(e);
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
    }


    const onJoinClan = async (clan) => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/player/join_clan/${clan.id}`;

        try {
            const response = await axios.post(API_ENDPOINT, null, headersWithToken() );
            rerenderStatsAfterDb();
            resultDispatch({
                type: 'SUCCESS',
                payload: { heading: 'Success', message: response.data.message },
            });

        } catch (e) {
            console.log(e);
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
    }

    const onKickPlayerFromClan = async (playerId) => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/clans/kick_player/${playerId}`;

        try {
            const response = await axios.post(API_ENDPOINT, null, headersWithToken() );
            rerenderStatsAfterDb();
            resultDispatch({
                type: 'SUCCESS',
                payload: { heading: 'Success', message: response.data.message },
            });

        } catch (e) {
            console.log(e);
            if (e.code === 'ERR_BAD_REQUEST') {
                const error = e.response.data;
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: error.name,
                        message: error.message,
                        type: error.type,
                        errors: error.errors,
                    },
                });
            } else if (e.code === 'ERR_NETWORK') {
                resultDispatch({
                    type: 'ERROR',
                    payload: {
                        heading: 'Service is currently unavailable',
                        message:
                            'Registration is currently unavailable! Please,try again later!',
                        type: 'ERR_NETWORK',
                        errors: [],
                    },
                });
            }
        }
    }

    const nullifyClanInputs = () => {
        setNewClanMinlevel(1);
        setNewClanMaxCapacity(50);
        setNewClanTitle("");
        setNewClanDescription("");
    }

    return (
        <>
            <div style={{ display: 'flex', flexDirection: 'column' }}>
                <NotificationMessage
                    error={error}
                    success={success}
                    forDisplay={forDisplay}
                    errors={errors}
                    resultDispatch={resultDispatch}
                />

                <div>
                    <button
                        onClick={() => {
                            setTabType(type.all);
                        }}
                    >All clans</button>

                    <button
                        onClick={() => {
                            setTabType(type.my);
                        }}
                    >My clan</button>
                </div>

                {tabType === type.all && <div>
                    <label>Sort mail By:</label>
                    <select onChange={(e) => setSortBy(e.target.value)}>
                        <option value="DATE">Oldest</option>
                        <option value="LEVEL_ASC">Smaller level</option>
                        <option value="LEVEL_DESC">Greater level</option>
                        <option value="TITLE_ASC">By title</option>
                        <option value="TITLE_DESC">By title descending</option>
                        <option value="OF_MY_LVL">Of my level</option>
                    </select>
                    {allClans.length === 0 && <div>No clans yet</div>}
                    {allClans.map((clan, id) => {
                        console.log(clan);
                        return <div key={id} style={{ display: 'flex', flexDirection: 'row', gap: '10px' }}>
                            <div>{clan.title}</div>
                            <div>{clan.description}</div>
                            <div>{clan.dinoType}</div>
                            <div>{clan.minPlayerLevel}</div>
                            <div>{clan.admin}</div>
                            <div>{clan.amountOfPlayers}/{clan.maxCapacity}</div>
                            <button onClick={() => onJoinClan(clan)}>Join</button>
                        </div>
                    })}
                </div>}

                {tabType === type.my && <div>
                    <div>
                        {(isCreatingClan || isUpdatingClan) && <div>
                            <form>
                                {isCreatingClan && <div>
                                    <label>Clan Title:</label>
                                    <input
                                        type="text"
                                        value={newClanTitle}
                                        onChange={handleTitleChange}
                                    />
                                </div>}
                                <div>
                                    <label>Clan Description:</label>
                                    <input
                                        type="text"
                                        value={newClanDescription}
                                        onChange={handleDescriptionChange}
                                    />
                                </div>
                                {isCreatingClan && <div>
                                    <label>Max Capacity:</label>
                                    <input
                                        type="number"
                                        value={newClanMaxCapacity}
                                        onChange={handleMaxCapacityChange}
                                    />
                                </div>}
                                <div>
                                    <label>Min Level:</label>
                                    <input
                                        type="number"
                                        value={newClanMinLevel}
                                        onChange={handleMinLevelChange}
                                    />
                                </div>
                            </form>
                            {isCreatingClan && <button onClick={createNewClan}>Create</button>}
                        </div>}
                        {!(isCreatingClan || isUpdatingClan) && !myClan && <div>
                            <div>You have joined no clan yet. You can create your own clan or join existing one in "All clans" section</div>
                            <button onClick={() => setIsCreatingClan(true)}>Create my own clan</button>
                        </div>}
                    </div>
                    {myClan && <div>

                        <div>title: {myClan.title}</div>
                        <div>description: {myClan.description}</div>
                        <div>dinoType: {myClan.dinoType}</div>

                        {myClan.players.map((player, id) => { // players list
                            const username = myClan.usernames[id];
                            console.log(username, player);
                            return <div key={id} style={{ display: 'flex', flexDirection: 'row', gap: '10px' }}>
                                <div>{username}</div>
                                <div>{player.level}</div>
                                {myClan.adminId === player.id && <div>LEADER</div>}
                                {user.id === myClan.adminId && <button onClick={() => onKickPlayerFromClan(player.id)}>Kick from clan</button>}
                            </div>
                        })}

                        {/*admin panel*/}
                        {user.id === myClan.adminId && <div>
                            <button onClick={onDeleteClan}>Delete clan</button>
                            {!isUpdatingClan ? <button onClick={onStartUpdateClan}>Edit clan</button>
                                : <div>
                                    <button onClick={onFinishUpdateClan}>Save changes</button>
                                    <button onClick={onCancelUpdateClan}>Cancel</button>
                                </div>
                            }
                        </div>}
                        <div>
                            <button onClick={onExitClan}>Exit clan</button>
                        </div>
                    </div>}
                </div>}

            </div>

        </>
    );
}

export default Clan;
