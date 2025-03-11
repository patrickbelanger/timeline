import { Textarea } from "@mantine/core";
import { IconUserScan } from "@tabler/icons-react";
import { useTranslation } from "react-i18next";
import { InputElementProps } from "./input-element-props.ts";

function BioTextarea<T extends { bio: string }>({
  form,
  formName = "signup",
}: InputElementProps<T>) {
  const { t } = useTranslation();
  const iconUserScan = <IconUserScan size={16} />;

  return (
    <Textarea
      {...form.getInputProps("bio")}
      name="bio"
      key={form.key("bio")}
      data-testid="bio-textarea"
      radius="md"
      leftSectionPointerEvents="none"
      leftSection={iconUserScan}
      label={t(`${formName}.textarea.bio.label`)}
      placeholder={t(`${formName}.textarea.bio.placeholder`)}
      minRows={4}
      maxRows={4}
    />
  );
}

export default BioTextarea;
