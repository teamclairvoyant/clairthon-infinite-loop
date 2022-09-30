import React, { useState, useMemo, useEffect } from 'react';
import Head from '../../layout/head/Head';
import Content from '../../layout/content/Content';
import './list.scss';
import {
  BlockHead,
  BlockHeadContent,
  BlockTitle,
  BlockBetween,
  PaginationComponent,
  DataTableRow,
  DataTableHead,
  DataTableBody,
  Icon,
  Block,
  DataTable,
  DataTableItem,
  TooltipComponent
} from '../../components/Component';
import { COPY, LIST_TYPE } from '../../constants/constant';
import { useListContext } from '../../context/listContext';
import {
  Button,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  Spinner,
  UncontrolledDropdown
} from 'reactstrap';
import CommonModal from '../../components/modals/CommonModal/CommonModal';
import { AddList } from '../pre-built/interview-manage/ModalLayouts/AddList';

const itemPerPage = 10;
const listArray = ['Skills', 'Locations'];
const List = () => {
  const [isSkill, setIsSkill] = useState(true);
  const { skillOptions, locationOptions } = useListContext();
  const [skills, setSkills] = useState([]);
  const [locations, setLocations] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [addModal, setAddModal] = useState(false);
  useEffect(() => {
    setSkills(skillOptions);
    setLocations(locationOptions);
  }, [skillOptions, locationOptions]);

  let [listData, selectedListName] = useMemo(() => {
    return isSkill ? [skills, listArray[0]] : [locations, listArray[1]];
  }, [isSkill, skills, locations]);

  const loading = useMemo(() => {
    return listData.length ? false : true;
  }, [listData]);

  const onFormCancel = (addedList) => {
    setAddModal(false);
    const {
      name,
      location: { value }
    } = addedList;
    const addedListData = { value: name, label: name, type: value };
    if (value === LIST_TYPE[1].value) {
      const locationArray = [...locations, ...[addedListData]];
      setLocations(locationArray);
      console.log({ locationArray });
    } else {
      const skillsArray = [...skills, ...[addedListData]];
      setSkills(skillsArray);
    }
  };

  // Get current list, pagination
  const indexOfLastItem = currentPage * itemPerPage;
  const indexOfFirstItem = indexOfLastItem - itemPerPage;
  const currentItems = useMemo(() => {
    return listData.slice(indexOfFirstItem, indexOfLastItem);
  }, [listData, indexOfFirstItem, indexOfLastItem]);
  // Change Page
  console.log({ currentItems });
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <Spinner color="dark" />
      </div>
    );
  }
  return (
    <React.Fragment>
      <Head title="Homepage"></Head>
      <Content>
        <BlockHead size="sm">
          <BlockBetween>
            <BlockHeadContent>
              <BlockTitle page tag="h3">
                Interview Slot Planner / List
              </BlockTitle>
            </BlockHeadContent>
          </BlockBetween>
        </BlockHead>

        <Block className="mainContent">
          <DataTable className="card-stretch">
            <div className="card-inner position-relative card-tools-toggle">
              <div className="card-title-group">
                <div className="card-tools">
                  <ul>
                    <li className="btn-toolbar-sep">
                      <Button color="primary" onClick={() => setIsSkill(!isSkill)}>
                        Show {isSkill ? listArray[1] : listArray[0]}
                      </Button>
                    </li>
                  </ul>
                </div>
                <div className="card-tools mr-n1">
                  <ul className="btn-toolbar gx-1">
                    <li>
                      {/* <a
                        href="#search"
                        onClick={(ev) => {
                          ev.preventDefault();
                          //   toggle();
                        }}
                        className="btn btn-icon search-toggle toggle-search">
                        <Icon name="search"></Icon>
                      </a> */}
                    </li>

                    <li>
                      <div className="toggle-wrap">
                        <Button
                          color="primary"
                          className="btn-icon"
                          onClick={() => setAddModal(true)}>
                          <Icon name="plus"></Icon>
                        </Button>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
              <div className={`card-search search-wrap ${!true && 'active'}`}>
                <div className="card-body">
                  <div className="search-content">
                    <Button
                      className="search-back btn-icon toggle-search active"
                      //   onClick={() => {
                      //     setSearchText('');
                      //     toggle();
                      //   }}
                    >
                      <Icon name="arrow-left"></Icon>
                    </Button>
                    <input
                      type="text"
                      className="border-transparent form-focus-none form-control"
                      placeholder="Search by user or email"
                      //   value={onSearchText}
                      //   onChange={(e) => onFilterChange(e)}
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
                <DataTableRow>
                  <span className="sub-text">{selectedListName} Name</span>
                </DataTableRow>

                <DataTableRow className="nk-tb-col-tools text-right">
                  <span className="sub-text">action</span>
                </DataTableRow>
              </DataTableHead>
              {/*Head*/}
              {currentItems.length > 0
                ? currentItems.map((item, i) => {
                    return (
                      <DataTableItem key={i}>
                        <DataTableRow size="md">
                          <span>{item.label}</span>
                        </DataTableRow>
                        <DataTableRow className="nk-tb-col-tools">
                          <ul className="nk-tb-actions gx-1">
                            <li
                              className="nk-tb-action-hidden"
                              //   onClick={() => onEditClick(item._id)}
                            >
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
                                  //   onClick={() => removeInterviewer(item._id)}
                                >
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
                                    <li
                                    //  onClick={() => onEditClick(item._id)}
                                    >
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
                                        <li
                                        // onClick={() => removeInterviewer(item._id)}
                                        >
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
              {listData.length > 0 ? (
                <PaginationComponent
                  itemPerPage={itemPerPage}
                  totalItems={listData.length}
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

        <CommonModal
          isOpen={addModal}
          toggle={() => setAddModal(false)}
          classNameModal="modal-dialog-centered"
          modalSize="lg"
          content={<AddList onFormCancel={onFormCancel} />}
        />
      </Content>
    </React.Fragment>
  );
};
export default List;
