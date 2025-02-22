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
import { useForm, yupResolver } from "@mantine/form";
import { useTranslation } from "react-i18next";
import { LoginRequest } from "../../types/loginRequest.ts";
import { useLogin } from "../../hooks/useLogin.ts";
import * as yup from "yup";
import { useAttempt } from "../../hooks/useAttempt.ts";
import { useNavigate } from "react-router-dom";
import DebugGrid from "../containers/utils/debug-grid.tsx";

function LoginForm() {
  const { t } = useTranslation();
  const { hasExceededAttempts, increment } = useAttempt();
  const navigate = useNavigate();

  const login = useLogin();
  const iconAt = <IconAt size={16} />;
  const iconLockPassword = <IconLockPassword size={16} />;

  const loginSchema = yup.object().shape({
    username: yup
      .string()
      .required(t("login.input.username.error.empty"))
      .email(t("login.input.username.error.invalid")),
    password: yup
      .string()
      .required()
      .min(8, t("login.input.password.error.min")),
  });

  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      username: "",
      password: "",
    },
    validate: yupResolver(loginSchema),
  });

  function handleSubmit(values: typeof form.values) {
    form.setSubmitting(true);
    login.mutate(values as LoginRequest);
    if (login.isError) {
      increment();
    }
    if (hasExceededAttempts) {
      navigate("/login-difficulties", { replace: true });
    }
  }

  return (
    <>
      <DebugGrid />
      <Box pos="relative">
        <Grid
          grow
          type="container"
          breakpoints={{
            xs: "100px",
            sm: "200px",
            md: "300px",
            lg: "400px",
            xl: "500px",
          }}
        >
          <Grid.Col span={{ md: 4, lg: 4 }} visibleFrom="sm" />
          <Grid.Col span={{ xs: 12, sm: 10, md: 4, lg: 4 }}>
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
          <Grid.Col span={{ md: 4, lg: 4 }} visibleFrom="sm" />
        </Grid>
      </Box>
    </>
  );
}

export default LoginForm;
