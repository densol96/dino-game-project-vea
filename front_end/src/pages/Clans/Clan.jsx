import { useUserContext, headersWithToken } from '../../context/UserProvider';
import {useEffect, useState} from 'react';

import axios from "axios";
import {reduceValidationErrors, useResponseResult} from "../../helpers/helpers";
import NotificationMessage from "../notificationMessage/NotificationMessage";


const type = {
    all: 'all',
    my: 'my',
};

function Clan() {
    const { user, setUserFullInfo } = useUserContext();
    const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
    const errors = reduceValidationErrors(error.errors);


    const [tabType, setTabType] = useState(type.my);
    const [allClans, setAllClans] = useState([]);

    const [isCreatingClan, setIsCreatingClan] = useState(false);
    const [myClan, setMyClan] = useState(null);

    const [newClanTitle, setNewClanTitle] = useState("");
    const [newClanDescription, setNewClanDescription] = useState("");
    const [newClanMaxCapacity, setNewClanMaxCapacity] = useState(50);
    const [newClanMinLevel, setNewClanMinlevel] = useState(1);

    useEffect(() => {
        getAllClans();
        getMyClan();
    }, [user]);

    const getAllClans = async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/clans`;

        try {
            const response = await axios.get(API_ENDPOINT, headersWithToken());
            // const allClans = response.data;
            // console.log("id: ",  user.clanId);
            // if (user.clanId != null) {
            //     const myClan = allClans.find(clan => clan.id === user.clanId);
            //     console.log(myClan);
            //     setMyClan(myClan);
            // }
            setAllClans(response.data);

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

    const getMyClan = async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/clans/show/${user.clanId}`;

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
            console.log(response);
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
                    {allClans.map((clan, id) => {
                        console.log(clan);
                        return <div key={id} style={{ display: 'flex', flexDirection: 'row' }}>
                            <div>{clan.title}</div>
                            <div>{clan.description}</div>
                            <div>{clan.dinoType}</div>
                            <div>{clan.admin}</div>
                            <div>{clan.amountOfPlayer}/{clan.maxCapacity}</div>

                        </div>
                    })}
                </div>}

                {tabType === type.my && <div>
                    {!myClan && <div>
                        {isCreatingClan ? <div>
                            <form>
                                <div>
                                    <label>Clan Title:</label>
                                    <input
                                        type="text"
                                        value={newClanTitle}
                                        onChange={handleTitleChange}
                                    />
                                </div>
                                <div>
                                    <label>Clan Description:</label>
                                    <input
                                        type="text"
                                        value={newClanDescription}
                                        onChange={handleDescriptionChange}
                                    />
                                </div>
                                <div>
                                    <label>Max Capacity:</label>
                                    <input
                                        type="number"
                                        value={newClanMaxCapacity}
                                        onChange={handleMaxCapacityChange}
                                    />
                                </div>
                                <div>
                                    <label>Min Level:</label>
                                    <input
                                        type="number"
                                        value={newClanMinLevel}
                                        onChange={handleMinLevelChange}
                                    />
                                </div>
                            </form>
                            <button onClick={createNewClan}>Create</button>
                        </div>
                        :
                        <div>
                            <div>You have joined no clan yet. You can create your own clan or join existing one in "All clans" section</div>
                            <button onClick={() => setIsCreatingClan(true)}>Create my own clan</button>
                        </div>}
                    </div> }
                    {myClan && <div>
                        {myClan.title}
                        {myClan.players.map((player, id) => {
                            console.log(player);
                            return <div key={id}>

                            </div>
                        })}
                    </div>}
                </div>}

            </div>

        </>
    );
}

export default Clan;
