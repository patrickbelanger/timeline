import LanguageDropdown from "../buttons/language-toggle.tsx";
import { Container } from "@mantine/core";

function AppHeader() {
  return (
    <>
      <header>
        <Container size="xl">
          <LanguageDropdown />
        </Container>
      </header>
    </>
  );
}

export default AppHeader;
