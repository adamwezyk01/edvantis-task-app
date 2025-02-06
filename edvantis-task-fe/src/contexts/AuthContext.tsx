import React, { createContext, useState, useContext, ReactNode } from 'react';

interface AuthContextProps {
  authHeader: string | null;
  login: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [authHeader, setAuthHeader] = useState<string | null>(null);

  const login = async (username: string, password: string): Promise<boolean> => {
    const header = 'Basic ' + btoa(`${username}:${password}`);
    try {
      const response = await fetch('http://localhost:8080/api/emergency-calls?page=0&size=1', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': header,
        },
      });
      if (response.ok) {
        setAuthHeader(header);
        return true;
      } else {
        return false;
      }
    } catch (error) {
      console.error('Login error', error);
      return false;
    }
  };

  const logout = () => {
    setAuthHeader(null);
  };

  return (
    <AuthContext.Provider value={{ authHeader, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextProps => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
