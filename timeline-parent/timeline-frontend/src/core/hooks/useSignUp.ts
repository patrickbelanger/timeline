import axios from "axios";
import { SignUpRequest } from "../types/sign-up-request.ts";
import { useMutation } from "@tanstack/react-query";

async function signUp(signUpRequest: SignUpRequest) {
  return await axios.post(
    "http://localhost:8081/api/v1/authenticate/register",
    signUpRequest,
  );
}

export function useSignUp() {
  return useMutation({
    mutationFn: signUp,
    onSuccess: () => {
      return true;
    },
    onError: (error: any) => {
      return error.response.status;
    },
  });
}
