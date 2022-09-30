import React from 'react';
import PropTypes from 'prop-types';
import {
  CATCH_MESSAGE,
  COPY,
  LIST_TYPE,
  METHODS,
  RESPONSE_MESSAGE,
  TEST_ID,
  URL_ENDPOINTS
} from '../../../../constants/constant';
import { Button, Col, Form, FormGroup } from 'reactstrap';
import { ErrorMessage } from '../../../../components/error-message/ErrorMessage';
import { Icon, RSelect } from '../../../../components/Component';
import { Controller, useForm } from 'react-hook-form';
import { request } from '../../../../utils/axiosUtils';
import { errorToast, successToast } from '../../../../components/toastr/Tostr';

export const AddList = ({ onFormCancel }) => {
  const { errors, register, handleSubmit, control } = useForm();

  const onFormSubmit = (listData) => {
    const {
      name,
      location: { value }
    } = listData;
    const postData = {
      type: value,
      action: 'ADD',
      items: [name]
    };
    const responseData = request({
      url: URL_ENDPOINTS.ADD_LISTING,
      method: METHODS.POST,
      data: postData
    });

    responseData
      .then((response) => {
        if (response.data.STATUS === RESPONSE_MESSAGE.SUCCESS) {
          successToast(COPY.INTERVIEWER_ADDED);
          onFormCancel(listData);
        } else {
          errorToast(CATCH_MESSAGE.ERROR);
        }
      })
      .catch((err) => {
        err && errorToast(err);
      });
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
        data-testid={TEST_ID.COMMON.ON_FORM_CANCEL_ANCHOR}>
        <Icon name="cross-sm"></Icon>
      </a>
      <div className="p-2">
        <h5 className="title">{COPY.ADD_LIST}</h5>
        <div className="mt-4">
          <Form
            data-testid={TEST_ID.ADD_EVENT.FORM}
            className="row gy-4"
            noValidate
            onSubmit={handleSubmit(onFormSubmit)}>
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
                {errors.name && <span className="invalid">{errors.name.message}</span>}
              </FormGroup>
            </Col>

            <Col md="6">
              <FormGroup>
                <label className="form-label" htmlFor={COPY.TYPE} aria-label={COPY.TYPE}>
                  {COPY.TYPE}
                </label>
                <div className="form-control-wrap">
                  <Controller
                    control={control}
                    name="location"
                    defaultValue={LIST_TYPE[0]}
                    render={({ onChange, value, ref }) => (
                      <RSelect
                        inputRef={ref}
                        options={LIST_TYPE}
                        value={value}
                        name={COPY.TYPE}
                        id={COPY.TYPE}
                        inputId={COPY.TYPE}
                        placeholder={COPY.PLACEHOLDER_LOCATION}
                        onChange={(selectedType) => onChange(selectedType)}
                      />
                    )}
                  />
                </div>
              </FormGroup>
            </Col>

            <Col size="12">
              <span>
                <ErrorMessage content={''} />
              </span>
            </Col>

            <Col size="12">
              <ul className="align-center flex-wrap flex-sm-nowrap gx-4 gy-2">
                <li>
                  <Button
                    color="primary"
                    size="md"
                    type="submit"
                    data-testid={TEST_ID.COMMON.SUMBIT_BUTTON}>
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
                    data-testid={TEST_ID.COMMON.ON_FORM_CANCEL}>
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

AddList.propTypes = {
  onFormCancel: PropTypes.func
};
