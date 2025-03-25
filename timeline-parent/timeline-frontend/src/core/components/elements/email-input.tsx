import { TextInput } from "@mantine/core";
import { IconAt } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { InputElementProps } from "./input-element-props.ts";

function EmailInput<T extends { email: string }>({
  form,
  formName = "signup",
}: InputElementProps<T>) {
  const { t } = useTranslation();
  const iconAt = <IconAt size={16} />;

  return (
    <TextInput
      {...form.getInputProps("email")}
      value={form.values.email || ""}
      name="email"
      key={form.key("email")}
      data-testid="email-input"
      radius="md"
      leftSectionPointerEvents="none"
      leftSection={iconAt}
      label={t(`${formName}.input.email.label`)}
      placeholder={t(`${formName}.input.email.placeholder`)}
    />
  );
}

export default EmailInput;
