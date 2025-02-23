import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import english from "../public/locales/en/translation.json";
import french from "../public/locales/fr/translation.json";

i18n.use(initReactI18next).init({
  lng: "en",
  fallbackLng: "en",
  debug: true,
  interpolation: { escapeValue: false },
  resources: {
    en: { translation: english },
    fr: { translation: french },
  },
});

export default i18n;
