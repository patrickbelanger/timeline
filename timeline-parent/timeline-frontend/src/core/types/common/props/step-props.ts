export interface StepProps {
  prevStep: () => Promise<void>;
  nextStep: () => void;
}
