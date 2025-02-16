import { useEffect, useState } from "react";
import { useMantineTheme, Box, Text } from "@mantine/core";

function DebugGrid() {
  const theme = useMantineTheme();
  const [currentBreakpoint, setCurrentBreakpoint] = useState("");

  useEffect(() => {
    function handleResize() {
      const width = window.innerWidth;
      const breakpointsPx = {
        xs: parseFloat(theme.breakpoints.xs) * 16,
        sm: parseFloat(theme.breakpoints.sm) * 16,
        md: parseFloat(theme.breakpoints.md) * 16,
        lg: parseFloat(theme.breakpoints.lg) * 16,
        xl: parseFloat(theme.breakpoints.xl) * 16,
      };

      if (width >= breakpointsPx.xl) {
        setCurrentBreakpoint("xl");
      } else if (width >= breakpointsPx.lg) {
        setCurrentBreakpoint("lg");
      } else if (width >= breakpointsPx.md) {
        setCurrentBreakpoint("md");
      } else if (width >= breakpointsPx.sm) {
        setCurrentBreakpoint("sm");
      } else {
        setCurrentBreakpoint("xs");
      }
    }

    handleResize();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [theme.breakpoints]);

  return (
    <Box>
      <Text size="sm">Current Breakpoint: {currentBreakpoint}</Text>
    </Box>
  );
}

export default DebugGrid;
