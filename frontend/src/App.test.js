import { render, screen } from "@testing-library/react";
import App from "./App";
import { MemoryRouter } from "react-router-dom";
import { AuthProvider } from "./context/authContext";
test("renders learn react link", () => {
  const { container } = render(
    <MemoryRouter>
      <AuthProvider>
        <App />
      </AuthProvider>
    </MemoryRouter>
  );
  expect(container).toBeDefined();
  const linkElement = screen.getByText(/Sign-In/i);
  expect(linkElement).toBeInTheDocument();
});
