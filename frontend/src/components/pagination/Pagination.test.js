import React from "react";
import { render } from "@testing-library/react";
import { fireEvent, screen } from "@testing-library/dom";
import Pagination from "./Pagination";
import { TEST_ID } from "../../constants/constant";

const itemPerPage = 10;
const totalItems = 10;
const currentPage = 10;
const paginate = jest.fn();

describe("Pagination", () => {
  test("it renders the Pagination", () => {
    const { container } = render(
      <Pagination
        itemPerPage={itemPerPage}
        totalItems={totalItems}
        currentPage={currentPage}
        paginate={paginate}
      />
    );

    expect(container).toBeDefined();
  });

  test("it test the next page click", () => {
    const { container } = render(
      <Pagination
        itemPerPage={itemPerPage}
        totalItems={totalItems}
        currentPage={currentPage}
        paginate={paginate}
      />
    );

    expect(container).toBeDefined();

    const nextPageClick = screen.getByTestId(TEST_ID.PAGINATION.NEXT_PAGE);
    fireEvent.click(nextPageClick);
  });

  test("it test the prev page click", () => {
    const { container } = render(
      <Pagination
        itemPerPage={itemPerPage}
        totalItems={totalItems}
        currentPage={currentPage}
        paginate={paginate}
      />
    );

    expect(container).toBeDefined();

    const prevPageClick = screen.getByTestId(TEST_ID.PAGINATION.PREV_PAGE);
    fireEvent.click(prevPageClick);
  });

  test("it test the item page click", () => {
    const { container } = render(
      <Pagination
        itemPerPage={itemPerPage}
        totalItems={totalItems}
        currentPage={currentPage}
        paginate={paginate}
      />
    );

    expect(container).toBeDefined();

    const itemPageClick = screen.getByTestId(TEST_ID.PAGINATION.ITEM_PAGE);
    fireEvent.click(itemPageClick);
  });
});
