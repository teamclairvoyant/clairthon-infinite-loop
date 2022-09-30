import React, { useContext, useEffect, useMemo, useState } from 'react';
import Content from '../../../layout/content/Content';
import Head from '../../../layout/head/Head';
import {
  DropdownMenu,
  DropdownToggle,
  UncontrolledDropdown,
  DropdownItem,
  Spinner
} from 'reactstrap';
import {
  Block,
  BlockBetween,
  BlockDes,
  BlockHead,
  BlockHeadContent,
  BlockTitle,
  Icon,
  UserAvatar,
  PaginationComponent,
  Button,
  DataTable,
  DataTableBody,
  DataTableHead,
  DataTableRow,
  DataTableItem,
  TooltipComponent
} from '../../../components/Component';
import { filterExperience } from '../../../common/listing/ListingData';
import { findUpper, getDateNTime } from '../../../utils/Utils';
import { Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { UserContext } from './UserContext';
import { request } from '../../../utils/axiosUtils';
import { URL_ENDPOINTS, COPY, METHODS, RESPONSE_MESSAGE } from '../../../constants/constant';
import formatString from '../../../common/formatting/formatString';
import InterviewListModal from '../../../components/modals/CommonModal/CommonModal';
import AddInterviewer from './ModalLayouts/AddInterviewer';
import EditInterviewer from './ModalLayouts/EditInterviewer';
import { ToastContainer } from 'react-toastify';
import { errorToast, successToast } from '../../../components/toastr/Tostr';
import FilterInterviewer from './components/FilterInterviewer/FilterInterviewer';
import InterviewerCalender from './InterviewerCalender';
import { ErrorMessage } from '../../../components/error-message/ErrorMessage';
import { useListContext } from '../../../context/listContext';
const initFormData = {
  name: '',
  employee_no: '',
  email: '',
  skills: [],
  phone: '',
  experience: 0,
  location: ''
};
const InterviewList = () => {
  const { contextData } = useContext(UserContext);
  const { skillOptions, locationOptions } = useListContext();
  const [data, setData] = contextData;

  const [sm, updateSm] = useState(false);
  const [tablesm, updateTableSm] = useState(false);
  const [onSearch, setonSearch] = useState(true);
  const [onSearchText, setSearchText] = useState('');
  const [modal, setModal] = useState({
    edit: false,
    add: false
  });

  const [interviewerError, setInterviewerError] = useState({
    addInterviewerMessage: '',
    editInterviewerMessage: ''
  });
  const [calendarModal, setCalendarModal] = useState(false);
  const [selectInterviewerMessage, setSelectInterviewerMessage] = useState('');
  const [editId, setEditedId] = useState();
  const [formData, setFormData] = useState(initFormData);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemPerPage, setItemPerPage] = useState(10);
  const [sort, setSortState] = useState('');
  const [filter, addFilter] = useState({
    location: [],
    skills: [],
    experience: 0,
    startDate: '',
    startTime: '',
    endTime: '',
    endDate: ''
  });
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const toggleFilterDropdown = () => setDropdownOpen((prevState) => !prevState);
  const [selectedInterviewers, setSelectedInterviewer] = useState([]);
  // Sorting data
  const sortFunc = (params) => {
    let defaultData = data;
    if (params === 'asc') {
      let sortedData = defaultData.sort((a, b) => a.name.localeCompare(b.name));
      setData([...sortedData]);
    } else if (params === 'dsc') {
      let sortedData = defaultData.sort((a, b) => b.name.localeCompare(a.name));
      setData([...sortedData]);
    }
  };
  const fetchInterviewers = (filterOptions = null) => {
    setLoading(true);

    const requestOptions = {
      url: filterOptions ? URL_ENDPOINTS.INTERVIEWER_SEARCH : URL_ENDPOINTS.INTERVIEWER,
      method: filterOptions ? METHODS.POST : METHODS.GET,
      ...(filterOptions && { data: filterOptions })
    };

    const responseData = request(requestOptions);
    responseData
      .then((response) => {
        if (response.data.STATUS === RESPONSE_MESSAGE.SUCCESS) {
          let interviewerData;
          interviewerData = response.data.DATA.map((interviewer) => {
            interviewer.checked = false;
            return interviewer;
          });
          setLoading(false);

          setData(interviewerData);
        } else {
          errorToast(response.data.MESSAGE);
        }
      })
      .catch((err) => {
        errorToast(err);
      });
  };

  const addInterviewers = (interviewerData) => {
    const responseData = request({
      url: URL_ENDPOINTS.INTERVIEWER,
      method: METHODS.POST,
      data: interviewerData
    });

    responseData
      .then((response) => {
        if (response.data.STATUS === RESPONSE_MESSAGE.SUCCESS) {
          successToast(editId ? COPY.INTERVIEWER_UPDATED : COPY.INTERVIEWER_ADDED);
          setModal({ edit: false }, { add: false });

          resetForm();
          editId && setEditedId(null);
          fetchInterviewers();
        } else {
          setInterviewerError({
            addInterviewerMessage: !editId ? response.data.MESSAGE : '',
            editInterviewerMessage: editId ? response.data.MESSAGE : ''
          });
        }
      })
      .catch((err) => {
        errorToast(err);
      });
  };

  const applyFilter = () => {
    const { skills, location, experience, startDate, endDate, startTime, endTime } = filter;

    const startDateTime = getDateNTime(startDate, startTime);
    const endDateTime = getDateNTime(endDate, endTime);

    const filterOptions = {
      ...(skills.length && { skills: skills.map((skill) => skill.value) }),
      ...(location.length && {
        location: location.map((location) => location.value)
      }),
      ...(experience.value && { experience: experience.value }),
      ...(startDateTime && { start_time: startDateTime }),
      ...(endDateTime && { end_time: endDateTime })
    };

    fetchInterviewers(filterOptions);

    setTimeout(() => toggleFilterDropdown(), 1000);
  };

  const resetFilter = () => {
    addFilter({
      experience: 0,
      skills: [],
      location: []
    });
    fetchInterviewers();
    toggleFilterDropdown();
  };

  useEffect(() => {
    fetchInterviewers();
  }, []);

  const interviewersList = useMemo(() => {
    if (!onSearchText) return data;
    return data.filter((item) => {
      return (
        item.name.toLowerCase().includes(onSearchText.toLowerCase()) ||
        item.email.toLowerCase().includes(onSearchText.toLowerCase())
      );
    });
  }, [data, onSearchText]);

  // function to set the action to be taken in table header
  // const onActionText = (e) => {
  //   setActionText(e.value);
  // };

  // onChange function for searching name
  const onFilterChange = (e) => {
    setSearchText(e.target.value);
  };

  // function to change the selected property of an item
  const onSelectChange = (e, id) => {
    let interviewerData = data;
    let index = interviewerData.findIndex((item) => item._id === id);
    interviewerData[index].checked = e.currentTarget.checked;

    if (e.currentTarget.checked) {
      setSelectInterviewerMessage('');
    }
    let selectedInterviewerIndex = selectedInterviewers.findIndex((item) => item._id === id);
    setData([...interviewerData]);

    if (selectedInterviewerIndex > -1) {
      selectedInterviewers.splice(selectedInterviewerIndex, 1);
      setSelectedInterviewer([...selectedInterviewers]);
    } else {
      setSelectedInterviewer([...selectedInterviewers, interviewerData[index]]);
    }
  };

  // function to reset the form
  const resetForm = () => {
    setFormData(initFormData);
  };

  // function to close the form modal
  const onFormCancel = () => {
    setModal({ edit: false, add: false });
    resetForm();
  };

  // submit function to update a new item
  const onEditSubmit = (submitData) => {
    const { location, experience, skills } = submitData;
    const interviewerData = {
      ...submitData,
      location: location.value,
      skills: skills.map((skill) => skill.value),
      experience: experience.value,
      isInterviewer: true,
      id: editId
    };

    addInterviewers(interviewerData);
  };

  // function that loads the want to editted data
  const onEditClick = (id) => {
    data.forEach((item) => {
      if (item._id === id) {
        setFormData({
          name: item.name,
          employee_no: item.employee_no,
          email: item.email,
          experience: filterExperience.find((experience) => experience.value === item.experience),
          location: locationOptions.find((location) => location.value === item.location),
          phone: item.phone,
          skills: item.skills
            ? item.skills.map((skill) => skillOptions.find((fSkill) => fSkill.value === skill))
            : []
        });
        setModal({ edit: true }, { add: false });
        setEditedId(id);
      }
    });
  };

  // function to change to suspend property for an item
  const suspendUser = (id) => {
    let newData = data;
    let index = newData.findIndex((item) => item._id === id);
    newData[index].status = 'Suspend';
    setData([...newData]);
  };

  // function to change the check property of an item
  const selectorCheck = (e) => {
    let newData;
    newData = data.map((item) => {
      item.checked = e.currentTarget.checked;
      return item;
    });
    setData([...newData]);
  };

  // function which fires on applying selected action
  // const onBulkActionClick = (e) => {
  //   if (!selectedInterviewers.length) {
  //     setSelectInterviewerMessage(COPY.SELECT_INTERVIEWER_MESSAGE);
  //     return;
  //   }
  //   if (actionText === "calendar") {
  //     setCalendarModal(true);
  //     setSelectInterviewerMessage("");
  //   } else if (actionText === "delete") {
  //     let newData;
  //     newData = data.filter((item) => item.checked !== true);
  //     setData([...newData]);
  //   }
  // };

  // function to toggle the search option
  const toggle = () => setonSearch(!onSearch);

  // Get current list, pagination
  const indexOfLastItem = currentPage * itemPerPage;
  const indexOfFirstItem = indexOfLastItem - itemPerPage;
  const currentItems = interviewersList.slice(indexOfFirstItem, indexOfLastItem);
  // console.log({curr})
  // Change Page
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const { errors, register, handleSubmit, control } = useForm();

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <Spinner color="dark" />
      </div>
    );
  }
  return (
    <React.Fragment>
      <Head title="Interview List"></Head>
      <Content>
        <BlockHead size="sm">
          <BlockBetween>
            <BlockHeadContent>
              <BlockTitle tag="h3" page>
                {COPY.INTERVIEWER_LIST}
              </BlockTitle>
              <BlockDes className="text-soft">
                <p>{formatString(COPY.TOTAL_INTERVIEWER, data.length)}</p>
              </BlockDes>
            </BlockHeadContent>
            <ToastContainer />

            <BlockHeadContent>
              <div className="toggle-wrap nk-block-tools-toggle">
                <Button
                  className={`btn-icon btn-trigger toggle-expand mr-n1 ${sm ? 'active' : ''}`}
                  onClick={() => updateSm(!sm)}>
                  <Icon name="menu-alt-r"></Icon>
                </Button>
                <div className="toggle-expand-content" style={{ display: sm ? 'block' : 'none' }}>
                  <ul className="nk-block-tools g-3">
                    <li>
                      <Button color="light" outline className="btn-white">
                        <Icon name="upload-cloud"></Icon>
                        <span>Import</span>
                      </Button>
                    </li>
                    <li className="nk-block-tools-opt">
                      <Button
                        color="primary"
                        className="btn-icon"
                        onClick={() => setModal({ add: true })}>
                        <Icon name="plus"></Icon>
                      </Button>
                    </li>
                  </ul>
                </div>
              </div>
            </BlockHeadContent>
          </BlockBetween>
        </BlockHead>

        <Block>
          <DataTable className="card-stretch">
            <div className="card-inner position-relative card-tools-toggle">
              <div className="card-title-group">
                <div className="card-tools">
                  {/* <div className="form-inline flex-nowrap gx-3">
                    <div className="form-wrap">
                      <RSelect
                        options={bulkActionOptions}
                        className="w-130px"
                        placeholder="Bulk Action"
                        onChange={(e) => onActionText(e)}
                      />
                    </div>
                    <div className="btn-wrap">
                      <span className="d-none d-md-block">
                        <Button
                          disabled={actionText !== "" ? false : true}
                          color="light"
                          outline
                          className="btn-dim"
                          onClick={(e) => onBulkActionClick(e)}
                        >
                          Apply
                        </Button>
                      </span>
                      <span className="d-md-none">
                        <Button
                          color="light"
                          outline
                          disabled={actionText !== "" ? false : true}
                          className="btn-dim btn-icon"
                          onClick={(e) => onBulkActionClick(e)}
                        >
                          <Icon name="arrow-right"></Icon>
                        </Button>
                      </span>
                    </div>
                  </div> */}
                  <ErrorMessage content={selectInterviewerMessage} />
                </div>
                <div className="card-tools mr-n1">
                  <ul className="btn-toolbar gx-1">
                    <li>
                      <a
                        href="#search"
                        onClick={(ev) => {
                          ev.preventDefault();
                          toggle();
                        }}
                        className="btn btn-icon search-toggle toggle-search">
                        <Icon name="search"></Icon>
                      </a>
                    </li>
                    <li className="btn-toolbar-sep"></li>
                    <li>
                      <div className="toggle-wrap">
                        <Button
                          className={`btn-icon btn-trigger toggle ${tablesm ? 'active' : ''}`}
                          onClick={() => updateTableSm(true)}>
                          <Icon name="menu-right"></Icon>
                        </Button>
                        <div className={`toggle-content ${tablesm ? 'content-active' : ''}`}>
                          <ul className="btn-toolbar gx-1">
                            <li className="toggle-close">
                              <Button
                                className="btn-icon btn-trigger toggle"
                                onClick={() => updateTableSm(false)}>
                                <Icon name="arrow-left"></Icon>
                              </Button>
                            </li>
                            <li>
                              <UncontrolledDropdown
                                isOpen={dropdownOpen}
                                toggle={toggleFilterDropdown}>
                                <DropdownToggle
                                  tag="a"
                                  className="btn btn-trigger btn-icon dropdown-toggle">
                                  <div className="dot dot-primary"></div>
                                  <Icon name="filter-alt"></Icon>
                                </DropdownToggle>
                                <DropdownMenu
                                  right
                                  className="filter-wg dropdown-menu-xl"
                                  style={{ overflow: 'visible' }}>
                                  <FilterInterviewer
                                    filter={filter}
                                    addFilter={addFilter}
                                    applyFilter={applyFilter}
                                    resetFilter={resetFilter}
                                  />
                                </DropdownMenu>
                              </UncontrolledDropdown>
                            </li>
                            <li>
                              <UncontrolledDropdown>
                                <DropdownToggle
                                  color="tranparent"
                                  className="btn btn-trigger btn-icon dropdown-toggle">
                                  <Icon name="setting"></Icon>
                                </DropdownToggle>
                                <DropdownMenu right className="dropdown-menu-xs">
                                  <ul className="link-check">
                                    <li>
                                      <span>Show</span>
                                    </li>
                                    <li className={itemPerPage === 10 ? 'active' : ''}>
                                      <DropdownItem
                                        tag="a"
                                        href="#dropdownitem"
                                        onClick={(ev) => {
                                          ev.preventDefault();
                                          setItemPerPage(10);
                                        }}>
                                        10
                                      </DropdownItem>
                                    </li>
                                    <li className={itemPerPage === 15 ? 'active' : ''}>
                                      <DropdownItem
                                        tag="a"
                                        href="#dropdownitem"
                                        onClick={(ev) => {
                                          ev.preventDefault();
                                          setItemPerPage(15);
                                        }}>
                                        15
                                      </DropdownItem>
                                    </li>
                                  </ul>
                                  <ul className="link-check">
                                    <li>
                                      <span>Order</span>
                                    </li>
                                    <li className={sort === 'dsc' ? 'active' : ''}>
                                      <DropdownItem
                                        tag="a"
                                        href="#dropdownitem"
                                        onClick={(ev) => {
                                          ev.preventDefault();
                                          setSortState('dsc');
                                          sortFunc('dsc');
                                        }}>
                                        DESC
                                      </DropdownItem>
                                    </li>
                                    <li className={sort === 'asc' ? 'active' : ''}>
                                      <DropdownItem
                                        tag="a"
                                        href="#dropdownitem"
                                        onClick={(ev) => {
                                          ev.preventDefault();
                                          setSortState('asc');
                                          sortFunc('asc');
                                        }}>
                                        ASC
                                      </DropdownItem>
                                    </li>
                                  </ul>
                                </DropdownMenu>
                              </UncontrolledDropdown>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
              <div className={`card-search search-wrap ${!onSearch && 'active'}`}>
                <div className="card-body">
                  <div className="search-content">
                    <Button
                      className="search-back btn-icon toggle-search active"
                      onClick={() => {
                        setSearchText('');
                        toggle();
                      }}>
                      <Icon name="arrow-left"></Icon>
                    </Button>
                    <input
                      type="text"
                      className="border-transparent form-focus-none form-control"
                      placeholder="Search by user or email"
                      value={onSearchText}
                      onChange={(e) => onFilterChange(e)}
                    />
                    <Button className="search-submit btn-icon">
                      <Icon name="search"></Icon>
                    </Button>
                  </div>
                </div>
              </div>
            </div>
            <DataTableBody>
              <DataTableHead>
                <DataTableRow className="nk-tb-col-check">
                  <div className="custom-control custom-control-sm custom-checkbox notext">
                    <input
                      type="checkbox"
                      className="custom-control-input form-control"
                      onChange={(e) => selectorCheck(e)}
                      id="uid"
                    />
                    <label className="custom-control-label" htmlFor="uid"></label>
                  </div>
                </DataTableRow>
                <DataTableRow>
                  <span className="sub-text">Interviewer</span>
                </DataTableRow>
                <DataTableRow size="mb">
                  <span className="sub-text">location</span>
                </DataTableRow>
                <DataTableRow size="md">
                  <span className="sub-text">Phone</span>
                </DataTableRow>
                <DataTableRow size="lg">
                  <span className="sub-text">Skills</span>
                </DataTableRow>
                <DataTableRow size="lg">
                  <span className="sub-text">experience</span>
                </DataTableRow>

                <DataTableRow className="nk-tb-col-tools text-right">
                  <span className="sub-text">action</span>
                </DataTableRow>
              </DataTableHead>
              {/*Head*/}
              {currentItems.length > 0
                ? currentItems.map((item) => {
                    return (
                      <DataTableItem key={item._id}>
                        <DataTableRow className="nk-tb-col-check">
                          <div className="custom-control custom-control-sm custom-checkbox notext">
                            <input
                              type="checkbox"
                              className="custom-control-input form-control"
                              defaultChecked={item.checked}
                              id={item._id + 'uid1'}
                              key={Math.random()}
                              onChange={(e) => onSelectChange(e, item._id)}
                            />
                            <label
                              className="custom-control-label"
                              htmlFor={item._id + 'uid1'}></label>
                          </div>
                        </DataTableRow>
                        <DataTableRow>
                          <Link to={`${process.env.PUBLIC_URL}/interviewer-calendar/${item._id}`}>
                            <div className="user-card">
                              <UserAvatar
                                theme={item.avatarBg}
                                text={findUpper(item.name)}
                                image={item.image}></UserAvatar>
                              <div className="user-info">
                                <span className="tb-lead">
                                  {item.name}{' '}
                                  <span
                                    className={`dot dot-${
                                      item.status === 'Active'
                                        ? 'success'
                                        : item.status === 'Pending'
                                        ? 'warning'
                                        : 'danger'
                                    } d-md-none ml-1`}></span>
                                </span>
                                <span>{item.email}</span>
                              </div>
                            </div>
                          </Link>
                        </DataTableRow>
                        <DataTableRow size="mb">
                          <span className="tb-amount">
                            {item.location}
                            {/* <span className="currency">USD</span> */}
                          </span>
                        </DataTableRow>
                        <DataTableRow size="md">
                          <span>{item.phone}</span>
                        </DataTableRow>
                        <DataTableRow size="lg">
                          <span>{item.skills ? item.skills.join(', ') : null}</span>
                        </DataTableRow>
                        <DataTableRow size="lg">
                          <span>{item.experience}</span>
                        </DataTableRow>
                        <DataTableRow className="nk-tb-col-tools">
                          <ul className="nk-tb-actions gx-1">
                            <li
                              className="nk-tb-action-hidden"
                              onClick={() => onEditClick(item._id)}>
                              <TooltipComponent
                                tag="a"
                                containerClassName="btn btn-trigger btn-icon"
                                id={'edit' + item._id}
                                icon="edit-alt-fill"
                                direction="top"
                                text="Edit"
                              />
                            </li>
                            {item.status !== 'Suspend' && (
                              <React.Fragment>
                                <li
                                  className="nk-tb-action-hidden"
                                  onClick={() => suspendUser(item._id)}>
                                  <TooltipComponent
                                    tag="a"
                                    containerClassName="btn btn-trigger btn-icon"
                                    id={'remove' + item._id}
                                    icon="user-cross-fill"
                                    direction="top"
                                    text="Remove"
                                  />
                                </li>
                              </React.Fragment>
                            )}
                            <li>
                              <UncontrolledDropdown>
                                <DropdownToggle
                                  tag="a"
                                  className="dropdown-toggle btn btn-icon btn-trigger">
                                  <Icon name="more-h"></Icon>
                                </DropdownToggle>
                                <DropdownMenu right>
                                  <ul className="link-list-opt no-bdr">
                                    <li onClick={() => onEditClick(item._id)}>
                                      <DropdownItem
                                        tag="a"
                                        href="#edit"
                                        onClick={(ev) => {
                                          ev.preventDefault();
                                        }}>
                                        <Icon name="edit"></Icon>
                                        <span>{COPY.EDIT}</span>
                                      </DropdownItem>
                                    </li>
                                    {item.status !== 'Suspend' && (
                                      <React.Fragment>
                                        <li className="divider"></li>
                                        <li onClick={() => suspendUser(item._id)}>
                                          <DropdownItem
                                            tag="a"
                                            href="#suspend"
                                            onClick={(ev) => {
                                              ev.preventDefault();
                                            }}>
                                            <Icon name="na"></Icon>
                                            <span>{COPY.REMOVE_INTERVIEWER}</span>
                                          </DropdownItem>
                                        </li>
                                      </React.Fragment>
                                    )}
                                  </ul>
                                </DropdownMenu>
                              </UncontrolledDropdown>
                            </li>
                          </ul>
                        </DataTableRow>
                      </DataTableItem>
                    );
                  })
                : null}
            </DataTableBody>
            <div className="card-inner">
              {currentItems.length > 0 ? (
                <PaginationComponent
                  itemPerPage={itemPerPage}
                  totalItems={interviewersList.length}
                  paginate={paginate}
                  currentPage={currentPage}
                />
              ) : (
                <div className="text-center">
                  <span className="text-silent">{COPY.NO_DATA_FOUND}</span>
                </div>
              )}
            </div>
          </DataTable>
        </Block>

        <InterviewListModal
          isOpen={modal.add}
          toggle={() => setModal({ add: false })}
          classNameModal="modal-dialog-centered"
          modalSize="lg"
          content={
            <AddInterviewer
              onFormCancel={onFormCancel}
              // onFormSubmit={onFormSubmit}
              addInterviewers={addInterviewers}
              interviewerValidationError={interviewerError.addInterviewerMessage}
            />
          }
        />

        <InterviewListModal
          isOpen={modal.edit}
          toggle={() => setModal({ edit: false })}
          classNameModal="modal-dialog-centered"
          modalSize="lg"
          content={
            <EditInterviewer
              onFormCancel={onFormCancel}
              handleSubmit={handleSubmit}
              onEditSubmit={onEditSubmit}
              formData={formData}
              register={register}
              errors={errors}
              filterSkills={skillOptions}
              control={control}
              interviewerValidationError={interviewerError.editInterviewerMessage}
            />
          }
        />

        <InterviewListModal
          isOpen={calendarModal}
          toggle={() => setCalendarModal(false)}
          classNameModal="modal-dialog-centered"
          modalSize="lg"
          content={<InterviewerCalender selectedInterviewers={selectedInterviewers} />}
        />
      </Content>
    </React.Fragment>
  );
};
export default InterviewList;
