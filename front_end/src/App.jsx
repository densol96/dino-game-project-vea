import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import { UserProvider } from './context/UserProvider';
import Home from './pages/home/Home';
import LoggedIn from './pages/logged/LoggedIn';
import ModalLogin from './pages/ModalLogin/ModalLogin';
import Profile from './pages/Profile/Profile';
import NotFound from './pages/NotFound.jsx/NotFound';

function App() {
  return (
    <UserProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home />}>
            <Route path="login" element={<ModalLogin />} />
          </Route>
          <Route path="/in" element={<LoggedIn />}>
            <Route index replace element={<Navigate to="profile" />} />
            <Route path="profile" element={<Profile />} />
          </Route>
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </UserProvider>
  );
}

export default App;
