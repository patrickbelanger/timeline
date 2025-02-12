import { useMutation } from "@tanstack/react-query";
import { LoginRequest } from "../types/loginRequest.ts";
import axios from "axios";

async function login(loginRequest: LoginRequest) {
  return await axios.post(
    "http://localhost:8081/api/v1/authenticate/login",
    loginRequest,
  );
}

export function useLogin() {
  return useMutation({
    mutationFn: login,
    onSuccess: (data) => {
      console.log(data);
    },
    onError: (data) => {
      console.log(data);
    },
  });
}
