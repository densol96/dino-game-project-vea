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
import { Mail } from './pages/Mail/Mail';
import ReadMail from './pages/Mail/ReadMail';
import WriteMail from './pages/Mail/WriteMail';
import Arena from './pages/Fights/Arena';
import PublicProfile from './pages/PublicProfile/PublicProfile';
import Farm from './pages/Farm/Farm';
import ArenaMain from './pages/Fights/ArenaMain';
import ArenaSearch from './pages/Fights/ArenaSearch';
import ArenaFightResult from './pages/Fights/ArenaFightResult';
import AdminManage from './pages/AdminManage.jsx/AdminManage';
import Clan from "./pages/Clans/Clan";
import Friends from "./pages/Friends/Friends";

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
              <Route path="users/:id" element={<PublicProfile />} />
              <Route path="arena" element={<Arena />}>
                <Route index replace element={<Navigate to="main" />} />
                <Route path="main" element={<ArenaMain />} />
                <Route path="search" element={<ArenaSearch />} />
                <Route path="result/:id" element={<ArenaFightResult />} />
              </Route>
              <Route path="mail">
                <Route index replace element={<Navigate to="all" />} />
                <Route path="all" element={<Mail />} />
                <Route path="read/:id" element={<ReadMail />} />
                <Route path="write" element={<WriteMail />} />
              </Route>
              <Route path="farm" element={<Farm />} />
              <Route path="clan" element={<Clan />} />
              <Route path="friends" element={<Friends />} />
              <Route path="settings" element={<Settings />} />
              <Route path="ratings" element={<Ratings />} />
              <Route path="admin/manage/:id" element={<AdminManage />} />
            </Route>
            <Route path="*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </NewMessagesProvider>
    </UserProvider>
  );
}

export default App;
