import {
  Box,
  Button,
  Fieldset,
  Grid,
  Group,
  LoadingOverlay,
  PasswordInput,
  Space,
  TextInput,
} from "@mantine/core";
import { IconAt, IconLogin2, IconUser } from "@tabler/icons-react";
import { isEmail, useForm } from "@mantine/form";
import { useTranslation } from "react-i18next";

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
  const iconAt = <IconAt size={16} />;
  const { t } = useTranslation();

  function handleSubmit(values: typeof form.values) {
    form.setSubmitting(true);
    console.log("handleSubmit");
    alert(values.username + " " + values.password);
    form.setSubmitting(false);
  }

  return (
    <>
      <Box pos="relative">
        <Grid>
          <Grid.Col span="auto" />
          <Grid.Col span={3}>
            <LoadingOverlay
              visible={false}
              zIndex="1000"
              overlayProps={{ blur: 2 }}
              loaderProps={{ color: "orange", type: "bars" }}
            />
            <Space h="xl" />
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
                  label={t("login.input.password.label")}
                  placeholder={t("login.input.password.placeholder")}
                />
                <Space h="xs" />
                <Group justify="flex-end" mt="md">
                  <Button
                    type="submit"
                    loading={form.submitting}
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
