import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { loginUser, toggleError } from "../../Slices/UserSlice";
import { AppDispatch } from "../../Store";
import "./LoginForm.css";

export const Login: React.FC = () => {
  const [userName, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const dispatch: AppDispatch = useDispatch();

  const handleInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.name === "userName") {
      setUsername(event.target.value);
    } else {
      setPassword(event.target.value);
    }
  };

  const handleLogin = (event: React.MouseEvent<HTMLButtonElement>) => {
    let credentials = {
      userName,
      password,
    };

    dispatch(loginUser(credentials));
  };

  return (
    <div className="login">
      <div className="text-container">
        <h1 className="login-h1">
          Welcome to the Employee Reimbursement System
        </h1>
        <h2>Sign in to view and create a reimbursement</h2>
      </div>

      <form className="login-form">
        <div className="input-div">
          <h4 className="input-h4">Please Enter Username</h4>
          <input
            autoComplete="off"
            className="login-input"
            type="text"
            placeholder="username"
            name="userName"
            onChange={handleInput}
          />
        </div>

        <div className="input-div">
          <h4 className="input-h4">Please Enter Password</h4>
          <input
            className="login-input"
            type="password"
            name="password"
            placeholder="password"
            onChange={handleInput}
          />
        </div>
      </form>
      <button className="login-btn" onClick={handleLogin}>
        Login
      </button>
    </div>
  );
};
