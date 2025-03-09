import React from "react";

export interface Step {
  label: string;
  description: string;
  component: (props: {
    nextStep: () => void;
    prevStep: () => void;
  }) => React.ReactNode;
}
