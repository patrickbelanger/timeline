import { MantineProvider } from "@mantine/core";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "@testing-library/jest-dom";
import { theme } from "../../theme/theme.ts";
import LoginForm from "./login-form.tsx";
import i18next from "i18next";
import { I18nextProvider } from "react-i18next";
import i18n from "../../../i18n.ts";

describe("<LoginForm />", () => {
  beforeEach(async () => {
    await i18next.changeLanguage("en");
  });

  it("renders <LoginForm />", () => {
    const queryClient = new QueryClient();
    const { getByText } = render(
      <I18nextProvider i18n={i18n}>
        <QueryClientProvider client={queryClient}>
          <MantineProvider theme={theme}>
            <MemoryRouter>
              <LoginForm />
            </MemoryRouter>
          </MantineProvider>
        </QueryClientProvider>
      </I18nextProvider>,
    );
    screen.debug();
    expect(getByText("Allo")).toBeInTheDocument();
  });
});
