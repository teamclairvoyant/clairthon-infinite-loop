import { toast } from "react-toastify";
// import { CloseButton } from "react-toastify/dist/components";
import "react-toastify/dist/ReactToastify.css";

export const successToast = (content) => {
  //   toast.success("Wow so easy!");
  toast.success(content, {
    position: "top-right",
    autoClose: true,
    hideProgressBar: true,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: false,
  });
};

export const errorToast = (content) => {
  //   toast.success("Wow so easy!");
  toast.error(content, {
    position: "top-right",
    autoClose: true,
    hideProgressBar: true,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: false,
  });
};
