import { useMutation } from "@tanstack/react-query";
import { LoginRequest } from "../types/loginRequest.ts";
import axios from "axios";
import { useNavigate } from "react-router-dom";

async function login(loginRequest: LoginRequest) {
  return await axios.post(
    "http://localhost:8081/api/v1/authenticate/login",
    loginRequest,
  );
}

export function useLogin() {
  const timeoutDelay = 2000;
  const redirectionUrl = "/dashboard";

  const navigate = useNavigate();

  return useMutation({
    mutationFn: login,
    onSuccess: () => {
      const timeoutId = setTimeout(() => {
        navigate(redirectionUrl, { replace: true });
      }, timeoutDelay);

      return () => clearTimeout(timeoutId);
    },
    onError: (data) => {
      console.log(data);
    },
  });
}
