import { useContext } from "react";
import { SignUpContext } from "../contexts/sign-up-context.tsx";

export function useSignUpContext() {
  const context = useContext(SignUpContext);
  if (!context) {
    throw new Error("SignUpProvider must be defined to use useAuth hook");
  }
  return context;
}
