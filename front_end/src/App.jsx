import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { UserProvider } from './context/UserProvider';
import { NewMessagesProvider } from './context/NewMessagesProvider';
import Home from './pages/home/Home';
import LoggedIn from './pages/logged/LoggedIn';
import ModalLogin from './pages/ModalLogin/ModalLogin';
import Profile from './pages/Profile/Profile';
import NotFound from './pages/NotFound.jsx/NotFound';
import Settings from './pages/settings/Settings';
import Ratings from './pages/Ratings/Ratings';
import Mail from './pages/Mail/Mail';
import ReadMail from './pages/Mail/ReadMail';
import WriteMail from './pages/Mail/WriteMail';

function App() {
  return (
    <UserProvider>
      <NewMessagesProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Home />}>
              <Route path="login" element={<ModalLogin />} />
            </Route>
            <Route path="/in" element={<LoggedIn />}>
              <Route index replace element={<Navigate to="profile" />} />
              <Route path="profile" element={<Profile />} />
              <Route path="mail">
                <Route index replace element={<Navigate to="all" />} />
                <Route path="all" element={<Mail />} />
                <Route path="read/:id" element={<ReadMail />} />
                <Route path="write" element={<WriteMail />} />
              </Route>
              <Route path="settings" element={<Settings />} />
              <Route path="ratings" element={<Ratings />} />
            </Route>
            <Route path="*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </NewMessagesProvider>
    </UserProvider>
  );
}

export default App;
