import { useNavigate } from 'react-router-dom';
import { headersWithToken, useUserContext } from '../../context/UserProvider';
import { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import {
  reduceValidationErrors,
  useResponseResult,
  handleBadRequest,
} from '../../helpers/helpers';
import NotificationMessage from '../notificationMessage/NotificationMessage';

function Friends() {
  const { user, setUserFullInfo } = useUserContext();
  const [{ success, error, forDisplay }, resultDispatch] = useResponseResult();
  const errors = reduceValidationErrors(error.errors);

  const [friends, setFriends] = useState([]);
  const [friendsRequests, setFriendsRequests] = useState([]);

  useEffect(() => {
    // getFriends();
  }, []);

  const getFriends = async () => {
    const API_ENDPOINT_1 = `http://localhost:8080/api/v1/friends/friends`;

    try {
      const response = await axios.get(API_ENDPOINT_1, headersWithToken());

      const friends = response.data;
      console.log('friends', friends);
      setFriends(friends);

      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }

    const API_ENDPOINT_2 = `http://localhost:8080/api/v1/friends/pending`;

    try {
      const response = await axios.get(API_ENDPOINT_2, headersWithToken());

      const friends = response.data;
      const sentToMe = friends.filter((friend) => friend.friendId === user.id);
      console.log('sentToMe', sentToMe);
      setFriendsRequests(sentToMe);

      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const onAcceptFriendship = async (playerId) => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/friends/accept/${playerId}`;

    try {
      const response = await axios.post(API_ENDPOINT, null, headersWithToken());

      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });

      getFriends();
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  const onRejectFriendship = async (playerId) => {
    const API_ENDPOINT = `http://localhost:8080/api/v1/friends/reject/${playerId}`;

    try {
      const response = await axios.post(API_ENDPOINT, null, headersWithToken());

      resultDispatch({
        type: 'SUCCESS',
        payload: { heading: 'Success', message: response.data.message },
      });

      getFriends();
    } catch (e) {
      handleBadRequest(e, resultDispatch);
    }
  };

  return (
    <>
      <NotificationMessage
        error={error}
        success={success}
        forDisplay={forDisplay}
        errors={errors}
        resultDispatch={resultDispatch}
      />

      <div>
        <div>Friend requests sent to you</div>
        {friendsRequests.map((friendsRequest, idx) => {
          console.log(friendsRequest);
          return (
            <div key={idx} style={{ display: 'flex', flexDirection: 'row' }}>
              <div>{friendsRequest.username1}</div>
              <div>{friendsRequest.level1}</div>
              <div>{friendsRequest.dinoType1}</div>
            </div>
          );
        })}
        {friends.map((friend, idx) => {
          console.log(friend);
          return (
            <div key={idx} style={{ display: 'flex', flexDirection: 'row' }}>
              <div>{friend.username1}</div>
              <div>{friend.level1}</div>
              <div>{friend.dinoType1}</div>
            </div>
          );
        })}
      </div>
    </>
  );
}

export default Friends;
