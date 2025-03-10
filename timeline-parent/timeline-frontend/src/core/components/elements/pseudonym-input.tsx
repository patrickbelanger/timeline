import { TextInput } from "@mantine/core";
import { IconEyeglass } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { InputElementProps } from "./input-element-props.ts";
import { ComponentProps } from "react";

type TextInputPropsWithoutForm = Omit<ComponentProps<typeof TextInput>, "form">;

function PseudonymInput<T extends { firstName: string }>({
  form,
  formName = "signup",
  ...props
}: InputElementProps<T> & TextInputPropsWithoutForm) {
  const { t } = useTranslation();
  const iconAt = <IconEyeglass size={16} />;

  return (
    <TextInput
      {...form.getInputProps("pseudonym")}
      name="pseudonym"
      key={form.key("pseudonym")}
      data-testid="pseudonym-input"
      radius="md"
      leftSectionPointerEvents="none"
      leftSection={iconAt}
      label={t(`${formName}.input.pseudonym.label`)}
      placeholder={t(`${formName}.input.pseudonym.placeholder`)}
      {...props}
    />
  );
}

export default PseudonymInput;
