import React from "react";
import { render } from "@testing-library/react";
import InterviewList from "./InterviewList";
import { UserContextProvider } from "./UserContext";
import { ListContext } from "../../../context/listContext";
import { listing } from "../../../common/MockData/mockTestData";
import mockInterviewListResponse from "../../../common/MockData/mockInterviewList";
import { request } from "../../../utils/axiosUtils";

jest.mock("../../../utils/axiosUtils", () => ({
  request: ({ ...options }) =>
    new Promise((resolve, reject) => resolve(mockInterviewListResponse)),
}));

describe("InterviewList", () => {
  test("renders learn react link", () => {
    const { container } = render(
      <UserContextProvider>
        <ListContext.Provider value={listing}>
          <InterviewList />
        </ListContext.Provider>
      </UserContextProvider>
    );
    expect(container).toBeDefined();
  });

  test("it check the API call", () => {
    const { container } = render(
      <UserContextProvider>
        <ListContext.Provider value={listing}>
          <InterviewList />
        </ListContext.Provider>
      </UserContextProvider>
    );
    expect(container).toBeDefined();
  });
});
