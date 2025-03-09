import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Step } from "../types/step.ts";

export function useStepperNavigation(steps: Step[], initialPath: string = "/") {
  const [active, setActive] = useState(0);
  const navigate = useNavigate();

  function nextStep() {
    setActive((current) => (current < steps.length ? current + 1 : current));
  }

  function prevStep() {
    if (active === 0) {
      navigate(initialPath, { replace: true });
    }
    setActive((current) => (current > 0 ? current - 1 : current));
  }

  return { active, setActive, nextStep, prevStep };
}
