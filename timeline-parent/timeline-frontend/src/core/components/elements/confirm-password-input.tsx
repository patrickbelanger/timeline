import PasswordInput from "./password-input.tsx";
import { InputElementProps } from "./input-element-props.ts";

function ConfirmPasswordInput<T extends { confirmPassword: string }>(
  props: InputElementProps<T>,
) {
  return <PasswordInput {...props} fieldName="confirmPassword" />;
}

export default ConfirmPasswordInput;
