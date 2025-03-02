import { Outlet } from "react-router-dom";
import Header from "./header.tsx";

function LoginRegistrationLayout() {
  return (
    <>
      <Header />
      <Outlet />
    </>
  );
}

export default LoginRegistrationLayout;
