import { renderHook } from "@testing-library/react";
import { useAttempt } from "./useAttempt.ts";
import { act } from "react";

describe("useAttempt", () => {
  it("should hasExceededAttempts initialized to false", () => {
    const { result } = renderHook(() => useAttempt());
    expect(result.current.hasExceededAttempts).toBe(false);
  });

  it("should not exceed max attempts on first increment", () => {
    const { result } = renderHook(() => useAttempt());

    act(() => {
      result.current.increment();
    });

    expect(result.current.hasExceededAttempts).toBe(false);
  });

  it("should exceed max attempts after two increments", () => {
    const { result } = renderHook(() => useAttempt());

    act(() => {
      result.current.increment();
      result.current.increment();
    });

    expect(result.current.hasExceededAttempts).toBe(true);
  });
});
