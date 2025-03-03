import { PasswordInput as MantinePasswordInput } from "@mantine/core";
import { IconLockPassword } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { InputElementProps } from "./input-props.ts";

function PasswordInput<T extends Record<string, any>>({
  form,
  fieldName = "password",
  formName = "login",
}: InputElementProps<T> & { fieldName?: keyof T }) {
  const { t } = useTranslation();
  const iconLockPassword = <IconLockPassword size={16} />;

  return (
    <MantinePasswordInput
      {...form.getInputProps(fieldName)}
      name={String(fieldName)}
      key={form.key(fieldName)}
      data-testid={`${String(fieldName)}-input`}
      radius="md"
      leftSectionPointerEvents="none"
      leftSection={iconLockPassword}
      label={t(`${formName}.input.${String(fieldName)}.label`)}
      placeholder={t(`${formName}.input.${String(fieldName)}.placeholder`)}
    />
  );
}

export default PasswordInput;
