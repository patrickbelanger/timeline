import { describe, it, vi, expect, beforeEach, Mock } from "vitest";
import { createRoot } from "react-dom/client";

vi.mock("react-dom/client", () => ({
  createRoot: vi.fn(() => ({
    render: vi.fn(),
  })),
}));

describe("main.tsx", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    import("./main");
  });

  it("renders the App component inside StrictMode", async () => {
    await import("./main");

    expect(createRoot).toHaveBeenCalledTimes(1);
    expect(createRoot).toHaveBeenCalledWith(document.getElementById("root"));

    const mockRender = (createRoot as Mock).mock.results[0].value.render;
    expect(mockRender).toHaveBeenCalledWith(expect.any(Object));
  });
});
