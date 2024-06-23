import styles from './LoadingSpinner.module.scss';

function LoadingSpinner({ width, height }) {
  return (
    <span
      style={{
        width: `${width}px`,
        height: `${height}px`,
      }}
      className={styles.loader}
    ></span>
  );
}

export default LoadingSpinner;
