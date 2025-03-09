import axios from "axios";
import { useMutation } from "@tanstack/react-query";
import { AccountCreationRequest } from "../types/requests/account-creation-request.ts";
import { AuthorCreationRequest } from "../types/requests/author-creation-request.ts";

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
  return useMutation({
    mutationFn,
    onSuccess: () => true,
    onError: (error: any) => error.response?.status,
  });
}

export function useAccountCreation() {
  return useSignUp<AccountCreationRequest>(accountCreation);
}

export function useAuthorCreation() {
  return useSignUp<AuthorCreationRequest>(authorCreation);
}
