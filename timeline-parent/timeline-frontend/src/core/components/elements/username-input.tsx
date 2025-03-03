import { TextInput } from "@mantine/core";
import { IconAt } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { InputElementProps } from "./input-props.ts";

function UsernameInput<T extends { username: string }>({
  form,
  formName = "login",
}: InputElementProps<T>) {
  const { t } = useTranslation();
  const iconAt = <IconAt size={16} />;

  return (
    <TextInput
      {...form.getInputProps("username")}
      name="username"
      key={form.key("username")}
      data-testid="username-input"
      radius="md"
      leftSectionPointerEvents="none"
      leftSection={iconAt}
      label={t(`${formName}.input.username.label`)}
      placeholder={t(`${formName}.input.username.placeholder`)}
    />
  );
}

export default UsernameInput;
