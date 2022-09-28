import React from "react";
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <div className="nk-footer">
      <div className="container-fluid">
        <div className="nk-footer-wrap">
          <div className="nk-footer-copyright"> &copy; 2022 Infinite Loop</div>
          <div className="nk-footer-links">
            <ul className="nav nav-sm">
              <li className="nav-item">
                <Link
                  // to={`${process.env.PUBLIC_URL}/pages/terms-policy`}
                  to={`${process.env.PUBLIC_URL}/`}
                  className="nav-link"
                >
                  Terms
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  to={`${process.env.PUBLIC_URL}/`}
                  // to={`${process.env.PUBLIC_URL}/pages/faq`}
                  className="nav-link"
                >
                  Privacy
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  // to={`${process.env.PUBLIC_URL}/pages/terms-policy`}
                  to={`${process.env.PUBLIC_URL}/`}
                  className="nav-link"
                >
                  Help
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Footer;