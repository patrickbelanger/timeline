import { render } from "@testing-library/react";
import App from "./App.tsx";

describe("App", () => {
  it("renders the App component", () => {
    const { container } = render(<App />);
    expect(container).toBeTruthy();
  });
});
