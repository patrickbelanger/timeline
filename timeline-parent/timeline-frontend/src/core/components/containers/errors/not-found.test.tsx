import { beforeEach } from "@vitest/runner";
import { render, Matcher, waitFor } from "@testing-library/react";
import { I18nextProvider } from "react-i18next";
import { MantineProvider } from "@mantine/core";
import NotFound from "./not-found.tsx";
import i18nForTest from "../../../../i18nForTest.ts";
import { theme } from "../../../theme/theme.ts";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { userEvent } from "@testing-library/user-event";

function mockedHomePage() {
  return (
    <div>
      <h1>Home Page</h1>
    </div>
  );
}

describe("<NotFound />", () => {
  beforeEach(() => {
    i18nForTest.changeLanguage("en");
  });

  it("renders", () => {
    const { container } = render(
      <I18nextProvider i18n={i18nForTest}>
        <MantineProvider theme={theme}>
          <MemoryRouter initialEntries={["/not-found"]}>
            <NotFound />
          </MemoryRouter>
        </MantineProvider>
      </I18nextProvider>,
    );
    expect(container).toBeInTheDocument();
  });

  it.each([
    [
      "en",
      [
        "Sorry",
        "The page you're trying to access doesn't exist. You may have mistyped the address, or it might have been moved to a different URL. If you believe this is an error, please contact support.",
      ],
    ],
    [
      "fr",
      [
        "Désolé",
        "La page que vous essayez d’accéder n’existe pas. Vous avez peut-être mal saisi l’adresse, ou elle a été déplacée vers une autre URL. Si vous pensez qu’il s’agit d’une erreur, veuillez contacter le support.",
      ],
    ],
  ])("assert labels", async (locale: string, labels: Matcher[]) => {
    await i18nForTest.changeLanguage(locale);
    const { getByText } = render(
      <I18nextProvider i18n={i18nForTest}>
        <MantineProvider theme={theme}>
          <MemoryRouter initialEntries={["/not-found"]}>
            <NotFound />
          </MemoryRouter>
        </MantineProvider>
      </I18nextProvider>,
    );

    expect(getByText(labels[0])).toBeInTheDocument();
    expect(getByText(labels[1])).toBeInTheDocument();
  });

  it("should return to home page", async () => {
    const { getByText } = render(
      <I18nextProvider i18n={i18nForTest}>
        <MantineProvider theme={theme}>
          <MemoryRouter initialEntries={["/not-found"]}>
            <Routes>
              <Route path="/" element={mockedHomePage()} />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </MemoryRouter>
        </MantineProvider>
      </I18nextProvider>,
    );
    await userEvent.click(getByText("Back to home"));
    await waitFor(() => {
      expect(getByText("Home Page")).toBeInTheDocument();
    });
  });
});
