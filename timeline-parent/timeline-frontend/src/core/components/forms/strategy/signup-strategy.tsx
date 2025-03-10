import { TFunction } from "i18next";
import { Step } from "../../../types/step.ts";
import AccountStep from "../signup/steps/account-step.tsx";
import AuthorStep from "../signup/steps/author-step.tsx";

export function getStep(t: TFunction): Step[] {
  return [
    {
      label: t("signup.steppers.step.one.label"),
      description: t("signup.steppers.step.one.description"),
      component: (props) => <AccountStep {...props} />,
    },
    {
      label: t("signup.steppers.step.two.label"),
      description: t("signup.steppers.step.two.description"),
      component: (props) => <AuthorStep {...props} />,
    },
    {
      label: t("signup.steppers.step.three.label"),
      description: t("signup.steppers.step.three.description"),
      component: (props) => <AccountStep {...props} />,
    },
  ];
}
