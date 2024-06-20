import { useNavigate } from 'react-router-dom';
import { useUserContext } from '../../context/UserProvider';
import { useEffect } from 'react';
function Profile() {
  const { user } = useUserContext();
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate('/');
    }
  }, []);

  return (
    <div style={{ fontSize: '5rem', backgroundColor: 'white' }}>
      Welcome to DinoConflict, <b>{user}</b>
    </div>
  );
}

export default Profile;
