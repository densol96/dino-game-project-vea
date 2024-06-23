import styles from './styles/LocationDescription.module.scss';
import { useEffect, useState } from 'react';

function LocationDescription({
  localStorageName,
  heading,
  src,
  alt,
  description,
}) {
  const [descriptionIsOpen, setDescriptionIsOpen] = useState(() => {
    const lastSavedOption = localStorage.getItem(localStorageName);
    if (lastSavedOption !== null)
      return lastSavedOption === 'true' ? true : false;
    return true;
  });

  // useEffect(() => {
  //   console.log(localStorage.getItem(localStorageName), descriptionIsOpen);
  // });

  function closeDescription() {
    localStorage.setItem(localStorageName, false);
    setDescriptionIsOpen(false);
  }

  function openDescription() {
    localStorage.setItem(localStorageName, true);
    setDescriptionIsOpen(true);
  }

  function toggleDescription() {
    descriptionIsOpen ? closeDescription() : openDescription();
  }

  return (
    <header className={styles.Header}>
      <button
        className={styles.closeOpenDescription}
        onClick={toggleDescription}
      >
        {descriptionIsOpen ? (
          <ion-icon id={styles.icon} name="caret-up-outline"></ion-icon>
        ) : (
          <ion-icon id={styles.icon} name="caret-down-outline"></ion-icon>
        )}
      </button>
      <h2 className={styles.Header__Heading}>{heading}!</h2>
      <img className={styles.farmImg} src={src} alt={alt} />
      {descriptionIsOpen && (
        <p className={styles.Header__Description}>{description}</p>
      )}
    </header>
  );
}

export default LocationDescription;
