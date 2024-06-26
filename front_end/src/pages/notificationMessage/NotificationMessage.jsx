import React, { useEffect } from 'react';

const HIDE_MESSAGE_AFTER_MS = 5000;

const NotificationMessage = ({
  error,
  success,
  forDisplay,
  errors,
  resultDispatch,
}) => {
  useEffect(() => {
    let timerId;
    if (forDisplay && (error.status || success.status)) {
      timerId = setTimeout(() => {
        resultDispatch({ type: 'CLOSE' });
      }, HIDE_MESSAGE_AFTER_MS);
    }
    return () => timerId && clearTimeout(timerId);
  }, [forDisplay, error, success]);

  if (!forDisplay || !(error.status || success.status)) {
    return null;
  }

  return (
    <div
      className={`message-container ${
        error.status ? 'error-color' : 'success-color'
      }`}
    >
      <p
        className="message-container__close-btn"
        onClick={() => resultDispatch({ type: 'CLOSE' })}
      >
        X
      </p>
      <h2 className="message-container__heading">
        {error.status ? error.heading : success.heading}
      </h2>
      <p className="message-container__content">
        {error.status ? error.message : success.message}
      </p>
      <ul>
        {errors?.map((e, i) => (
          <li className="message-container__content" key={i}>
            {e}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default NotificationMessage;
