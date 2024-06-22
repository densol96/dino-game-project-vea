import { useNavigate } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import {useEffect, useState} from 'react';
import { useNewMessagesContext } from '../../context/NewMessagesProvider';
// import { IonIcon } from '@ionic/react';


// import styles from './Combat.module.scss';
import axios from "axios";
import {reduceValidationErrors, useResponseResult} from "../../helpers/helpers";
import NotificationMessage from "../notificationMessage/NotificationMessage";

// function extractStats(obj) {
//     const arr = [];
//     for (let key in obj) {
//         arr.push(obj[key]);
//     }
//     return arr;
// }

function Combat() {
    const { user, setUserFullInfo } = useUserContext();
    const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
    const errors = reduceValidationErrors(error.errors);

    const [foundOpponent, setFoundOpponent] = useState(null);
    const [combat, setCombat] = useState(null);

    const workingUntilDate = new Date(user.workingUntil);
    const cannotAttackAgainUntilDate = new Date(user.cannotAttackAgainUntil);
    const currentDate = new Date();

    const isOnJob = workingUntilDate >= currentDate;
    const isOnAttackCoolDown = cannotAttackAgainUntilDate >= currentDate;


    const { checkIfNewMessages } = useNewMessagesContext();
    useEffect(() => {
        checkIfNewMessages();
    }, []);

    const findOpponent =  async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/combat/find/one/${user.id}`;

        try {
            const response = await axios.get(API_ENDPOINT);
            console.log(response.data);
            setFoundOpponent(response.data);
            // resultDispatch({
            //     type: 'SUCCESS',
            //     payload: { heading: 'Success', message: response.data.message },
            // });

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
    const attackOpponent =  async () => {
        if (foundOpponent) {
            const API_ENDPOINT = `http://localhost:8080/api/v1/combat/attack/one/${user.id}/${foundOpponent.id}`;

            try {
                const response = await axios.post(API_ENDPOINT);
                console.log(response.data);
                setCombat(response.data);
                rerenderStatsAfterDb();
                // resultDispatch({
                //     type: 'SUCCESS',
                //     payload: { heading: 'Success', message: response.data.message },
                // });

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
    }

    const rerenderStatsAfterDb = () => {
        setUserFullInfo();
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

                {/*for no combat*/}
                {!combat && <div>

                    {isOnJob ? <div>You cannot attack while being on farm!</div> :
                    isOnAttackCoolDown ? <div>You cannot attack while being on cool down from the last attack</div>
                    : <button onClick={findOpponent}>{foundOpponent ? "Next opponent" : "Find an opponent"}</button>}

                    {foundOpponent && <div>
                        <div>Armor: {foundOpponent.playerStats.armor}</div>
                        <div>Agility: {foundOpponent.playerStats.agility}</div>
                        <div>Damage: {foundOpponent.playerStats.damage}</div>
                        <div>Health: {foundOpponent.playerStats.health}</div>
                        <div>Critical hit: {foundOpponent.playerStats.criticalHitPercentage}</div>
                        <button onClick={attackOpponent}>Attack {foundOpponent.name}</button>
                    </div>}
                </div>}

                {combat && <div>
                    <div>{combat.combatResult.winner.id === user.id ? "You won the combat!" : "You lost the combat"}</div>
                    {combat.combatResult.winner.id === user.id && <div>
                        <div>{"Gold reward: " + combat.combatResult.winnerCurrencyChange}</div>
                        <div>{"Experience reward: " + combat.combatResult.winnerExpReward}</div>
                    </div>}
                    {combat.combatResult.loser.id === user.id && <div>
                        <div>{"Gold loss: " + (-combat.combatResult.loserCurrencyChange)}</div>
                        <div>{"Experience reward: " + combat.combatResult.loserExpReward}</div>
                    </div>}
                    <button onClick={() => {
                        setCombat(null);
                        setFoundOpponent(null);
                    }}>Close</button>
                </div>}
            </div>

        </>
    );
}

export default Combat;
