import React, { useState } from 'react';
import UserAvatar from '../../../../components/user/UserAvatar';
import { DropdownToggle, DropdownMenu, Dropdown } from 'reactstrap';
import { Icon } from '../../../../components/Component';
import { LinkList } from '../../../../components/links/Links';
import { useHistory } from 'react-router';
import { useAuth } from '../../../../context/authContext';
const User = () => {
  const [open, setOpen] = useState(false);
  const toggle = () => setOpen((prevState) => !prevState);
  const { logout, user } = useAuth();
  const history = useHistory();
  const handleSignout = () => {
    logout(history);
  };
  return (
    <Dropdown isOpen={open} className="user-dropdown" toggle={toggle}>
      <DropdownToggle
        tag="a"
        href="#toggle"
        className="dropdown-toggle"
        onClick={(ev) => {
          ev.preventDefault();
        }}>
        <div className="user-toggle">
          <UserAvatar icon="user-alt" className="sm" />
          <div className="user-info d-none d-md-block">
            <div className="user-status"></div>
            <div className="user-name dropdown-indicator">{user?.login_user}</div>
          </div>
        </div>
      </DropdownToggle>
      <DropdownMenu right className="dropdown-menu-md dropdown-menu-s1">
        <div className="dropdown-inner user-card-wrap bg-lighter d-none d-md-block">
          <div className="user-card sm">
            <div className="user-avatar">
              <span>A</span>
            </div>
            <div className="user-info">
              <span className="lead-text">{user?.login_user}</span>
              <span className="sub-text">admin@int.com</span>
            </div>
          </div>
        </div>

        <div className="dropdown-inner">
          <LinkList>
            <span onClick={handleSignout}>
              <Icon name="signout"></Icon>
              <span>Sign Out</span>
            </span>
          </LinkList>
        </div>
      </DropdownMenu>
    </Dropdown>
  );
};

export default User;
