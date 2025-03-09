import { TextInput } from "@mantine/core";
import { IconAt } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { InputElementProps } from "./input-element-props.ts";

function FirstnameInput<T extends { firstName: string }>({
  form,
  formName = "signup",
}: InputElementProps<T>) {
  const { t } = useTranslation();
  const iconAt = <IconAt size={16} />;

  return (
    <TextInput
      {...form.getInputProps("firstName")}
      name="firstName"
      key={form.key("firstName")}
      data-testid="firstname-input"
      radius="md"
      leftSectionPointerEvents="none"
      leftSection={iconAt}
      label={t(`${formName}.input.firstname.label`)}
      placeholder={t(`${formName}.input.firstname.placeholder`)}
    />
  );
}

export default FirstnameInput;
