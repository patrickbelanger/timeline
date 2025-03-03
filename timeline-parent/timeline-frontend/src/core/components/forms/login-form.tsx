import {
  Alert,
  Box,
  Button,
  Fieldset,
  Grid,
  Group,
  Space,
} from "@mantine/core";
import { IconLogin2, IconUser } from "@tabler/icons-react";
import { useForm, yupResolver } from "@mantine/form";
import { useTranslation } from "react-i18next";
import { LoginRequest } from "../../types/loginRequest.ts";
import { useLogin } from "../../hooks/useLogin.ts";
import * as yup from "yup";
import { useAttempt } from "../../hooks/useAttempt.ts";
import { Link, useNavigate } from "react-router-dom";
import DebugGrid from "../containers/utils/debug-grid.tsx";
import UsernameInput from "../elements/username-input.tsx";
import PasswordInput from "../elements/password-input.tsx";

function LoginForm() {
  const { t } = useTranslation();
  const { hasExceededAttempts, increment } = useAttempt();
  const login = useLogin();
  const navigate = useNavigate();

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
                <UsernameInput form={form} formName="login" />
                <Space h="xs" />
                <PasswordInput form={form} formName="login" />
                <Space h="xs" />
                <Group justify="flex-end" mt="md">
                  <Button
                    type="submit"
                    data-testid="login-btn"
                    loading={login.isPending || login.isSuccess}
                    disabled={login.isPending || login.isSuccess}
                    rightSection={<IconLogin2 size={14} />}
                  >
                    {t("login.button.login")}
                  </Button>
                  <Link to="/sign-up">
                    <Button rightSection={<IconUser size={14} />}>
                      {t("login.button.sign-up")}
                    </Button>
                  </Link>
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
