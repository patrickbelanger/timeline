import * as yup from "yup";

/* Refactor label keys */
export function accountCreationSchema(t: any) {
  return yup.object().shape({
    username: yup
      .string()
      .required(t("login.input.username.error.empty"))
      .email(t("login.input.username.error.invalid")),
    password: yup
      .string()
      .required()
      .min(8, t("login.input.password.error.min")),
    confirmPassword: yup
      .string()
      .required()
      .min(8, t("login.input.password.error.min")),
  });
}
