/* eslint-disable react/prop-types */
import React from 'react';
import Select from 'react-select';

const RSelect = ({ ...props }) => {
  const { handleFilter } = props;
  return (
    <div
      className="form-control-select"
      onKeyUp={(e) => handleFilter?.(e)}
      data-testid={props.dataTestId}>
      <Select
        className={`react-select-container ${props.className ? props.className : ''}`}
        classNamePrefix="react-select"
        {...props}
      />
    </div>
  );
};

export default RSelect;
