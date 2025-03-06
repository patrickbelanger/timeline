import { LoginRequest } from "./login-request.ts";

export type SignUpRequest = LoginRequest & {
  confirmPassword: string;
  role: string;
};
