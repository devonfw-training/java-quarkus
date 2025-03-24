import React from 'react';
import { logoutKeycloak } from '../../keycloak';

const LogoutButton: React.FC = () => {
  const handleLogout = () => {
    logoutKeycloak();
  };

  return (
    <button onClick={handleLogout}>
      Logout
    </button>
  );
};

export default LogoutButton;