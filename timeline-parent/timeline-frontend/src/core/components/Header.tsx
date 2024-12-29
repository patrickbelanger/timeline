import { AppBar, Button, IconButton, Toolbar, Typography } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import LoginButton from "./buttons/LoginButton.tsx";

function Header() {
  return (
    <AppBar position="absolute">
      <Toolbar>
        <IconButton
          size="large"
          edge="start"
          color="inherit"
          aria-label="menu"
          sx={{ mr: 2 }}
        >
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Timeline
        </Typography>
        <Button color="inherit">
          <LoginButton />
        </Button>
      </Toolbar>
    </AppBar>
  );
}

export default Header;
