import { MantineProvider } from "@mantine/core";
import { render } from "@testing-library/react";
import "@testing-library/jest-dom";
import { theme } from "./core/theme/theme.ts";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { I18nextProvider } from "react-i18next";
import i18nForTest from "./i18nForTest.ts";
import App from "./App.tsx";
import { beforeEach } from "@vitest/runner";

describe("<App />", () => {
  beforeEach(() => {
    i18nForTest.changeLanguage("en");
  });

  it("renders", () => {
    const queryClient = new QueryClient();
    const { container } = render(
      <I18nextProvider i18n={i18nForTest}>
        <QueryClientProvider client={queryClient}>
          <MantineProvider theme={theme}>
            <App />
          </MantineProvider>
        </QueryClientProvider>
      </I18nextProvider>,
    );

    expect(container).toBeInTheDocument();
  });
});
