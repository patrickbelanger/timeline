import * as yup from "yup";

export function authorCreationSchema(t: any) {
  return yup.object().shape({
    firstName: yup
      .string()
      .required()
      .min(2, t("signup.input.firstname.error.min")),
    lastName: yup
      .string()
      .required()
      .min(2, t("signup.input.lastname.error.min")),
    email: yup
      .string()
      .required(t("signup.input.email.error.empty"))
      .email(t("signup.input.email.error.invalid")),
  });
}
