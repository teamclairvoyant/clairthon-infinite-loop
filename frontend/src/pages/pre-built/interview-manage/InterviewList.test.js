import React from "react";
import { render, screen } from "@testing-library/react";
import InterviewList from "./InterviewList";
// import { UserContextProvider } from "../UserContext";
import { UserContextProvider } from "./UserContext";

describe("InterviewList", () => {
  test("renders learn react link", () => {
    const { container } = render(
      <UserContextProvider>
        <InterviewList />
      </UserContextProvider>
    );
    expect(container).toBeDefined();
  });
});
