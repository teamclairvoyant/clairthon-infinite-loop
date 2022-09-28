import React, { useState } from "react";
import { Icon, Col, Button, RSelect } from "../../../../components/Component";

import { FormGroup, Form } from "reactstrap";
import { COPY } from "../../../../constants/constant";
import { Controller, useForm } from "react-hook-form";
import {
  filterExperience,
  filterLocation,
} from "../../../../common/listing/ListingData";
import { ErrorMessage } from "../../../../components/error-message/ErrorMessage";

const errorMessageStyle = {
  color: "#e85347",
  fontSize: "11px",
  fontStyle: "italic",
};

const EditInterviewer = (props) => {
  const { onFormCancel, onEditSubmit, formData, filterSkills } = props;
  const { errors, register, handleSubmit, control } = useForm();

  const [error, setError] = useState({
    isSkillEmpty: false,
    isLocationEmpty: false,
    errorMessage: "This field is required!",
  });

  const handleUpdateButton = (interviewerData) => {
    const { skills, location } = interviewerData;

    if (!skills.length) {
      setError({
        ...error,
        isSkillEmpty: true,
      });

      return;
    }

    if (!location.value) {
      setError({
        ...error,
        isLocationEmpty: true,
      });

      return;
    }

    onEditSubmit(interviewerData);
  };
  return (
    <React.Fragment>
      <a
        href="#cancel"
        onClick={(ev) => {
          ev.preventDefault();
          onFormCancel();
        }}
        className="close"
      >
        <Icon name="cross-sm"></Icon>
      </a>
      <div className="p-2">
        <h5 className="title">{COPY.UPDATE_USER}</h5>
        <div className="mt-4">
          <Form
            className="row gy-4"
            onSubmit={handleSubmit(handleUpdateButton)}
          >
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.NAME}</label>
                <input
                  className="form-control"
                  type="text"
                  name="name"
                  defaultValue={formData.name}
                  placeholder="Enter name"
                  ref={register({ required: "This field is required" })}
                />
                {errors.name && (
                  <span className="invalid">{errors.name.message}</span>
                )}
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.EMPLOYEE_NO} </label>
                <input
                  className="form-control"
                  type="text"
                  name="employee_no"
                  defaultValue={formData.employee_no}
                  placeholder="Enter Employee No"
                  ref={register({
                    required: "This field is required",
                  })}
                />
                {errors.employee_no && (
                  <span className="invalid">{errors.employee_no.message}</span>
                )}
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.EMAIL}</label>
                <input
                  className="form-control"
                  type="text"
                  name="email"
                  defaultValue={formData.email}
                  placeholder="Enter email"
                  ref={register({
                    required: "This field is required",
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                      message: "invalid email address",
                    },
                  })}
                />
                {errors.email && (
                  <span className="invalid">{errors.email.message}</span>
                )}
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.PHONE}</label>
                <input
                  className="form-control"
                  type="text"
                  name="phone"
                  defaultValue={formData.phone}
                  ref={register({ required: "This field is required" })}
                />
                {errors.phone && (
                  <span className="invalid">{errors.phone.message}</span>
                )}
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.LOCATION}</label>
                <div className="form-control-wrap">
                  <Controller
                    control={control}
                    defaultValue={formData.location}
                    name="location"
                    render={({ onChange, value, ref }) => (
                      <RSelect
                        inputRef={ref}
                        options={filterLocation}
                        value={value}
                        onChange={(selectedLocation) =>
                          onChange(selectedLocation)
                        }
                      />
                    )}
                  />
                </div>
                {error.isLocationEmpty && (
                  <ErrorMessage
                    content={error.errorMessage}
                    style={errorMessageStyle}
                  />
                )}
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.EXPERIENCE}</label>

                <div className="form-control-wrap">
                  <Controller
                    control={control}
                    defaultValue={formData.experience}
                    name="experience"
                    render={({ onChange, value, ref }) => (
                      <RSelect
                        inputRef={ref}
                        options={filterExperience}
                        value={value}
                        onChange={(selectedExperience) =>
                          onChange(selectedExperience)
                        }
                      />
                    )}
                  />
                </div>
              </FormGroup>
            </Col>
            <Col md="12">
              <FormGroup>
                <label className="form-label">{COPY.SKILLS}</label>
                <div className="form-control-wrap">
                  <Controller
                    control={control}
                    defaultValue={formData.skills}
                    name="skills"
                    render={({ onChange, value, ref }) => (
                      <RSelect
                        isMulti
                        inputRef={ref}
                        closeMenuOnSelect={false}
                        options={filterSkills}
                        value={value}
                        onChange={(selectedSkills) => {
                          setError({
                            ...error,
                            isSkillEmpty: selectedSkills.length === 0,
                          });
                          onChange(selectedSkills);
                        }}
                      />
                    )}
                  />
                </div>
                {error.isSkillEmpty && (
                  <ErrorMessage
                    content={error.errorMessage}
                    style={errorMessageStyle}
                  />
                )}
              </FormGroup>
            </Col>

            <Col size="12">
              <ul className="align-center flex-wrap flex-sm-nowrap gx-4 gy-2">
                <li>
                  <Button color="primary" size="md" type="submit">
                    {COPY.UPDATE_INTERVIEWER}
                  </Button>
                </li>
                <li>
                  <a
                    href="#cancel"
                    onClick={(ev) => {
                      ev.preventDefault();
                      onFormCancel();
                    }}
                    className="link link-light"
                  >
                    {COPY.CANCEL}
                  </a>
                </li>
              </ul>
            </Col>
          </Form>
        </div>
      </div>
    </React.Fragment>
  );
};

export default EditInterviewer;
