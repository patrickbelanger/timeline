import { beforeEach } from "@vitest/runner";
import i18nForTest from "../../../i18nForTest.ts";
import { Matcher, render, screen, waitFor } from "@testing-library/react";
import { I18nextProvider } from "react-i18next";
import { MantineProvider } from "@mantine/core";
import { theme } from "../../theme/theme.ts";
import { MemoryRouter } from "react-router-dom";
import LoginForm from "./login-form.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { userEvent } from "@testing-library/user-event";
import { Mock } from "vitest";
import { useLogin } from "../../hooks/useLogin.ts";
import { useAttempt } from "../../hooks/useAttempt.ts";

vi.mock("../../hooks/useLogin");
vi.mock("../../hooks/useAttempt");

const mockMutate = vi.fn();
const mockIncrement = vi.fn();
const mockHasExceededAttempts = vi.fn();

describe("<LoginForm />", () => {
  beforeEach(() => {
    i18nForTest.changeLanguage("en");
    (useLogin as Mock).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: false,
      isError: false,
    });

    (useAttempt as Mock).mockReturnValue({
      increment: mockIncrement,
      hasExceededAttempts: mockHasExceededAttempts,
    });
    mockHasExceededAttempts.mockReturnValue(false);
  });

  function renderComponent() {
    const queryClient = new QueryClient();
    return render(
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
  }

  it("renders", () => {
    const { container } = renderComponent();

    expect(container).toBeInTheDocument();
  });

  it.each([
    ["en", ["Email address", "Password", "Login", "Sign up"]],
    ["fr", ["Adresse courriel", "Mot de passe", "Connexion", "Inscription"]],
  ])("assert labels [%s]", async (locale: string, labels: Matcher[]) => {
    await i18nForTest.changeLanguage(locale);
    const { getByText } = renderComponent();

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
    await i18nForTest.changeLanguage(locale);
    const { getByText } = renderComponent();

    await userEvent.click(getByText(labels[0]));

    labels.forEach((label) => {
      expect(getByText(label)).toBeInTheDocument();
    });
  });

  it("should be able to submit login form", async () => {
    renderComponent();

    await userEvent.type(
      screen.getByTestId("username-input"),
      "test@example.com",
    );
    await userEvent.type(screen.getByTestId("password-input"), "password123");
    await userEvent.click(screen.getByTestId("login-btn"));

    await waitFor(() => {
      expect(mockMutate).toHaveBeenCalledWith({
        username: "test@example.com",
        password: "password123",
      });
    });
  });

  it("should return an error if an invalid email is provided", async () => {
    renderComponent();

    await userEvent.type(screen.getByTestId("username-input"), "invalid-email");
    await userEvent.click(screen.getByTestId("login-btn"));

    expect(await screen.findByText("Required format: name@domain.com"));
  });

  it("should return error message when login fails", async () => {
    (useLogin as Mock).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: false,
      isError: true,
    });

    renderComponent();

    expect(
      await screen.findByText("Invalid email/password combination"),
    ).toBeInTheDocument();
  });

  it("should return success message when login is successful", async () => {
    (useLogin as Mock).mockReturnValue({
      mutate: mockMutate,
      isPending: false,
      isSuccess: true,
      isError: false,
    });

    renderComponent();

    expect(
      await screen.findByText("Login successful. Please stand by..."),
    ).toBeInTheDocument();
  });
});
