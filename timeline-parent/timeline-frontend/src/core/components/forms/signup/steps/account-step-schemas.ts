import * as yup from "yup";

export function accountCreationSchema(t: any) {
  return yup.object().shape({
    username: yup
      .string()
      .required(t("signup.input.username.error.empty"))
      .email(t("signup.input.username.error.invalid")),
    password: yup
      .string()
      .required()
      .min(8, t("signup.input.password.error.min")),
    confirmPassword: yup
      .string()
      .required()
      .min(8, t("signup.input.password.error.min"))
      .oneOf(
        [yup.ref("password")],
        t("signup.input.confirmPassword.error.match"),
      ),
  });
}
