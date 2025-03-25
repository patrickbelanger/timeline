import axios from "axios";
import { useMutation } from "@tanstack/react-query";
import { AccountCreationRequest } from "../types/requests/account-creation-request.ts";
import { AuthorCreationRequest } from "../types/requests/author-creation-request.ts";
import { useSignUpContext } from "./useSignUpContext.ts";

async function accountCreation(accountCreationRequest: AccountCreationRequest) {
  return await axios.post(
    "http://localhost:8081/api/v1/authenticate/register",
    accountCreationRequest,
  );
}

async function authorCreation(authorCreationRequest: AuthorCreationRequest) {
  return await axios.post(
    "http://localhost:8081/api/v1/authors",
    authorCreationRequest,
  );
}

function useSignUp<T>(mutationFn: (data: T) => Promise<any>) {
  const { setEmail, setUuid } = useSignUpContext();

  const mutation = useMutation({
    mutationFn,
    onSuccess: (response) => {
      const email = response?.data?.email;
      const userUuid = response?.data?.uuid;
      if (email) {
        setEmail(email);
        console.log("👨‍💻 User created with email:", email);
      }
      if (userUuid) {
        setUuid(userUuid); // Store UUID in context
        console.log("👨‍💻 User created with UUID:", userUuid);
      }
    },
    onError: (error: any) => error.response?.status,
  });

  return {
    ...mutation,
  };
}

export function useAccountCreation() {
  return useSignUp<AccountCreationRequest>(accountCreation);
}

export function useAuthorCreation() {
  return useSignUp<AuthorCreationRequest>(authorCreation);
}
