import { Alert, Button, Group, Space, Text } from "@mantine/core";
import { Link } from "react-router-dom";
import UsernameInput from "../../../elements/username-input.tsx";
import PasswordInput from "../../../elements/password-input.tsx";
import ConfirmPasswordInput from "../../../elements/confirm-password-input.tsx";
import { useTranslation } from "react-i18next";
import { useForm, yupResolver } from "@mantine/form";
import { IconLogin2 } from "@tabler/icons-react";
import { useAccountCreation } from "../../../../hooks/useSignUp.ts";
import { StepProps } from "../../../../types/common/props/step-props.ts";
import { AccountCreationRequest } from "../../../../types/requests/account-creation-request.ts";
import { accountCreationSchema } from "./account-step-schemas.ts";
import { nprogress } from "@mantine/nprogress";
import { useEffect } from "react";

function AccountStep({ nextStep, prevStep }: StepProps) {
  const { t } = useTranslation();
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      username: "",
      password: "",
      confirmPassword: "",
    },
    validate: yupResolver(accountCreationSchema(t)),
  });
  const accountCreation = useAccountCreation();

  function handleSubmit(values: typeof form.values) {
    form.setSubmitting(true);
    accountCreation.mutate(values as AccountCreationRequest, {
      onSuccess: () => {
        console.log("✅ Mutation success, moving to next step");
        nprogress.complete();
        nextStep();
      },
      onError: () => {
        nprogress.complete();
      },
    });
  }

  useEffect(() => {
    if (accountCreation.isPending) {
      nprogress.start();
    }
  });

  return (
    <>
      {accountCreation.isError && (
        <>
          <Alert variant="filled" color="red">
            {(
              {
                409: t("signup.error.alreadyExists"),
                400: t("signup.error.badRequest"),
              } as Record<number, string>
            )[accountCreation.error?.response?.status ?? -1] ||
              t("signup.error.internal")}
          </Alert>
          <Space h="xs" />
        </>
      )}
      <form onSubmit={form.onSubmit(handleSubmit)}>
        <Space h="xs" />
        <Text size="sm">
          {t("signup.steppers.step.one.panel")}{" "}
          <Link to="/">{t("signup.steppers.step.one.login-cta")}</Link>
        </Text>
        <Space h="xs" />
        <UsernameInput form={form} formName="signup" />
        <Space h="xs" />
        <PasswordInput form={form} formName="signup" />
        <Space h="xs" />
        <ConfirmPasswordInput form={form} formName="signup" />
        <Group justify="center" mt="xl">
          <Button variant="default" onClick={prevStep}>
            {t("common.button.back")}
          </Button>
          <Button
            type="submit"
            data-testid="next-btn"
            loading={accountCreation.isPending || accountCreation.isSuccess}
            disabled={
              accountCreation.isPending ||
              accountCreation.isSuccess ||
              accountCreation.error?.response?.status === 409
            }
            rightSection={<IconLogin2 size={14} />}
          >
            {t("common.button.next")}
          </Button>
        </Group>
      </form>
    </>
  );
}

export default AccountStep;
