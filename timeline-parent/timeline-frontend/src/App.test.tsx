import { MantineProvider } from "@mantine/core";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { theme } from "./core/theme/theme.ts";
import LoginForm from "./core/components/forms/login-form.tsx";
import { MemoryRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

describe("<App />", () => {
  it("renders Mantine Button with correct label", () => {
    const queryClient = new QueryClient();
    render(
      <MemoryRouter>
        <QueryClientProvider client={queryClient}>
          <MantineProvider theme={theme}>
            <LoginForm />
          </MantineProvider>
        </QueryClientProvider>
      </MemoryRouter>,
    );
    /* WIP */
    screen.debug();
    const buttonElement = screen.getByText(/Click me/i);
    expect(buttonElement).toBeInTheDocument();
  });
});
