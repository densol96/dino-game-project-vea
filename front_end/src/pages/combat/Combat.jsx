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


    const { checkIfNewMessages } = useNewMessagesContext();
    useEffect(() => {
        checkIfNewMessages();
    }, []);

    const findOpponent =  async () => {
        const API_ENDPOINT = `http://localhost:8080/api/v1/combat/find/one/${user.id}`;

        try {
            const response = await axios.get(API_ENDPOINT);
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
        const API_ENDPOINT = `http://localhost:8080/api/v1/combat/find/one/${user.id}`;

        try {
            const response = await axios.get(API_ENDPOINT);
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

    return (
        <>
            <NotificationMessage
                error={error}
                success={success}
                forDisplay={forDisplay}
                errors={errors}
                resultDispatch={resultDispatch}
            />
            <div style={{
                flex: 1,
                alignItems: "center",
                justifyContent: "center",
            }}>
                <button onClick={findOpponent}>Find an opponent</button>
                {foundOpponent && <button onClick={attackOpponent}>Attack {foundOpponent.name}</button>}
            </div>
        </>
    );
}

export default Combat;
