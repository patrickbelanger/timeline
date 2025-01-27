import LoginIcon from "@mui/icons-material/Login";
import LogoutIcon from "@mui/icons-material/Logout";

type Props = {
  isVisible?: boolean;
  isLogged?: boolean;
};

function LoginButton({ isVisible = true, isLogged = false }: Props) {
  if (!isVisible) {
    return <></>;
  }
  return isLogged ? <LogoutIcon /> : <LoginIcon />;
}

export default LoginButton;
