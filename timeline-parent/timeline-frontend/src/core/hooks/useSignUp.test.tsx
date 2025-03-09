import { renderHook, act, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import axios from "axios";
import { Mocked, vi } from "vitest";
import { useAccountCreation, useAuthorCreation } from "./useSignUp.ts";

vi.mock("axios");
const mockedAxios = axios as Mocked<typeof axios>;

const createWrapper = () => {
  const queryClient = new QueryClient();
  return ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};

describe("useAccountCreation", () => {
  it("should call API and return success on successful account creation", async () => {
    mockedAxios.post.mockResolvedValueOnce({ data: { message: "Success" } });

    const { result } = renderHook(() => useAccountCreation(), {
      wrapper: createWrapper(),
    });

    act(() => {
      result.current.mutate({
        username: "test",
        password: "password",
        confirmPassword: "password",
        role: "USER",
      });
    });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(mockedAxios.post).toHaveBeenCalledWith(
        "http://localhost:8081/api/v1/authenticate/register",
        {
          username: "test",
          password: "password",
          confirmPassword: "password",
          role: "USER",
        },
      );
    });
  });

  it("should return error status on failure", async () => {
    mockedAxios.post.mockRejectedValueOnce({ response: { status: 409 } });

    const { result } = renderHook(() => useAccountCreation(), {
      wrapper: createWrapper(),
    });

    act(() => {
      result.current.mutate({
        username: "test",
        password: "password",
        confirmPassword: "password",
        role: "USER",
      });
    });

    await waitFor(() => {
      expect(result.current.isError).toBe(true);
      expect(result.current.error.response.status).toBe(409);
    });
  });
});

describe("useAuthorCreation", () => {
  it("should call API and return success on successful author creation", async () => {
    mockedAxios.post.mockResolvedValueOnce({ data: { message: "Success" } });

    const { result } = renderHook(() => useAuthorCreation(), {
      wrapper: createWrapper(),
    });

    act(() => {
      result.current.mutate({
        firstName: "Emilie",
        lastName: "Jolie",
        pseudonym: "Emilie Jolie",
        email: "emilie-jobin@test.com",
        bio: "Author bio",
        picture: "path/to/emilie-jobin-picture.png",
      });
    });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
      expect(mockedAxios.post).toHaveBeenCalledWith(
        "http://localhost:8081/api/v1/authors",
        {
          firstName: "Emilie",
          lastName: "Jolie",
          pseudonym: "Emilie Jolie",
          email: "emilie-jobin@test.com",
          bio: "Author bio",
          picture: "path/to/emilie-jobin-picture.png",
        },
      );
    });
  });

  it("should return error status on failure", async () => {
    mockedAxios.post.mockRejectedValueOnce({ response: { status: 409 } });

    const { result } = renderHook(() => useAuthorCreation(), {
      wrapper: createWrapper(),
    });

    act(() => {
      result.current.mutate({
        firstName: "Emilie",
        lastName: "Jolie",
        pseudonym: "Emilie Jolie",
        email: "emilie-jobin@test.com",
        bio: "Author bio",
        picture: "path/to/emilie-jobin-picture.png",
      });
    });

    await waitFor(() => {
      expect(result.current.isError).toBe(true);
      expect(result.current.error.response.status).toBe(409);
    });
  });
});
