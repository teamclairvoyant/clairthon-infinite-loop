import React from "react";
import { render } from "@testing-library/react";
import Layout from "./Index";
import { MemoryRouter } from "react-router-dom";
import { AuthProvider } from "../context/authContext";

describe("LayoutIndex", () => {
  test("it renders the Layout", () => {
    const { container } = render(
      <MemoryRouter>
        <AuthProvider>
          <Layout />
        </AuthProvider>
      </MemoryRouter>
    );

    expect(container).toBeDefined();
  });
});
