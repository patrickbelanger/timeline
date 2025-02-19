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
  const [currentLanguage, setCurrentLanguage] = useState(i18n.language);

  useEffect(() => {
    const savedLanguage = localStorage.getItem("language") || "en";
    i18n.changeLanguage(savedLanguage).then(() => {
      setCurrentLanguage(savedLanguage);
    });
  }, [i18n]);

  const changeLanguage = (lang: string) => {
    i18n.changeLanguage(lang).then(() => {
      localStorage.setItem("language", lang);
      setCurrentLanguage(lang);
    });
  };

  return (
    <Menu>
      <Menu.Target>
        <Button
          variant="outline"
          size="sm"
          leftSection={<IconLanguage size={16} />}
        >
          {LANGUAGES.find((l) => l.code === currentLanguage)?.label ||
            "Language"}
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
