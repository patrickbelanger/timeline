import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { configDefaults } from "vitest/config";

export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: "./vitest.setup.mjs",
    reporters: ["verbose"],
    coverage: {
      exclude: [
        ...configDefaults.exclude,
        ".vite-env.d.ts",
        "**/types/**",
        "**/node_modules/**",
        "**/dist/**",
        "**/cypress/**",
        "**/.{idea,git,cache,output,temp}/**",
        "./src/config/**",
        "**/*.{-illustration}.?(c|m)[jt]s?(x)",
        "**/postcss.config.cjs",
      ],
    },
  },
});
