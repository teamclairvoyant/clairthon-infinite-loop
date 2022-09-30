import React from "react";
import { render } from "@testing-library/react";
import { fireEvent, screen } from "@testing-library/dom";
import AddEvent from "./AddEvent";
import { COPY, TEST_ID } from "../../../../constants/constant";
import dayjs from "dayjs";
import {
  mailIdOptions,
  dateOptions,
} from "../../../../common/MockData/mockTestData";

const { date, dateHourLater } = dateOptions;
const setEventModal = jest.fn();

describe("AddEvent", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  test("renders add event compoent", () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();
  });

  test("it test the input box value", () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const title = screen.getByTestId(COPY.TITLE);
    fireEvent.change(title, { target: { value: "Interview SLot" } });
    expect(title.value).toStrictEqual("Interview SLot");

    const description = screen.getByTestId(COPY.DESCRIPTION);
    fireEvent.change(description, {
      target: { value: "Interview SLot description" },
    });
    expect(description.value).toStrictEqual("Interview SLot description");

    const dateInput = screen.getByPlaceholderText("Enter Date");
    fireEvent.change(dateInput, {
      target: { value: date },
    });
    expect(dateInput.value).toStrictEqual(dayjs(date).format("DD-MM-YYYY"));

    const startTimeInput = screen.getByPlaceholderText("Start Time");
    fireEvent.change(startTimeInput, {
      target: { value: date },
    });
    expect(startTimeInput.value).toStrictEqual(dayjs(date).format("h:mm A"));

    const endTimeInput = screen.getByPlaceholderText("End Time");
    fireEvent.change(endTimeInput, {
      target: { value: dateHourLater },
    });
    expect(endTimeInput.value).toStrictEqual(
      dayjs(dateHourLater).format("h:mm A")
    );

    const attendees = screen.getByTestId("attendees");
  });

  test("it test the error message except title", async () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const title = screen.getByTestId(COPY.TITLE);
    fireEvent.change(title, { target: { value: "Interview SLot" } });
    expect(title.value).toStrictEqual("Interview SLot");

    const description = screen.getByTestId(COPY.DESCRIPTION);
    fireEvent.change(description, {
      target: { value: "" },
    });
    const submitButton = screen.getByTestId(TEST_ID.COMMON.SUMBIT_BUTTON);
    fireEvent.click(submitButton);
    const requiredErrorMessage = await screen.findAllByText(
      COPY.REQUIRED_ERROR_MESSAGE
    );
    expect(requiredErrorMessage.length).toEqual(2);
  });

  test("it test the error message date", async () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const title = screen.getByTestId(COPY.TITLE);
    fireEvent.change(title, { target: { value: "Interview SLot" } });
    expect(title.value).toStrictEqual("Interview SLot");

    const description = screen.getByTestId(COPY.DESCRIPTION);
    fireEvent.change(description, {
      target: { value: "Interview Slot desc" },
    });
    expect(description.value).toStrictEqual("Interview Slot desc");

    const dateInput = screen.getByPlaceholderText("Enter Date");
    fireEvent.change(dateInput, {
      target: { value: "" },
    });

    const submitButton = screen.getByTestId(TEST_ID.COMMON.SUMBIT_BUTTON);
    fireEvent.click(submitButton);
    const requiredErrorMessage = await screen.findAllByText(
      COPY.REQUIRED_ERROR_MESSAGE
    );
    expect(requiredErrorMessage.length).toEqual(1);
  });

  test("it test the error message startTime and endTime", async () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const title = screen.getByTestId(COPY.TITLE);
    fireEvent.change(title, { target: { value: "Interview SLot" } });
    expect(title.value).toStrictEqual("Interview SLot");

    const description = screen.getByTestId(COPY.DESCRIPTION);
    fireEvent.change(description, {
      target: { value: "Interview Slot desc" },
    });

    const dateInput = screen.getByPlaceholderText("Enter Date");
    fireEvent.change(dateInput, {
      target: { value: date },
    });

    const startTimeInput = screen.getByPlaceholderText("Start Time");
    fireEvent.change(startTimeInput, {
      target: { value: date },
    });

    const submitButton = screen.getByTestId(TEST_ID.COMMON.SUMBIT_BUTTON);
    fireEvent.click(submitButton);
    const requiredErrorMessage = await screen.findAllByText(
      COPY.REQUIRED_ERROR_MESSAGE
    );
    expect(requiredErrorMessage.length).toEqual(1);
  });

  test("it test the error message attendees", async () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const title = screen.getByTestId(COPY.TITLE);
    fireEvent.change(title, { target: { value: "Interview SLot" } });

    const description = screen.getByTestId(COPY.DESCRIPTION);
    fireEvent.change(description, {
      target: { value: "Interview SLot description" },
    });
    const dateInput = screen.getByPlaceholderText("Enter Date");
    fireEvent.change(dateInput, {
      target: { value: date },
    });

    const startTimeInput = screen.getByPlaceholderText("Start Time");
    fireEvent.change(startTimeInput, {
      target: { value: date },
    });

    const endTimeInput = screen.getByPlaceholderText("End Time");
    fireEvent.change(endTimeInput, {
      target: { value: dateHourLater },
    });

    const submitButton = screen.getByTestId(TEST_ID.COMMON.SUMBIT_BUTTON);
    fireEvent.click(submitButton);
    const requiredErrorMessage = await screen.findAllByText(
      COPY.REQUIRED_ERROR_MESSAGE
    );
    expect(requiredErrorMessage.length).toEqual(1);
  });

  test("it test the error message for field empty", async () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const submitButton = screen.getByTestId(TEST_ID.COMMON.SUMBIT_BUTTON);
    fireEvent.click(submitButton);
    const requiredErrorMessage = screen.getAllByText(
      COPY.REQUIRED_ERROR_MESSAGE
    );
    expect(requiredErrorMessage.length).toEqual(3);

    const requiredErrorMessageDate = screen.getAllByText(
      COPY.REQUIRED_DATE_ERROR_MESSAGE
    );
    expect(requiredErrorMessageDate.length).toEqual(1);

    const title = screen.getByTestId(COPY.TITLE);
    fireEvent.change(title, { target: { value: "Interview SLot" } });
    expect(title.value).toStrictEqual("Interview SLot");

    const requiredErrorMessageOnChange = screen.getAllByText(
      COPY.REQUIRED_ERROR_MESSAGE
    );
    expect(requiredErrorMessageOnChange.length).toEqual(2);
  });

  test("it test the cancel button", async () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const cancelButton = screen.getByTestId(TEST_ID.COMMON.ON_FORM_CANCEL);
    fireEvent.click(cancelButton);
  });

  test("it test the cancel anchor tag", async () => {
    const { container } = render(
      <AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />
    );
    expect(container).toBeDefined();

    const cancelAnchorTag = screen.getByTestId(
      TEST_ID.COMMON.ON_FORM_CANCEL_ANCHOR
    );
    fireEvent.click(cancelAnchorTag);
  });
});
