import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { UserProvider } from './context/UserProvider';
import Home from './pages/home/Home';
import Profile from './pages/profile/Profile';
import ModalLogin from './pages/home/login/ModalLogin.jsx/ModalLogin';

function App() {
  return (
    <UserProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />}>
            <Route path="login" element={<ModalLogin />} />
          </Route>

          <Route path="/profile" element={<Profile />} />
        </Routes>
      </BrowserRouter>
    </UserProvider>
  );
}

export default App;
