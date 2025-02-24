import { beforeEach } from "@vitest/runner";
import i18nForTest from "../../../i18nForTest.ts";
import { Matcher, render } from "@testing-library/react";
import { I18nextProvider } from "react-i18next";
import { MantineProvider } from "@mantine/core";
import { theme } from "../../theme/theme.ts";
import { MemoryRouter } from "react-router-dom";
import LoginForm from "./login-form.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { userEvent } from "@testing-library/user-event";

describe("<LoginForm />", () => {
  beforeEach(() => {
    i18nForTest.changeLanguage("en");
  });

  it("renders", () => {
    const queryClient = new QueryClient();
    const { container } = render(
      <I18nextProvider i18n={i18nForTest}>
        <QueryClientProvider client={queryClient}>
          <MantineProvider theme={theme}>
            <MemoryRouter initialEntries={["/"]}>
              <LoginForm />
            </MemoryRouter>
          </MantineProvider>
        </QueryClientProvider>
      </I18nextProvider>,
    );

    expect(container).toBeInTheDocument();
  });

  it.each([
    ["en", ["Email address", "Password", "Login", "Sign up"]],
    ["fr", ["Adresse courriel", "Mot de passe", "Connexion", "Inscription"]],
  ])("assert labels [%s]", async (locale: string, labels: Matcher[]) => {
    const queryClient = new QueryClient();
    await i18nForTest.changeLanguage(locale);
    const { getByText } = render(
      <I18nextProvider i18n={i18nForTest}>
        <QueryClientProvider client={queryClient}>
          <MantineProvider theme={theme}>
            <MemoryRouter initialEntries={["/"]}>
              <LoginForm />
            </MemoryRouter>
          </MantineProvider>
        </QueryClientProvider>
      </I18nextProvider>,
    );

    labels.forEach((label) => {
      expect(getByText(label)).toBeInTheDocument();
    });
  });

  it.each([
    [
      "en",
      ["Login", "Enter your email", "Password must be at least 8 characters"],
    ],
    [
      "fr",
      [
        "Connexion",
        "Entrez votre adresse courriel",
        "Mot de passe doit contenir au minimum 8 caractères",
      ],
    ],
  ])("assert errors [%s]", async (locale: string, labels: Matcher[]) => {
    const queryClient = new QueryClient();
    await i18nForTest.changeLanguage(locale);
    const { getByText } = render(
      <I18nextProvider i18n={i18nForTest}>
        <QueryClientProvider client={queryClient}>
          <MantineProvider theme={theme}>
            <MemoryRouter initialEntries={["/"]}>
              <LoginForm />
            </MemoryRouter>
          </MantineProvider>
        </QueryClientProvider>
      </I18nextProvider>,
    );

    await userEvent.click(getByText(labels[0]));

    labels.forEach((label) => {
      expect(getByText(label)).toBeInTheDocument();
    });
  });
});
