import React from 'react';
import { render } from '@testing-library/react';
import { screen } from '@testing-library/dom';
import CommonModal from './CommonModal';
import { TEST_CASES_CONSTANT_STRING } from '../../../constants/constant';

const modalProps = {
  isOpen: true,
  toggle: jest.fn(),
  classNameModal: 'modal-dialog-centered',
  modalSize: 'lg'
};
describe('Common Modal', () => {
  const ModalComponent = () => {
    return <p>{TEST_CASES_CONSTANT_STRING.COMMON_MODAL_TEXT}</p>;
  };
  test('it renders common modal', () => {
    const { container } = render(
      <CommonModal
        isOpen={modalProps.isOpen}
        toggle={() => modalProps.toggle}
        classNameModal="modal-dialog-centered"
        modalSize="lg"
        content={<ModalComponent />}
      />
    );

    expect(container).toBeDefined();
    const requiredErrorMessage = screen.getByText(TEST_CASES_CONSTANT_STRING.COMMON_MODAL_TEXT);
    expect(requiredErrorMessage).toBeInTheDocument();
  });
});
