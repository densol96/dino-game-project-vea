import { useReducer } from 'react';

function resultReducer(state, action) {
  switch (action.type) {
    case 'SUCCESS':
      return {
        buttonClickable: false,
        isLoading: false,
        forDisplay: true,
        error: {
          status: false,
          heading: '',
          message: '',
          type: '',
          errors: [],
        },
        success: {
          status: true,
          heading: action.payload.heading,
          message: action.payload.message,
        },
      };
    case 'ERROR':
      return {
        buttonClickable: true,
        isLoading: false,
        forDisplay: true,
        error: {
          status: true,
          heading: action.payload.heading,
          message: action.payload.message,
          type: action.payload.type,
          errors: action.payload.errors,
        },
        success: {
          status: false,
          heading: '',
          message: '',
        },
      };
    case 'IS_LOADING':
      return {
        buttonClickable: false,
        isLoading: true,
        forDisplay: false,
        error: {
          status: false,
          heading: '',
          message: '',
          type: '',
          errors: [],
        },
        success: {
          status: false,
          heading: '',
          message: '',
        },
      };
    case 'BUTTON_CLICKABLE':
      return {
        ...state,
        error: { ...state.error },
        success: { ...state.success },
        buttonClickable: true,
      };
    case 'CLOSE':
      return {
        buttonClickable: true,
        isLoading: false,
        forDisplay: false,
        error: {
          status: false,
          heading: '',
          message: '',
          type: '',
          errors: [],
        },
        success: {
          status: false,
          heading: '',
          message: '',
        },
      };
    default:
      throw new Error('💥 -> Check reponseReducer');
  }
}

function useResponseResult() {
  const [
    { success, error, forDisplay, isLoading, buttonClickable },
    resultDispatch,
  ] = useReducer(resultReducer, {
    buttonClickable: true,
    isLoading: false,
    forDisplay: false,
    error: {
      status: false,
      heading: '',
      message: '',
      type: '',
      errors: '',
    },
    success: {
      status: false,
      heading: '',
      message: '',
    },
  });

  return [
    { success, error, forDisplay, isLoading, buttonClickable },
    resultDispatch,
  ];
}

function reduceValidationErrors(errors) {
  let parsedErrors = [];
  for (let field in errors) {
    parsedErrors.push(errors[field]);
  }
  return parsedErrors;
}

function styleNavLink(isActive) {
  return isActive.isActive ? `isActive navLink` : `navLink`;
}

function formatDate(date) {
  const d = new Date(date);
  const day = d.getDate() < 10 ? `0${d.getDate()}` : d.getDate();
  const month = d.getMonth() < 10 ? `0${d.getMonth()}` : d.getMonth();
  const hrs = d.getHours() < 10 ? `0${d.getHours()}` : d.getHours();
  const mins = d.getMinutes() < 10 ? `0${d.getMinutes()}` : d.getMinutes();
  const stringDateTime = `${day}/${month}/${d.getFullYear()} ${hrs}:${mins}`;
  return stringDateTime;
}

function handleBadRequest(e, resultDispatch) {
  if (e.code === 'ERR_BAD_REQUEST') {
    const error = e.response.data;
    resultDispatch({
      type: 'ERROR',
      payload: {
        heading: error.name,
        message: error.message,
        type: error.type,
        errors: error.errors,
      },
    });
  } else if (e.code === 'ERR_NETWORK') {
    resultDispatch({
      type: 'ERROR',
      payload: {
        heading: 'Service is currently unavailable',
        message:
          'Registration is currently unavailable! Please,try again later!',
        type: 'ERR_NETWORK',
        errors: [],
      },
    });
  }
}

const BASE_API_URL = 'http://localhost:8080/api/v1';

function capitalize(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

export {
  useResponseResult,
  reduceValidationErrors,
  styleNavLink,
  formatDate,
  handleBadRequest,
  BASE_API_URL,
  capitalize,
};
