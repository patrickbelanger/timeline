import { beforeEach } from "@vitest/runner";
import i18nForTest from "../../../i18nForTest.ts";
import { render, Matcher, waitFor } from "@testing-library/react";
import { I18nextProvider } from "react-i18next";
import { MantineProvider } from "@mantine/core";
import { theme } from "../../theme/theme.ts";
import LanguageToggle from "./language-toggle.tsx";
import { userEvent } from "@testing-library/user-event";

describe("<LanguageToggle />", () => {
  beforeEach(() => {
    i18nForTest.changeLanguage("en");
  });

  it("renders", () => {
    const { container } = render(
      <I18nextProvider i18n={i18nForTest}>
        <MantineProvider theme={theme}>
          <LanguageToggle />
        </MantineProvider>
      </I18nextProvider>,
    );

    expect(container).toBeInTheDocument();
  });

  it.each([
    ["en", ["English", "Français"]],
    ["fr", ["Français", "English"]],
  ])("assert labels [%s]", async (locale: string, labels: Matcher[]) => {
    await i18nForTest.changeLanguage(locale);
    const { getByText } = render(
      <I18nextProvider i18n={i18nForTest}>
        <MantineProvider theme={theme}>
          <LanguageToggle />
        </MantineProvider>
      </I18nextProvider>,
    );

    expect(getByText(labels[0])).toBeInTheDocument();

    await userEvent.click(getByText(labels[0]));
    await waitFor(() => {
      expect(getByText(labels[1])).toBeInTheDocument();
    });

    await userEvent.click(getByText(labels[1]));
    await waitFor(() => {
      expect(getByText(labels[0])).toBeInTheDocument();
    });
  });
});
