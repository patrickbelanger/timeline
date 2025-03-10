import { TextInput } from "@mantine/core";
import { IconUser } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { InputElementProps } from "./input-element-props.ts";

function LastnameInput<T extends { firstName: string }>({
  form,
  formName = "signup",
}: InputElementProps<T>) {
  const { t } = useTranslation();
  const iconAt = <IconUser size={16} />;

  return (
    <TextInput
      {...form.getInputProps("lastName")}
      name="lastName"
      key={form.key("lastName")}
      data-testid="lastname-input"
      radius="md"
      leftSectionPointerEvents="none"
      leftSection={iconAt}
      label={t(`${formName}.input.lastname.label`)}
      placeholder={t(`${formName}.input.lastname.placeholder`)}
    />
  );
}

export default LastnameInput;
