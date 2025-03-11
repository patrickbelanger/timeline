import { UseFormReturnType } from "@mantine/form";

export interface InputElementProps<T extends Record<string, any>> {
  form: UseFormReturnType<T>;
  formName?: string;
  withAsterisk?: boolean;
}
