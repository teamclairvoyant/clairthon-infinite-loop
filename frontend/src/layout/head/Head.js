import React from 'react';
import { Helmet } from 'react-helmet';
import PropTypes from 'prop-types';

const Head = ({ ...props }) => {
  return (
    <Helmet>
      <title>{props.title ? props.title + ' | ' : null} Interview Slot Planner</title>
    </Helmet>
  );
};
export default Head;

Head.propTypes = {
  title: PropTypes.string
};
