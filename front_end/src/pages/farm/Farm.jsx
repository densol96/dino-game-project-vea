import { useNavigate } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import {useEffect, useRef, useState} from 'react';
import axios from "axios";
import * as _ from "lodash";
import {reduceValidationErrors, useResponseResult} from "../../helpers/helpers";
import NotificationMessage from "../notificationMessage/NotificationMessage";

function Farm() {
    const { user, setUserFullInfo } = useUserContext();
    const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
    const errors = reduceValidationErrors(error.errors);

    const [timeString, setTimeString] = useState("");
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
            refJobInterval.current = setInterval(()=> {
                const dateNow =  new Date();
                const ms = workingUntilDate.getTime() - dateNow.getTime();
                if (ms < 0) {
                    clearInterval(refJobInterval.current);
                    refJobInterval.current = null;
                    setTimeString("");
                    finishJob();
                } else setTimeString(getFormattedTimeFromMs(ms));
            }, 1000);
        }
    }

    const getFormattedTimeFromMs = (ms) => { // provide only new Date().getTime() since it doesnt consider timezone
        let result = '';
        let seconds = Math.floor(ms / 1000);
        let minutes = Math.floor(seconds / 60);
        let hours = Math.floor(minutes / 60);
        let secondsStr = _.padStart((seconds % 60).toFixed(), 2, '0');
        let minutesStr = _.padStart((minutes % 60).toFixed(), 2, '0');
        hours = hours % 24;
        if(hours) {
            result = `${hours}:${minutesStr}:${secondsStr}`;
        }else {
            result = `${minutesStr}:${secondsStr}`;
        }
        return result;
    }

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


    const onStartShortFarm = () => {
        postStartJob(SHORT_JOB_DURATION, SHORT_JOB_REWARD);
    }

    const onStartLongFarm = () => {
        postStartJob(LONG_JOB_DURATION, LONG_JOB_REWARD);
    }

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

    if (isOnJob) startInterval();

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
                {isOnJob && <div>currently on job {timeString}</div>}
                {!isOnJob && <div>
                    <button onClick={onStartShortFarm}>Start short farm</button>
                    <button onClick={onStartLongFarm}>Start long farm</button>
                </div>}

            </div>
        </>
    );
}

export default Farm;
