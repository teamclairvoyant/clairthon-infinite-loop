import React, { useContext, useEffect, useMemo, useState } from 'react';
import PropTypes from 'prop-types';
import Content from '../../../layout/content/Content';
import Head from '../../../layout/head/Head';
import './InterviewCalender.scss';
import {
  Button,
  BlockBetween,
  BlockHead,
  BlockHeadContent,
  BlockTitle,
  Icon,
  Block,
  Row,
  Col,
  PreviewAltCard,
  BlockContent,
  RSelect
} from '../../../components/Component';
import DatePicker from 'react-datepicker';
import { DropdownMenu, DropdownToggle, FormGroup, Spinner, UncontrolledDropdown } from 'reactstrap';
import { useHistory } from 'react-router';
import { UserContext } from './UserContext';
import { request } from '../../../utils/axiosUtils';
import {
  CATCH_MESSAGE,
  COPY,
  METHODS,
  RESPONSE_MESSAGE,
  URL_ENDPOINTS
} from '../../../constants/constant';
import dayjs from 'dayjs';
import { getDateNTime } from '../../../utils/Utils';
import { returnDate } from '../../../components/partials/calender/CalenderData';
import CalenderApp from '../../../components/partials/calender/Calender';
import { colorOptions } from './components/CaldendarOptions/CalendarData';
import AddEventModal from '../../../components/modals/CommonModal/CommonModal';
import AddEvent from './ModalLayouts/AddEvent';
import { errorToast } from '../../../components/toastr/Tostr';

const InterviewerCalender = ({ match, ...props }) => {
  const { selectedInterviewers } = props;

  const { contextData } = useContext(UserContext);
  const [loading, setLoading] = useState(false);
  const [events, setEvents] = useState([]);
  const [isFilterEvent, setIsFilterEvent] = useState(null);
  const [data] = contextData;
  const [eventModal, setEventModal] = useState(false);
  const [interviewerData, setInterviewerData] = useState([]);
  const [interviewersEmail, setInterviewersEmail] = useState([]);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const toggleFilterDropdown = () => setDropdownOpen((prevState) => !prevState);
  const [interviewersList, setInterviewersList] = useState([]);
  const [errorMessage, setErrorMessage] = useState('');
  const [emailId, setEmailId] = useState();
  const [emailOptions, setEmailOptions] = useState([]);
  const history = useHistory();
  const [dates, setDates] = useState({
    startDate: dayjs().startOf('week').$d,
    startTime: dayjs().startOf('week').$d,
    endTime: dayjs().endOf('week').$d,
    endDate: dayjs().endOf('week').$d
  });

  useEffect(() => {
    if (match?.params?.id) {
      const id = match.params.id;
      if (data.length) {
        setInterviewerData([
          data.find((item) => {
            return item._id === id;
          })
        ]);
        setInterviewersList(data);
      } else {
        const requestOptions = {
          url: URL_ENDPOINTS.INTERVIEWER,
          method: METHODS.GET
        };

        const responseData = request(requestOptions);

        responseData
          .then((response) => {
            if (response.data.STATUS === RESPONSE_MESSAGE.SUCCESS) {
              setLoading(false);
              const interviewers = response.data.DATA;

              setInterviewerData([
                interviewers.find((item) => {
                  return item._id === id;
                })
              ]);

              setInterviewersList(interviewers);
            } else {
              errorToast(response.data.MESSAGE);
            }
          })
          .catch((err) => {
            err && errorToast(CATCH_MESSAGE.ERROR);
          });
      }
    }
    return null;
  }, [match?.params?.id, data]);

  useEffect(() => {
    const emailIds = selectedInterviewers
      ? selectedInterviewers?.map((interviewer, i) => ({
          email: interviewer.email,
          className: colorOptions[i]
        }))
      : interviewerData?.map((interviewer, i) => ({
          email: interviewer.email,
          className: colorOptions[i]
        }));

    setInterviewersEmail(emailIds);
  }, [selectedInterviewers, interviewerData]);

  const fetchEvents = () => {
    const { startDate, startTime, endDate, endTime } = dates;
    setLoading(true);
    const dataObj = {
      emails: interviewersEmail.map((interviewer) => interviewer.email),
      start_time: getDateNTime(startDate, startTime),
      end_time: getDateNTime(endDate, endTime)
    };

    const requestOptions = {
      url: URL_ENDPOINTS.GET_INTERVIEWER_EVENTS,
      method: METHODS.POST,
      data: dataObj
    };
    const responseData = request(requestOptions);
    responseData
      .then((response) => {
        if (response.data.STATUS === RESPONSE_MESSAGE.SUCCESS) {
          let eventListArray = [];
          response.data.DATA.map((interviewerData) => {
            const [interviewerEmailId, interviewerEvents] = Object.entries(interviewerData)[0];
            const interviewerEmailIndex = interviewersEmail.findIndex(
              (emailList) => emailList.email === interviewerEmailId
            );
            const className = interviewersEmail[interviewerEmailIndex].className;
            const eventArray = interviewerEvents.map((event) => {
              event.id = 'id-' + event.id;
              event.className = className;
              event.title = `${event.title}, ${returnDate(event.start, 'h:mm A')}-${returnDate(
                event.end,
                'h:mm A'
              )}`;
              return event;
            });
            eventListArray = [...eventListArray, ...eventArray];
            return eventListArray;
          });
          setEmailId([]);
          setEvents(eventListArray);
        } else {
          if (response.data.MESSAGE.includes(COPY.EMAIL_ID_NOT_FOUND)) {
            setErrorMessage(COPY.EMAIL_ID_NOT_FOUND_MESSAGE);
          }
        }
      })
      .catch((err) => {
        err && errorToast(CATCH_MESSAGE.ERROR);
      })
      .finally(() => {
        setLoading(false);
        setDropdownOpen(false);
      });
  };

  const mailIdOptions = useMemo(() => {
    return interviewersList.map((interviewer) => ({
      value: interviewer.email,
      label: interviewer.email
    }));
  }, [interviewersList]);
  const toggle = () => {
    setEventModal(!eventModal);
  };

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
  useEffect(() => {
    if (interviewersEmail?.length) {
      fetchEvents();
    }
  }, [interviewersEmail]);

  const editEvent = (formData) => {
    let newEvents = events;
    const index = newEvents.findIndex((item) => item.id === formData.id);
    events[index] = formData;
    setEvents([...events]);
  };

  const deleteEvent = (id) => {
    let filteredEvents = events.filter((item) => item.id !== id);
    setEvents(filteredEvents);
  };

  const addEmailId = () => {
    let colorOptionIndex = interviewersEmail.length;

    const emailArray = emailId?.map((email) => ({
      email: email.value,
      className: colorOptions[colorOptionIndex++]
    }));

    setInterviewersEmail([...interviewersEmail, ...emailArray]);
  };

  const removeEmailId = (emailId) => {
    const filteredEmailList = interviewersEmail.filter(
      (interviewer) => interviewer.email !== emailId
    );
    if (!filteredEmailList.length) {
      setEvents([]);
    }
    setInterviewersEmail(filteredEmailList);
  };

  const filterInterviewerCreatedSlots = (e) => {
    setIsFilterEvent(e.currentTarget.checked);
  };

  const filteredEvents = useMemo(() => {
    if (isFilterEvent) {
      return events?.filter((event) => event.title.toLowerCase().includes(COPY.EVENT_IDENTIFIER));
    }
    return null;
  }, [events, isFilterEvent]);

  const showEmailIds = () => {
    if (!interviewersEmail.length) {
      return null;
    }
    return (
      <div className="mailListBlock">
        {interviewersEmail?.map((interviewer, i) => (
          <span className={`${interviewer.className} mailList`} key={`${interviewer.email}-${i}`}>
            <b>{interviewer.email} </b>
            <span className="mailCloseIcon">
              <Icon onClick={() => removeEmailId(interviewer.email)} name="cross-sm"></Icon>
            </span>
          </span>
        ))}
      </div>
    );
  };

  if (errorMessage) {
    return (
      <React.Fragment>
        <Head title="Interviewer Calender"></Head>
        <Block>
          <PreviewAltCard>
            <Row className="d-flex justify-content-center align-items-center vh-100">
              <div className="">
                <b>{errorMessage}</b>
              </div>
            </Row>
          </PreviewAltCard>
        </Block>
      </React.Fragment>
    );
  }
  return (
    <React.Fragment>
      <Head title="Interviewer Calender"></Head>

      {(interviewerData || interviewersEmail) && (
        <Content>
          <BlockHead size="sm">
            <BlockBetween>
              <BlockHeadContent>
                <BlockTitle tag="h3" page>
                  Interviewer /{' '}
                  <strong className="text-primary small">{interviewerData?.name}</strong>
                </BlockTitle>
              </BlockHeadContent>

              <BlockHeadContent>
                <Button
                  color="light"
                  outline
                  className="bg-white d-none d-sm-inline-flex"
                  onClick={() => history.goBack()}>
                  <Icon name="arrow-left"></Icon>
                  <span>Back</span>
                </Button>
                <a
                  href="#back"
                  onClick={(ev) => {
                    ev.preventDefault();
                    history.goBack();
                  }}
                  className="btn btn-icon btn-outline-light bg-white d-inline-flex d-sm-none">
                  <Icon name="arrow-left"></Icon>
                </a>
              </BlockHeadContent>
            </BlockBetween>
          </BlockHead>

          {/* {showCalender} */}
          <BlockHead size="sm">
            <BlockBetween>
              <BlockHeadContent>
                <BlockBetween>
                  <BlockHeadContent>
                    <BlockContent page>{showEmailIds()}</BlockContent>
                  </BlockHeadContent>
                  <span className="btn-toolbar-sep"></span>

                  <BlockHeadContent>
                    <FormGroup className="emailText">
                      <RSelect
                        isMulti
                        handleFilter={handleFilterEmails}
                        placeholder="enter emailId"
                        name={COPY.EMAIL}
                        dataTestId={COPY.EMAIL}
                        closeMenuOnSelect={false}
                        options={emailOptions}
                        value={emailId}
                        onChange={(selectedEmail) => {
                          setEmailId(selectedEmail);
                        }}
                      />
                    </FormGroup>
                  </BlockHeadContent>
                  <span className="btn-toolbar-sep"></span>

                  <BlockHeadContent>
                    <Button color="light" onClick={addEmailId} className="mr-n2">
                      <Icon name="plus" />
                    </Button>
                  </BlockHeadContent>
                </BlockBetween>
              </BlockHeadContent>

              <BlockHeadContent>
                <BlockBetween>
                  <BlockHeadContent>
                    <UncontrolledDropdown isOpen={dropdownOpen} toggle={toggleFilterDropdown}>
                      <DropdownToggle tag="a" className="btn btn-trigger btn-icon dropdown-toggle">
                        <div className="dot dot-primary"></div>
                        <Icon name="filter-alt"></Icon>
                      </DropdownToggle>
                      <DropdownMenu
                        right
                        className="filter-wg dropdown-menu-xl"
                        style={{ overflow: 'visible' }}>
                        <div className="dropdown-head">
                          <span className="sub-title dropdown-title">{COPY.SELECT_DATE}</span>
                        </div>
                        <div className="dropdown-body dropdown-body-rg">
                          <Row className="gx-6 gy-3">
                            <Col size="12">
                              <FormGroup>
                                <label className="form-label">Start Date &amp; Time</label>
                                <Row className="gx-2">
                                  <div className="w-55">
                                    <div className="form-control-wrap">
                                      <DatePicker
                                        selected={dates.startDate}
                                        onChange={(date) =>
                                          setDates({
                                            ...dates,
                                            startDate: date
                                          })
                                        }
                                        className="form-control date-picker"
                                      />
                                    </div>
                                  </div>
                                  <div className="w-45">
                                    <div className="form-control-wrap has-timepicker">
                                      <DatePicker
                                        selected={dates.startTime}
                                        onChange={(date) =>
                                          setDates({
                                            ...dates,
                                            startTime: date
                                          })
                                        }
                                        showTimeSelect
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
                                <label className="form-label">End Date &amp; Time</label>
                                <Row className="gx-2">
                                  <div className="w-55">
                                    <div className="form-control-wrap">
                                      <DatePicker
                                        selected={dates.endDate}
                                        onChange={(date) =>
                                          setDates({
                                            ...dates,
                                            endDate: date
                                          })
                                        }
                                        className="form-control date-picker"
                                      />
                                    </div>
                                  </div>
                                  <div className="w-45">
                                    <div className="form-control-wrap has-timepicker">
                                      <DatePicker
                                        selected={dates.endTime}
                                        onChange={(date) =>
                                          setDates({
                                            ...dates,
                                            endTime: date
                                          })
                                        }
                                        showTimeSelect
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
                          </Row>
                        </div>
                        <div className="dropdown-foot between">
                          <button type="button" className="btn btn-secondary" onClick={fetchEvents}>
                            {COPY.APPLY_FILTER}
                          </button>
                        </div>
                      </DropdownMenu>
                    </UncontrolledDropdown>
                  </BlockHeadContent>
                  <span className="btn-toolbar-sep"></span>
                  {interviewerData && (
                    <BlockHeadContent>
                      <Button color="primary" onClick={toggle}>
                        <Icon name="plus" />
                        <span>Add Event</span>
                      </Button>
                    </BlockHeadContent>
                  )}
                </BlockBetween>
              </BlockHeadContent>
            </BlockBetween>
          </BlockHead>
          <BlockHead size="sm">
            <BlockBetween>
              <BlockHeadContent>
                <div className="custom-control custom-control-sm custom-checkbox notext">
                  <input
                    type="checkbox"
                    className="custom-control-input form-control"
                    onChange={(e) => filterInterviewerCreatedSlots(e)}
                    id="uid"
                  />
                  <label className="custom-control-label" htmlFor="uid">
                    Show only interviewer slots
                  </label>
                </div>
              </BlockHeadContent>

              <BlockHeadContent>
                <BlockBetween>
                  <BlockHeadContent></BlockHeadContent>
                </BlockBetween>
              </BlockHeadContent>
            </BlockBetween>
          </BlockHead>

          {loading ? (
            <div className="d-flex justify-content-center align-items-center vh-100">
              <Spinner color="dark" />
            </div>
          ) : (
            <Block>
              <PreviewAltCard>
                <CalenderApp
                  events={!filteredEvents ? events : filteredEvents}
                  onDelete={deleteEvent}
                  onEdit={editEvent}
                />
              </PreviewAltCard>
            </Block>
          )}
        </Content>
      )}

      <AddEventModal
        isOpen={eventModal}
        toggle={() => setEventModal(false)}
        classNameModal="modal-dialog-centered"
        modalSize="lg"
        content={<AddEvent setEventModal={setEventModal} mailIdOptions={mailIdOptions} />}
      />
    </React.Fragment>
  );
};
export default InterviewerCalender;

InterviewerCalender.defaultProps = {
  selectedInterviewers: []
};

InterviewerCalender.propTypes = {
  match: PropTypes.object,
  selectedInterviewers: PropTypes.array
};
