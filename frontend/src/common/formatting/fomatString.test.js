import formatString from "./formatString";

describe("It test formatString", () => {
  test("It gives a formatted string", () => {
    const resultStr =
      "This is Interview Slot Planner application for the hackathon";
    const testStr = "This is {0} application for the {1}";

    expect(
      formatString(testStr, "Interview Slot Planner", "hackathon")
    ).toStrictEqual(resultStr);
  });
});
