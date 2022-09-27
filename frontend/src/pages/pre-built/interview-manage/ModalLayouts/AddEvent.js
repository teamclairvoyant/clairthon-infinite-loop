import React, { useState } from "react";
import { Icon, Col, Button, RSelect } from "../../../../components/Component";
import { Controller, useForm } from "react-hook-form";

import { FormGroup, Form, Row, Spinner } from "reactstrap";
import {
  COPY,
  METHODS,
  RESPONSE_MESSAGE,
  TEST_ID,
  URL_ENDPOINTS,
} from "../../../../constants/constant";
import DatePicker from "react-datepicker";
import { ErrorMessage } from "../../../../components/error-message/ErrorMessage";
import { getDateNTime } from "../../../../utils/Utils";
import { successToast } from "../../../../components/toastr/Tostr";
import { request } from "../../../../utils/axiosUtils";
import { ToastContainer } from "react-toastify";

const initEventData = {
  title: "",
  description: "",
  date: "",
  startTime: "",
  endTime: "",
  attendees: [],
};
const AddEvent = (props) => {
  const { setEventModal, mailIdOptions } = props;
  const { control } = useForm();

  const [eventData, setEventData] = useState(initEventData);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({
    title: false,
    description: false,
    attendees: false,
    date: false,
    startDateGreater: false,
    errorMessage: "",
  });
  const [emailOptions, setEmailOptions] = useState([]);

  const handleFilterEmails = (e) => {
    if (!e.target.value) {
      setEmailOptions([]);
      return;
    }
    setEmailOptions(
      mailIdOptions.filter((mailId) =>
        mailId.label.toUpperCase().includes(e.target.value.toUpperCase())
      )
    );
  };

  const handleError = () => {
    const { title, description, date, startTime, endTime, attendees } =
      eventData;
    if (
      !title ||
      !description ||
      !date ||
      !startTime ||
      !endTime ||
      !attendees.length
    ) {
      setErrors({
        ...errors,
        ...(!title && { title: true }),
        ...(!description && { description: true }),
        ...((!date || !startTime || !endTime) && { date: true }),
        ...(!attendees.length && { attendees: true }),
      });

      return true;
    }

    const startDateTime = getDateNTime(date, startTime);
    const endDateTime = getDateNTime(date, endTime);
    if (new Date(startDateTime) >= new Date(endDateTime)) {
      setErrors({
        ...errors,
        startDateGreater: true,
      });
      return true;
    }

    return false;
  };

  const submitEvent = () => {
    const checkIsError = handleError();
    if (!checkIsError) {
      addInterviewEvent(eventData);
    }
  };

  // function to reset the form
  const resetForm = () => {
    setEventData(initEventData);
  };
  // function to close the form modal
  const onFormCancel = () => {
    setEventModal(false);
    resetForm();
  };

  // submit function to add a new item
  const addInterviewEvent = (eventData) => {
    setLoading(true);
    const { title, description, date, startTime, endTime, attendees } =
      eventData;

    const requestOptions = {
      url: URL_ENDPOINTS.CREATE_EVENT,
      method: METHODS.POST,
      data: {
        title,
        description,
        attendees,
        start_time: getDateNTime(date, startTime),
        end_time: getDateNTime(date, endTime),
      },
    };

    const responseData = request(requestOptions);

    responseData
      .then((response) => {
        if (response.data.STATUS === RESPONSE_MESSAGE.SUCCESS) {
          setLoading(false);
          successToast(COPY.INTERVIEW_SCHEDULE_SUCCESSFULLY);
        } else {
        }
      })
      .catch((err) => {});
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setErrors({
      ...errors,
      [name]: false,
    });
    setEventData({
      ...eventData,
      [name]: value,
    });
  };

  const handleDateChange = (date, name) => {
    setEventData({ ...eventData, [name]: date });
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
        <h5 className="title">{COPY.ADD_EVENT}</h5>
        <ToastContainer />
        <div className="mt-4">
          {loading ? (
            <div className="d-flex justify-content-center align-items-center vh-100">
              <Spinner color="dark" />
            </div>
          ) : (
            <Form className="row gy-4" noValidate>
              <Col md="12">
                <FormGroup>
                  <label className="form-label">{COPY.TITLE}</label>
                  <input
                    className="form-control"
                    type="text"
                    name="title"
                    data-testid={COPY.TITLE}
                    onChange={handleInputChange}
                    placeholder="Enter title"
                  />
                  {errors.title && (
                    <span className="invalid">
                      {COPY.REQUIRED_ERROR_MESSAGE}
                    </span>
                  )}
                </FormGroup>
              </Col>
              <Col size="6">
                <FormGroup>
                  <label className="form-label">Date</label>

                  <div className="form-control-wrap">
                    <DatePicker
                      selected={eventData.date}
                      dateFormat="dd-MM-yyyy"
                      placeholderText="Enter Date"
                      name="date"
                      onChange={(date) => handleDateChange(date, "date")}
                      className="form-control date-picker"
                    />
                  </div>
                  {(errors.date || errors.startDateGreater) && (
                    <ErrorMessage
                      content={
                        errors.date
                          ? COPY.REQUIRED_DATE_ERROR_MESSAGE
                          : COPY.FROM_DATE_GREATER_ERROR
                      }
                    />
                  )}
                </FormGroup>
              </Col>
              <Col size="6">
                <FormGroup>
                  <label className="form-label">
                    {COPY.START} &amp; {COPY.END_TIME}
                  </label>
                  <Row className="gx-2">
                    <div className="w-50">
                      <div className="form-control-wrap has-timepicker">
                        <DatePicker
                          selected={eventData.startTime}
                          onChange={(date) =>
                            setEventData({ ...eventData, startTime: date })
                          }
                          showTimeSelect
                          placeholderText="Start Time"
                          showTimeSelectOnly
                          timeIntervals={30}
                          data-testid={COPY.DATE}
                          timeCaption="Time"
                          dateFormat="h:mm aa"
                          className="form-control date-picker"
                        />
                      </div>
                    </div>
                    <div className="w-50">
                      <div className="form-control-wrap has-timepicker">
                        <DatePicker
                          selected={eventData.endTime}
                          onChange={(date) =>
                            setEventData({ ...eventData, endTime: date })
                          }
                          data-testid={COPY.END_TIME}
                          showTimeSelect
                          placeholderText="End Time"
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
              <Col md="12">
                <FormGroup>
                  <label className="form-label">{COPY.DESCRIPTION} </label>
                  <input
                    className="form-control"
                    type="text"
                    name="description"
                    data-testid={COPY.DESCRIPTION}
                    onChange={handleInputChange}
                    placeholder="Enter Description"
                  />
                  {errors.description && (
                    <span className="invalid">This field is required</span>
                  )}
                </FormGroup>
              </Col>
              <Col md="12">
                <FormGroup>
                  <label className="form-label">{COPY.ATTENDEES}</label>
                  <div className="form-control-wrap">
                    <Controller
                      control={control}
                      name="attendees"
                      render={({ onChange, value, ref }) => (
                        <RSelect
                          isMulti
                          handleFilter={handleFilterEmails}
                          inputRef={ref}
                          placeholder="Select Attendees"
                          name="attendees"
                          dataTestId="attendees"
                          closeMenuOnSelect={false}
                          options={emailOptions}
                          value={value}
                          onChange={(selectedAttendees) => {
                            onChange(selectedAttendees);
                            setEventData({
                              ...eventData,
                              attendees: selectedAttendees.map(
                                (attendees) => attendees.value
                              ),
                            });
                          }}
                        />
                      )}
                    />
                    {errors.attendees && (
                      <ErrorMessage content={COPY.REQUIRED_ERROR_MESSAGE} />
                    )}
                  </div>
                </FormGroup>
              </Col>
              <Col size="12">
                <ul className="align-center flex-wrap flex-sm-nowrap gx-4 gy-2">
                  <li>
                    <Button
                      color="primary"
                      size="md"
                      data-testid={TEST_ID.COMMON.SUMBIT_BUTTON}
                      name="submitButton"
                      onClick={(ev) => {
                        ev.preventDefault();

                        submitEvent();
                      }}
                    >
                      {COPY.ADD_EVENT}
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
          )}
        </div>
      </div>
    </React.Fragment>
  );
};

export default AddEvent;
