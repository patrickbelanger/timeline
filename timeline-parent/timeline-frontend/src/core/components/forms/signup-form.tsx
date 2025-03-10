import DebugGrid from "../containers/utils/debug-grid.tsx";
import { Box, Fieldset, Grid, Space, Stepper } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { getStep } from "./strategy/signup-strategy.tsx";
import { useStepperNavigation } from "../../hooks/useStepperNavigation.ts";
import { useEffect } from "react";

function SignUpForm() {
  const { t } = useTranslation();
  const steps = getStep(t);
  const { active, nextStep, prevStep } = useStepperNavigation(steps);

  useEffect(() => {
    console.log("🎯 Active state changed:", active);
  }, [active]);

  return (
    <>
      <DebugGrid />
      <Box pos="relative">
        <Grid
          grow
          type="container"
          breakpoints={{
            xs: "100px",
            sm: "200px",
            md: "300px",
            lg: "400px",
            xl: "500px",
          }}
        >
          <Grid.Col span={{ md: 4, lg: 2 }} visibleFrom="sm" />
          <Grid.Col span={{ xs: 12, sm: 10, md: 4, lg: 6 }}>
            <Space h="xl" />
            <Fieldset variant="filled">
              <Stepper active={active} allowNextStepsSelect={false}>
                {steps.map((step, index) => (
                  <Stepper.Step
                    key={index}
                    label={step.label}
                    description={step.description}
                  >
                    {step.component({ nextStep, prevStep })}
                  </Stepper.Step>
                ))}
                <Stepper.Completed>Placeholder</Stepper.Completed>
              </Stepper>
            </Fieldset>
          </Grid.Col>
          <Grid.Col span={{ md: 4, lg: 2 }} visibleFrom="sm" />
        </Grid>
      </Box>
    </>
  );
}

export default SignUpForm;
