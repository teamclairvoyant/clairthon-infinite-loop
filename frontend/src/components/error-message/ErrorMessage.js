import React from "react";

export const ErrorMessage = ({ content, style }) => {
  const errorMessageStyle = {
    color: "#e85347",
    fontSize: "11px",
    fontStyle: "italic",
  };

  return <span style={style ?? errorMessageStyle}>{content ?? null}</span>;
};
