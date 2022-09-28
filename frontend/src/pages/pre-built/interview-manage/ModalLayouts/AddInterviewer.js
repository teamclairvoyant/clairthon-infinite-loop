import React from "react";
import { Icon, Col, Button, RSelect } from "../../../../components/Component";
import { Controller, useForm } from "react-hook-form";
import { FormGroup, Form } from "reactstrap";
import { COPY, TEST_ID } from "../../../../constants/constant";
import { filterExperience } from "../../../../common/listing/ListingData";
import { ErrorMessage } from "../../../../components/error-message/ErrorMessage";
import { useListContext } from "../../../../context/listContext";
const AddInterviewer = (props) => {
  const { onFormCancel, interviewerValidationError, addInterviewers } = props;
  const { errors, register, handleSubmit, control } = useForm();
  const { skillOptions, locationOptions } = useListContext();

  // submit function to add a new item
  const onFormSubmit = (interviewerData) => {
    const { name, email, employeeNo, phone, experience, skills, location } =
      interviewerData;

    const addInterviewer = {
      name,
      email,
      employee_no: employeeNo,
      phone,
      experience: experience.value,
      skills: skills.map((skill) => skill.value),
      location: location.value,
      isInterviewer: true,
    };

    addInterviewers(addInterviewer);
  };
  return (
    <React.Fragment>
      <a
        href="#close"
        onClick={(ev) => {
          ev.preventDefault();
          onFormCancel();
        }}
        className="close"
        data-testid={TEST_ID.COMMON.ON_FORM_CANCEL_ANCHOR}
      >
        <Icon name="cross-sm"></Icon>
      </a>
      <div className="p-2">
        <h5 className="title">{COPY.ADD_INTERVIEWER}</h5>
        <div className="mt-4">
          <Form
            data-testid={TEST_ID.ADD_EVENT.FORM}
            className="row gy-4"
            noValidate
            onSubmit={handleSubmit(onFormSubmit)}
          >
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.NAME}</label>
                <input
                  className="form-control"
                  type="text"
                  name="name"
                  placeholder={COPY.PLACEHOLDER_NAME}
                  ref={register({ required: COPY.REQUIRED_ERROR_MESSAGE })}
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
                  name="employeeNo"
                  placeholder={COPY.PLACEHOLDER_EMPLOYEE_NO}
                  ref={register({
                    required: COPY.REQUIRED_ERROR_MESSAGE,
                  })}
                />
                {errors.employeeNo && (
                  <span className="invalid">{errors.employeeNo.message}</span>
                )}
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label className="form-label">{COPY.EMAIL} </label>
                <input
                  className="form-control"
                  type="text"
                  name="email"
                  placeholder={COPY.PLACEHOLDER_EMAIL}
                  ref={register({
                    required: COPY.REQUIRED_ERROR_MESSAGE,
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                      message: COPY.INVALID_EMAIL_ADDRESS,
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
                  placeholder={COPY.PLACEHOLDER_PHONE}
                  ref={register({ required: COPY.REQUIRED_ERROR_MESSAGE })}
                />
                {errors.phone && (
                  <span className="invalid">{errors.phone.message}</span>
                )}
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label
                  className="form-label"
                  htmlFor={COPY.LOCATION}
                  aria-label={COPY.LOCATION}
                >
                  {COPY.LOCATION}
                </label>
                <div className="form-control-wrap">
                  <Controller
                    control={control}
                    name="location"
                    defaultValue={locationOptions[0]}
                    render={({ onChange, value, ref }) => (
                      <RSelect
                        inputRef={ref}
                        options={locationOptions}
                        value={value}
                        name={COPY.LOCATION}
                        id={COPY.LOCATION}
                        inputId={COPY.LOCATION}
                        placeholder={COPY.PLACEHOLDER_LOCATION}
                        onChange={(selectedLocation) =>
                          onChange(selectedLocation)
                        }
                      />
                    )}
                  />
                </div>
              </FormGroup>
            </Col>
            <Col md="6">
              <FormGroup>
                <label
                  className="form-label"
                  htmlFor={COPY.EXPERIENCE}
                  aria-label={COPY.EXPERIENCE}
                >
                  {COPY.EXPERIENCE}
                </label>

                <div className="form-control-wrap">
                  <Controller
                    control={control}
                    defaultValue={filterExperience[0]}
                    name="experience"
                    render={({ onChange, value, ref }) => (
                      <RSelect
                        inputRef={ref}
                        options={filterExperience}
                        placeholder={COPY.PLACEHOLDER_EXPERIENCE}
                        name={COPY.EXPERIENCE}
                        id={COPY.EXPERIENCE}
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
                <label
                  className="form-label"
                  htmlFor={COPY.SKILLS}
                  aria-label={COPY.SKILLS}
                >
                  {COPY.SKILLS}
                </label>
                <div className="form-control-wrap">
                  <Controller
                    control={control}
                    defaultValue={[skillOptions[0]]}
                    name="skills"
                    render={({ onChange, value, ref }) => (
                      <RSelect
                        isMulti
                        id={COPY.SKILLS}
                        inputRef={ref}
                        name={COPY.SKILLS}
                        closeMenuOnSelect={false}
                        options={skillOptions}
                        placeholder={COPY.PLACEHOLDER_SKILLS}
                        value={value}
                        onChange={(selectedSkills) => onChange(selectedSkills)}
                      />
                    )}
                  />
                </div>
              </FormGroup>
            </Col>
            {interviewerValidationError && (
              <Col size="12">
                <span>
                  <ErrorMessage content={interviewerValidationError} />
                </span>
              </Col>
            )}
            <Col size="12">
              <ul className="align-center flex-wrap flex-sm-nowrap gx-4 gy-2">
                <li>
                  <Button
                    color="primary"
                    size="md"
                    type="submit"
                    data-testid={TEST_ID.COMMON.SUMBIT_BUTTON}
                  >
                    {COPY.ADD_INTERVIEWER}
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
                    data-testid={TEST_ID.COMMON.ON_FORM_CANCEL}
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

export default AddInterviewer;
