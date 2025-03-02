import { useState } from "react";

export function useAttempt() {
  const [attempt, setAttempt] = useState(0);
  const MAX_ATTEMPTS = 2;

  function increment() {
    setAttempt((prevState) => prevState + 1);
  }

  return { hasExceededAttempts: attempt >= MAX_ATTEMPTS, increment };
}
