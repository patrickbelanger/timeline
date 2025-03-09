import { describe, it, expect } from "vitest";
import { TFunction } from "i18next";
import { getStep } from "./signup-strategy.tsx";

const mockT: TFunction = ((key: string) => key) as TFunction;
const numberToWord = ["one", "two", "three"];

describe("getStep", () => {
  it("should return an array of steps with correct labels and descriptions", () => {
    const steps = getStep(mockT);
    expect(steps).toHaveLength(3);
    steps.forEach((step, index) => {
      expect(step.label).toBe(
        `signup.steppers.step.${numberToWord[index]}.label`,
      );
      expect(step.description).toBe(
        `signup.steppers.step.${numberToWord[index]}.description`,
      );
      expect(step.component).toBeTruthy();
    });
  });
});
