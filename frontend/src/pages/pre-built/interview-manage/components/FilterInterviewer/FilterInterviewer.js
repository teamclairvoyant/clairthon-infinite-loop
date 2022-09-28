import React, { useState } from "react";
import { FormGroup } from "reactstrap";
import { COPY } from "../../../../../constants/constant";
import { RSelect, Row, Col } from "../../../../../components/Component";
import { filterExperience } from "../../../../../common/listing/ListingData";
import { ErrorMessage } from "../../../../../components/error-message/ErrorMessage";
import DatePicker from "react-datepicker";
import { getDateNTime } from "../../../../../utils/Utils";
import { useListContext } from "../../../../../context/listContext";
const FilterInterviewer = (props) => {
  const { filter, addFilter, applyFilter, resetFilter } = props;
  const { skillOptions, locationOptions } = useListContext();
  const [error, setError] = useState({
    filterEmpty: false,
    errorMessage: "",
  });

  const filterHandler = () => {
    const {
      location,
      skills,
      experience,
      endDate,
      startTime,
      startDate,
      endTime,
    } = filter;

    if (startDate || endDate) {
      if (!(endDate & startDate)) {
        setError({
          ...error,
          filterEmpty: true,
          errorMessage: COPY.SINGLE_DATE_ERROR,
        });
        return;
      }

      const startDateTime = getDateNTime(startDate, startTime);
      const endDateTime = getDateNTime(endDate, endTime);
      if (new Date(startDateTime) >= new Date(endDateTime)) {
        setError({
          ...error,
          filterEmpty: true,
          errorMessage: COPY.FROM_DATE_GREATER_ERROR,
        });
        return;
      }
    }

    if (
      !(
        location.length ||
        skills.length ||
        experience.value ||
        startDate & endDate
      )
    ) {
      setError({
        ...error,
        filterEmpty: true,
        errorMessage: COPY.REQUIRED_FIELD_FILTER_ERROR,
      });
      return;
    }

    applyFilter();
  };
  return (
    <React.Fragment>
      <div className="dropdown-head">
        <span className="sub-title dropdown-title">
          {COPY.FILTER_INTERVIEWERS}
        </span>
      </div>
      <div className="dropdown-body dropdown-body-rg">
        <Row className="gx-6 gy-3">
          <Col size="6">
            <FormGroup>
              <label className="overline-title overline-title-alt">
                {COPY.LOCATION}
              </label>
              <RSelect
                isMulti
                closeMenuOnSelect={false}
                options={locationOptions}
                defaultValue={filter.location}
                onChange={(selectedLocation) =>
                  addFilter({
                    ...filter,
                    location: selectedLocation,
                  })
                }
              />
            </FormGroup>
          </Col>

          <Col size="6">
            <FormGroup>
              <label className="overline-title overline-title-alt">
                {COPY.EXPERIENCE}
              </label>
              <RSelect
                options={filterExperience}
                defaultValue={filter.experience}
                onChange={(selectedExperience) =>
                  addFilter({
                    ...filter,
                    experience: selectedExperience,
                  })
                }
              />
            </FormGroup>
          </Col>
          <Col size="12">
            <FormGroup>
              <label className="form-label">Start Date &amp; Time</label>
              <Row className="gx-2">
                <div className="w-55">
                  <div className="form-control-wrap">
                    <DatePicker
                      selected={filter.startDate}
                      dateFormat="dd-MM-yyyy"
                      placeholderText="Enter Time"
                      onChange={(date) =>
                        addFilter({ ...filter, startDate: date })
                      }
                      className="form-control date-picker"
                    />
                  </div>
                </div>
                <div className="w-45">
                  <div className="form-control-wrap has-timepicker">
                    <DatePicker
                      selected={filter.startTime}
                      onChange={(date) =>
                        addFilter({ ...filter, startTime: date })
                      }
                      showTimeSelect
                      placeholderText="Enter Date"
                      showTimeSelectOnly
                      timeIntervals={30}
                      timeCaption="Time"
                      dateFormat="h:mm aa"
                      className="form-control date-picker"
                    />
                  </div>
                </div>
              </Row>
            </FormGroup>
          </Col>
          <Col size="12">
            <FormGroup>
              <label className="form-label">End Date &amp; Time</label>
              <Row className="gx-2">
                <div className="w-55">
                  <div className="form-control-wrap">
                    <DatePicker
                      selected={filter.endDate}
                      placeholderText="Enter Date"
                      dateFormat="dd-MM-yyyy"
                      onChange={(date) =>
                        addFilter({ ...filter, endDate: date })
                      }
                      className="form-control date-picker"
                    />
                  </div>
                </div>
                <div className="w-45">
                  <div className="form-control-wrap has-timepicker">
                    <DatePicker
                      selected={filter.endTime}
                      onChange={(date) =>
                        addFilter({ ...filter, endTime: date })
                      }
                      showTimeSelect
                      placeholderText="Enter Time"
                      showTimeSelectOnly
                      timeIntervals={15}
                      timeCaption="Time"
                      dateFormat="h:mm aa"
                      className="form-control date-picker"
                    />
                  </div>
                </div>
              </Row>
            </FormGroup>
          </Col>
          <Col size="12">
            <FormGroup>
              <label className="overline-title overline-title-alt">
                {COPY.SKILLS}
              </label>
              <RSelect
                isMulti
                closeMenuOnSelect={false}
                defaultValue={filter.skills}
                options={skillOptions}
                onChange={(selectedSkills) =>
                  addFilter({
                    ...filter,
                    skills: selectedSkills,
                  })
                }
              />
            </FormGroup>
          </Col>
          {error.filterEmpty && (
            <ErrorMessage
              content={error.errorMessage}
              style={{ color: "red", marginLeft: "1rem" }}
            />
          )}
        </Row>
      </div>
      <div className="dropdown-foot between">
        <a
          href="#reset"
          onClick={(ev) => {
            ev.preventDefault();
            resetFilter();
          }}
          className="clickable"
        >
          {COPY.RESET_FILTER}
        </a>
        <button
          type="button"
          className="btn btn-secondary"
          onClick={filterHandler}
        >
          {COPY.APPLY_FILTER}
        </button>
      </div>
    </React.Fragment>
  );
};
export default FilterInterviewer;
