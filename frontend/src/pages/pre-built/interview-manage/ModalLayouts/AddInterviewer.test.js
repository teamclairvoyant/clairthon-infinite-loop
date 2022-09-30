import React from "react";
import { render } from "@testing-library/react";
import { fireEvent, screen } from "@testing-library/dom";
import AddInterviewer from "./AddInterviewer";
import { COPY, TEST_ID } from "../../../../constants/constant";
import {
  filterExperience,
  filterLocation,
  filterSkills,
} from "../../../../common/listing/ListingData";
import { ListContext } from "../../../../context/listContext";
import { listing } from "../../../../common/MockData/mockTestData";
const onFormCancel = jest.fn();
const interviewerValidationError = "";
const addInterviewers = jest.fn();
describe("AddInterviewerModal", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  test("it renders the add interviewer modal", () => {
    const { container } = render(
      <ListContext.Provider value={listing}>
        <AddInterviewer
          onFormCancel={onFormCancel}
          interviewerValidationError={interviewerValidationError}
          addInterviewers={addInterviewers}
        />
      </ListContext.Provider>
    );

    expect(container).toBeDefined();
  });

  test("it test the input box value", async () => {
    const { container } = render(
      <ListContext.Provider value={listing}>
        <AddInterviewer
          onFormCancel={onFormCancel}
          interviewerValidationError={interviewerValidationError}
          addInterviewers={addInterviewers}
        />
      </ListContext.Provider>
    );
    expect(container).toBeDefined();

    const title = screen.getByPlaceholderText(COPY.PLACEHOLDER_NAME);
    fireEvent.change(title, { target: { value: "Test user" } });
    expect(title.value).toStrictEqual("Test user");

    const employeeNo = screen.getByPlaceholderText(
      COPY.PLACEHOLDER_EMPLOYEE_NO
    );
    fireEvent.change(employeeNo, {
      target: { value: "P01234" },
    });
    expect(employeeNo.value).toStrictEqual("P01234");

    const email = screen.getByPlaceholderText(COPY.PLACEHOLDER_EMAIL);
    fireEvent.change(email, {
      target: { value: "test@gmail.com" },
    });
    expect(email.value).toStrictEqual("test@gmail.com");

    const phone = screen.getByPlaceholderText(COPY.PLACEHOLDER_PHONE);
    fireEvent.change(phone, {
      target: { value: "9897567858" },
    });
    expect(phone.value).toStrictEqual("9897567858");

    expect(screen.getByTestId(TEST_ID.ADD_EVENT.FORM)).toHaveFormValues({
      [COPY.LOCATION]: filterLocation[0].value,
    });

    expect(screen.getByTestId(TEST_ID.ADD_EVENT.FORM)).toHaveFormValues({
      [COPY.EXPERIENCE]: filterExperience[0].value.toString(),
    });

    expect(screen.getByTestId(TEST_ID.ADD_EVENT.FORM)).toHaveFormValues({
      [COPY.SKILLS]: filterSkills[0].value,
    });

    const submitButton = screen.getByTestId(TEST_ID.COMMON.SUMBIT_BUTTON);
    fireEvent.click(submitButton);
  });

  test("it test the error message for field empty", async () => {
    const { container } = render(
      <ListContext.Provider value={listing}>
        <AddInterviewer
          onFormCancel={onFormCancel}
          interviewerValidationError={interviewerValidationError}
          addInterviewers={addInterviewers}
        />
      </ListContext.Provider>
    );
    expect(container).toBeDefined();

    const submitButton = screen.getByTestId(TEST_ID.COMMON.SUMBIT_BUTTON);
    fireEvent.click(submitButton);
    const requiredErrorMessage = await screen.findAllByText(
      COPY.REQUIRED_ERROR_MESSAGE
    );
    expect(requiredErrorMessage.length).toEqual(4);
  });

  test("it test the cancel button", async () => {
    const { container } = render(
      <ListContext.Provider value={listing}>
        <AddInterviewer
          onFormCancel={onFormCancel}
          interviewerValidationError={interviewerValidationError}
          addInterviewers={addInterviewers}
        />
      </ListContext.Provider>
    );
    expect(container).toBeDefined();

    const cancelButton = screen.getByTestId(TEST_ID.COMMON.ON_FORM_CANCEL);
    fireEvent.click(cancelButton);
  });

  test("it test the cancel anchor tag", async () => {
    const { container } = render(
      <ListContext.Provider value={listing}>
        <AddInterviewer
          onFormCancel={onFormCancel}
          interviewerValidationError={interviewerValidationError}
          addInterviewers={addInterviewers}
        />
      </ListContext.Provider>
    );
    expect(container).toBeDefined();

    const cancelAnchorTag = screen.getByTestId(
      TEST_ID.COMMON.ON_FORM_CANCEL_ANCHOR
    );
    fireEvent.click(cancelAnchorTag);
  });
});
