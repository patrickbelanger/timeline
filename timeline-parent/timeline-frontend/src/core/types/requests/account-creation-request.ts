import { LoginRequest } from "./login-request.ts";

export type AccountCreationRequest = LoginRequest & {
  confirmPassword: string;
  role: string;
};
