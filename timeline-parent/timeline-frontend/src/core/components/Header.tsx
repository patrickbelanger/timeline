import { AppBar, Button, IconButton, Toolbar, Typography } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import LoginButton from "./buttons/LoginButton.tsx";
import { useNavigate } from "react-router-dom";

type Props = {
  isDrawerMenuDisabled?: boolean;
  isLoginButtonVisible?: boolean;
};

function Header({
  isDrawerMenuDisabled = true,
  isLoginButtonVisible = true,
}: Props) {
  const navigate = useNavigate();

  return (
    <AppBar position="absolute">
      <Toolbar>
        <IconButton
          size="large"
          edge="start"
          color="inherit"
          aria-label="Drawer menu"
          sx={{ mr: 2 }}
          disabled={isDrawerMenuDisabled}
        >
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Timeline
        </Typography>
        {isLoginButtonVisible && (
          <Button
            color="inherit"
            onClick={() => navigate("/login")}
            aria-label="Login"
          >
            <LoginButton />
          </Button>
        )}
      </Toolbar>
    </AppBar>
  );
}

export default Header;
