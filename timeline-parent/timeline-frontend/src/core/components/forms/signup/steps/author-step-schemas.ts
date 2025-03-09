import * as yup from "yup";

export function authorCreationSchema(t: any) {
  return yup.object().shape({
    firstname: yup
      .string()
      .required()
      .min(2, t("signup.input.firstname.error.min")),
    lastname: yup
      .string()
      .required()
      .min(2, t("signup.input.firstname.error.min")),
  });
}
