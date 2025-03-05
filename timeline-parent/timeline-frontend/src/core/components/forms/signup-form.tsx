import DebugGrid from "../containers/utils/debug-grid.tsx";
import {
  Box,
  Button,
  Fieldset,
  Grid,
  Group,
  Space,
  Stepper,
  Text,
} from "@mantine/core";
import { useState } from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import { useForm, yupResolver } from "@mantine/form";
import { useTranslation } from "react-i18next";
import UsernameInput from "../elements/username-input.tsx";
import PasswordInput from "../elements/password-input.tsx";
import { accountCreationSchema } from "./schemas/signup-form-schemas.ts";
import ConfirmPasswordInput from "../elements/confirm-password.tsx";

function SignUpForm() {
  const { t } = useTranslation();
  const [active, setActive] = useState(0);
  const navigate = useNavigate();
  const nextStep = () =>
    setActive((current) => (current < 3 ? current + 1 : current));
  const prevStep = () => {
    if (active === 0) {
      navigate("/", { replace: true });
    }
    setActive((current) => (current > 0 ? current - 1 : current));
  };

  const formAccountCreation = useForm({
    mode: "uncontrolled",
    initialValues: {
      username: "",
      password: "",
      confirmPassword: "",
    },
    validate: yupResolver(accountCreationSchema),
  });

  function handleAccountCreation(values: typeof formAccountCreation.values) {
    formAccountCreation.setSubmitting(true);
    console.log(values);
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
          <Grid.Col span={{ md: 4, lg: 2 }} visibleFrom="sm" />
          <Grid.Col span={{ xs: 12, sm: 10, md: 4, lg: 6 }}>
            <Space h="xl" />
            <Fieldset variant="filled">
              <Stepper active={active} onStepClick={setActive}>
                <Stepper.Step
                  label={t("signup.steppers.step.one.label")}
                  description={t("signup.steppers.step.one.description")}
                >
                  <form
                    onSubmit={formAccountCreation.onSubmit(
                      handleAccountCreation,
                    )}
                  >
                    <Space h="xs" />
                    <Text size="sm">
                      {t("signup.steppers.step.one.panel")}{" "}
                      <Link to="/">
                        {t("signup.steppers.step.one.login-cta")}
                      </Link>
                    </Text>
                    <Space h="xs" />
                    <UsernameInput
                      form={formAccountCreation}
                      formName="login"
                    />
                    <Space h="xs" />
                    <PasswordInput
                      form={formAccountCreation}
                      formName="login"
                    />
                    <Space h="xs" />
                    <ConfirmPasswordInput
                      form={formAccountCreation}
                      formName="login"
                    />
                  </form>
                </Stepper.Step>
                <Stepper.Step
                  label={t("signup.steppers.step.two.label")}
                  description={t("signup.steppers.step.two.description")}
                >
                  Placeholder
                </Stepper.Step>
                <Stepper.Step
                  label={t("signup.steppers.step.three.label")}
                  description={t("signup.steppers.step.three.description")}
                >
                  Placeholder
                </Stepper.Step>
                <Stepper.Completed>Placeholder</Stepper.Completed>
              </Stepper>
              <Group justify="center" mt="xl">
                <Button variant="default" onClick={prevStep}>
                  Back
                </Button>
                <Button onClick={nextStep}>Next</Button>
              </Group>
            </Fieldset>
            <Outlet />
          </Grid.Col>
          <Grid.Col span={{ md: 4, lg: 2 }} visibleFrom="sm" />
        </Grid>
      </Box>
    </>
  );
}

export default SignUpForm;
