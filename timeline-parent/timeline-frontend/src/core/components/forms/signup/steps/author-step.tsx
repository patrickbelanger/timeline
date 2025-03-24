import { Alert, Button, Group, Popover, Space, Text } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { useForm, yupResolver } from "@mantine/form";
import { IconLogin2 } from "@tabler/icons-react";
import { useAuthorCreation } from "../../../../hooks/useSignUp.ts";
import { StepProps } from "../../../../types/common/props/step-props.ts";
import { AuthorCreationRequest } from "../../../../types/requests/author-creation-request.ts";
import FirstnameInput from "../../../elements/firstname-input.tsx";
import LastnameInput from "../../../elements/lastname-input.tsx";
import { authorCreationSchema } from "./author-step-schemas.ts";
import PseudonymInput from "../../../elements/pseudonym-input.tsx";
import { useDisclosure } from "@mantine/hooks";
import EmailInput from "../../../elements/email-input.tsx";
import BioTextarea from "../../../elements/bio-textarea.tsx";

function AuthorStep({ nextStep, prevStep }: StepProps) {
  const [opened, { close, open }] = useDisclosure(false);
  const { t } = useTranslation();
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      accountUuid: "",
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
    authorCreation.mutate(values as AuthorCreationRequest, {
      onSuccess: () => {
        console.log("✅ Mutation success, moving to next step");
        nextStep();
      },
    });
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
        <Text size="sm">{t("signup.steppers.step.two.panel")}</Text>
        <Space h="xs" />
        <FirstnameInput form={form} formName="signup" />
        <Space h="xs" />
        <LastnameInput form={form} formName="signup" />
        <Space h="xs" />
        <EmailInput form={form} formName="signup" />
        <Space h="xs" />
        <BioTextarea form={form} formName="signup" />

        <Popover
          width={200}
          position="bottom"
          withArrow
          shadow="md"
          opened={opened}
        >
          <Popover.Target>
            <PseudonymInput
              form={form}
              formName="signup"
              onMouseEnter={open}
              onMouseLeave={close}
            />
          </Popover.Target>
          <Popover.Dropdown style={{ pointerEvents: "none" }}>
            <Text size="sm">{t("signup.input.pseudonym.popover")}</Text>
          </Popover.Dropdown>
        </Popover>

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
