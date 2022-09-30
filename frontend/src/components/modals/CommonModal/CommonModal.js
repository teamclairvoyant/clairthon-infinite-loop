/* eslint-disable react/prop-types */
import { Modal, ModalBody } from 'reactstrap';
const CommonModal = (props) => {
  const { isOpen, toggle, classNameModal, modalSize, content } = props;
  return (
    <Modal isOpen={isOpen} toggle={toggle} className={classNameModal} size={modalSize}>
      <ModalBody>{content}</ModalBody>
    </Modal>
  );
};

export default CommonModal;
