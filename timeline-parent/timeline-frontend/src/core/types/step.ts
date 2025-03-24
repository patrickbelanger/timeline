import React from "react";

export interface Step {
  label: string;
  description: string;
  component: (props: {
    prevStep: () => Promise<void>;
    nextStep: () => {};
  }) => React.ReactNode;
}
