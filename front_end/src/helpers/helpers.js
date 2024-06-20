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
          errors: '',
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
          errors: '',
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
          errors: '',
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

export { useResponseResult, reduceValidationErrors, styleNavLink };
