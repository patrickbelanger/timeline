import { describe, it, vi, expect, Mock } from "vitest";
import { renderHook, act } from "@testing-library/react";
import { useLogin } from "./useLogin";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

vi.mock("axios");
vi.mock("react-router-dom", () => ({
  useNavigate: vi.fn(),
}));

const queryClient = new QueryClient();

describe("useLogin hook", () => {
  const mockNavigate = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    vi.useFakeTimers();
    (useNavigate as Mock).mockReturnValue(mockNavigate);
  });

  it("calls login function and navigates on success", async () => {
    const loginRequest = { username: "test", password: "password" };
    (axios.post as Mock).mockResolvedValue({
      data: { token: "fake_token" },
    });

    const { result } = renderHook(() => useLogin(), {
      wrapper: ({ children }) => (
        <QueryClientProvider client={queryClient}>
          {children}
        </QueryClientProvider>
      ),
    });

    await act(async () => {
      await result.current.mutateAsync(loginRequest);
    });

    vi.advanceTimersByTime(2000);
    expect(mockNavigate).toHaveBeenCalledWith("/dashboard", { replace: true });
  });

  it("handles errors properly", async () => {
    const loginRequest = { username: "test", password: "password" };
    const mockError = new Error("Invalid credentials");
    (axios.post as Mock).mockRejectedValue(mockError);

    const consoleSpy = vi.spyOn(console, "log").mockImplementation(() => {});

    const { result } = renderHook(() => useLogin(), {
      wrapper: ({ children }) => (
        <QueryClientProvider client={queryClient}>
          {children}
        </QueryClientProvider>
      ),
    });

    await act(async () => {
      await result.current.mutateAsync(loginRequest).catch(() => {});
    });

    expect(consoleSpy).toHaveBeenCalledWith(mockError);
    consoleSpy.mockRestore();
  });
});
