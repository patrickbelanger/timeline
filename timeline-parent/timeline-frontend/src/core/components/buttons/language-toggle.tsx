import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Menu, Button } from "@mantine/core";
import { IconLanguage } from "@tabler/icons-react";

const LANGUAGES = [
  { code: "en", label: "English" },
  { code: "fr", label: "Français" },
];

export default function LanguageDropdown() {
  const { i18n } = useTranslation();
  const [currentLang, setCurrentLang] = useState(i18n.language);

  useEffect(() => {
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
    <Menu>
      <Menu.Target>
        <Button
          variant="outline"
          size="sm"
          leftSection={<IconLanguage size={16} />}
        >
          {LANGUAGES.find((l) => l.code === currentLang)?.label || "Language"}
        </Button>
      </Menu.Target>
      <Menu.Dropdown>
        {LANGUAGES.map(({ code, label }) => (
          <Menu.Item key={code} onClick={() => changeLanguage(code)}>
            {label}
          </Menu.Item>
        ))}
      </Menu.Dropdown>
    </Menu>
  );
}
