import {
  Alert,
  Box,
  Button,
  Fieldset,
  Grid,
  Group,
  PasswordInput,
  Space,
  TextInput,
} from "@mantine/core";
import {
  IconAt,
  IconLockPassword,
  IconLogin2,
  IconUser,
} from "@tabler/icons-react";
import { isEmail, useForm } from "@mantine/form";
import { useTranslation } from "react-i18next";
import { LoginRequest } from "../../types/loginRequest.ts";
import { useLogin } from "../../hooks/useLogin.ts";

function LoginForm() {
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      username: "",
      password: "",
    },
    validate: {
      username: isEmail("Invalid email"),
    },
  });
  const login = useLogin();
  const iconAt = <IconAt size={16} />;
  const iconLockPassword = <IconLockPassword size={16} />;
  const { t } = useTranslation();

  function handleSubmit(values: typeof form.values) {
    form.setSubmitting(true);
    login.mutate(values as LoginRequest);
  }

  return (
    <>
      <Box pos="relative">
        <Grid>
          <Grid.Col span="auto" />
          <Grid.Col span={3}>
            {/* <LoadingOverlay
              visible={login.isPending}
              zIndex="1000"
              overlayProps={{ blur: 2 }}
              loaderProps={{ color: "orange", type: "bars" }}
            />*/}
            <Space h="xl" />
            {login.isError && (
              <>
                <Alert variant="filled" color="red">
                  {t("login.error.invalid-email-password")}
                </Alert>
                <Space h="xs" />
              </>
            )}
            {login.isSuccess && (
              <>
                <Alert variant="filled" color="green">
                  {t("login.status.successful")}
                </Alert>
                <Space h="xs" />
              </>
            )}
            <Fieldset variant="filled">
              <form onSubmit={form.onSubmit(handleSubmit)}>
                <TextInput
                  {...form.getInputProps("username")}
                  key={form.key("username")}
                  radius="md"
                  leftSectionPointerEvents="none"
                  leftSection={iconAt}
                  label={t("login.input.username.label")}
                  placeholder={t("login.input.username.placeholder")}
                />
                <Space h="xs" />
                <PasswordInput
                  {...form.getInputProps("password")}
                  key={form.key("password")}
                  radius="md"
                  leftSectionPointerEvents="none"
                  leftSection={iconLockPassword}
                  label={t("login.input.password.label")}
                  placeholder={t("login.input.password.placeholder")}
                />
                <Space h="xs" />
                <Group justify="flex-end" mt="md">
                  <Button
                    type="submit"
                    loading={login.isPending || login.isSuccess}
                    disabled={login.isPending || login.isSuccess}
                    rightSection={<IconLogin2 size={14} />}
                  >
                    {t("login.button.login")}
                  </Button>
                  <Button rightSection={<IconUser size={14} />}>
                    {t("login.button.sign-up")}
                  </Button>
                </Group>
              </form>
            </Fieldset>
          </Grid.Col>
          <Grid.Col span="auto" />
        </Grid>
      </Box>
    </>
  );
}

export default LoginForm;
