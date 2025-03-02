import i18next from "i18next";
import { render } from "@testing-library/react";
import { I18nextProvider } from "react-i18next";
import i18n from "../../../i18n.ts";
import LoginRegistrationLayout from "./login-registration-layout.tsx";
import { MantineProvider } from "@mantine/core";

describe("<LoginRegistrationLayout />", () => {
  beforeEach(() => {
    i18next.changeLanguage("en");
  });

  it("renders", () => {
    const { container } = render(
      <I18nextProvider i18n={i18n}>
        <MantineProvider>
          <LoginRegistrationLayout />
        </MantineProvider>
      </I18nextProvider>,
    );
    expect(container).toBeTruthy();
  });
});
