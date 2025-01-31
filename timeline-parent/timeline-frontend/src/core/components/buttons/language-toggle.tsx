import * as React from "react";
import { useTranslation } from "react-i18next";
import { Dropdown, Menu, MenuItem, MenuButton } from "@mui/joy";
import LanguageRoundedIcon from "@mui/icons-material/Language";

const LANGUAGES = [
  { code: "en", label: "English" },
  { code: "fr", label: "Français" },
];

function LanguageDropdown() {
  const { i18n } = useTranslation();
  const [currentLang, setCurrentLang] = React.useState(i18n.language);

  React.useEffect(() => {
    const savedLang = localStorage.getItem("language") || "en";
    i18n.changeLanguage(savedLang);
    setCurrentLang(savedLang);
  }, [i18n]);

  const changeLanguage = (lang: string) => {
    i18n.changeLanguage(lang);
    localStorage.setItem("language", lang);
    setCurrentLang(lang);
  };

  return (
    <Dropdown>
      <MenuButton
        startDecorator={<LanguageRoundedIcon />}
        variant="outlined"
        size="sm"
      >
        {LANGUAGES.find((l) => l.code === currentLang)?.label || "Language"}
      </MenuButton>
      <Menu>
        {LANGUAGES.map(({ code, label }) => (
          <MenuItem key={code} onClick={() => changeLanguage(code)}>
            {label}
          </MenuItem>
        ))}
      </Menu>
    </Dropdown>
  );
}

export default LanguageDropdown;
