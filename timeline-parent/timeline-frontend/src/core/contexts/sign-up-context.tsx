import { createContext, ReactNode, useState } from "react";
import { SignUpContextType } from "./sign-up-context-type.ts";

export const SignUpContext = createContext<SignUpContextType | undefined>(
  undefined,
);

export function SignUpProvider({ children }: { children: ReactNode }) {
  const [uuid, setUuid] = useState<string>("");
  const [email, setEmail] = useState<string>("");

  return (
    <SignUpContext.Provider value={{ email, setEmail, uuid, setUuid }}>
      {children}
    </SignUpContext.Provider>
  );
}
