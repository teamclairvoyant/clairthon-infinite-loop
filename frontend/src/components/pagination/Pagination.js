import React from "react";
import Icon from "../icon/Icon";
import { Pagination, PaginationLink, PaginationItem } from "reactstrap";
import { TEST_ID } from "../../constants/constant";

const PaginationComponent = ({
  itemPerPage,
  totalItems,
  paginate,
  currentPage,
}) => {
  const pageNumbers = [];
  console.log({ itemPerPage, totalItems, paginate, currentPage });
  for (let i = 1; i <= Math.ceil(totalItems / itemPerPage); i++) {
    pageNumbers.push(i);
  }

  const nextPage = () => {
    paginate(currentPage + 1);
  };

  const prevPage = () => {
    paginate(currentPage - 1);
  };

  return (
    <Pagination aria-label="Page navigation example">
      <PaginationItem disabled={currentPage - 1 === 0 ? true : false}>
        <PaginationLink
          className="page-link-prev"
          onClick={(ev) => {
            ev.preventDefault();
            prevPage();
          }}
          data-testid={TEST_ID.PAGINATION.PREV_PAGE}
          href="#prev"
        >
          <Icon name="chevrons-left" />
          <span>Prev</span>
        </PaginationLink>
      </PaginationItem>
      {pageNumbers.map((item) => {
        return (
          <PaginationItem
            className={currentPage === item ? "active" : ""}
            key={item}
          >
            <PaginationLink
              tag="a"
              href="#pageitem"
              onClick={(ev) => {
                ev.preventDefault();
                paginate(item);
              }}
              data-testid={TEST_ID.PAGINATION.ITEM_PAGE}
            >
              {item}
            </PaginationLink>
          </PaginationItem>
        );
      })}

      <PaginationItem
        disabled={pageNumbers[pageNumbers.length - 1] === currentPage}
      >
        <PaginationLink
          className="page-link-next"
          onClick={(ev) => {
            ev.preventDefault();
            nextPage();
          }}
          href="#next"
          data-testid={TEST_ID.PAGINATION.NEXT_PAGE}
        >
          <span>Next</span>
          <Icon name="chevrons-right" />
        </PaginationLink>
      </PaginationItem>
    </Pagination>
  );
};
export default PaginationComponent;
