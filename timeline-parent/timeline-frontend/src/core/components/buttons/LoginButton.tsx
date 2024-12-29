import LoginIcon from "@mui/icons-material/Login";
import LogoutIcon from "@mui/icons-material/Logout";

type Props = {
  isLogged?: boolean;
};

function LoginButton({ isLogged = false }: Props) {
  return isLogged ? <LogoutIcon /> : <LoginIcon />;
}

export default LoginButton;
