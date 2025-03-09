import { Alert, Button, Group, Space, Text } from "@mantine/core";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useForm, yupResolver } from "@mantine/form";
import { IconLogin2 } from "@tabler/icons-react";
import { useAuthorCreation } from "../../../../hooks/useSignUp.ts";
import { StepProps } from "../../../../types/common/props/step-props.ts";
import { AuthorCreationRequest } from "../../../../types/requests/author-creation-request.ts";
import FirstnameInput from "../../../elements/firstname-input.tsx";
import LastnameInput from "../../../elements/lastname-input.tsx";
import { authorCreationSchema } from "./author-step-schemas.ts";

function AuthorStep({ nextStep, prevStep }: StepProps) {
  const { t } = useTranslation();
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      firstName: "",
      lastName: "",
      pseudonym: "",
      email: "",
      bio: "",
      picture: "",
    },
    validate: yupResolver(authorCreationSchema(t)),
  });
  const authorCreation = useAuthorCreation();

  function handleSubmit(values: typeof form.values) {
    form.setSubmitting(true);
    authorCreation.mutate(values as AuthorCreationRequest);
    if (authorCreation.isSuccess) {
      nextStep();
    }
    console.log(authorCreation.error);
    console.log(authorCreation.data);
  }

  return (
    <>
      {authorCreation.isError && (
        <>
          <Alert variant="filled" color="red">
            {t("signup.error.internal")}
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
        <FirstnameInput form={form} formName="signup" />
        <Space h="xs" />
        <LastnameInput form={form} formName="signup" />
        <Space h="xs" />
        <Group justify="center" mt="xl">
          <Button variant="default" onClick={prevStep} disabled={true}>
            {t("common.button.back")}
          </Button>
          <Button
            type="submit"
            data-testid="next-btn"
            loading={authorCreation.isPending || authorCreation.isSuccess}
            disabled={authorCreation.isPending || authorCreation.isSuccess}
            rightSection={<IconLogin2 size={14} />}
          >
            {t("common.button.next")}
          </Button>
        </Group>
      </form>
    </>
  );
}

export default AuthorStep;
