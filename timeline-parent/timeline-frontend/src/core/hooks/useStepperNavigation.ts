import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Step } from "../types/step.ts";

export function useStepperNavigation(
  steps: Step[],
  initialPath: string = "/",
  doNavigateBack: boolean = true,
) {
  const [active, setActive] = useState(0);
  const navigate = useNavigate();

  function nextStep() {
    setActive((prevState) => {
      const newState = prevState < steps.length - 1 ? prevState + 1 : prevState;
      console.log(
        "🔄 nextStep triggered. Before:",
        prevState,
        "After:",
        newState,
      );
      return newState;
    });
  }

  async function prevStep() {
    if (active === 0 && doNavigateBack) {
      navigate(initialPath, { replace: true });
    }
    setActive((prevState) => (prevState > 0 ? prevState - 1 : prevState));
  }

  return { active, setActive, nextStep, prevStep };
}
