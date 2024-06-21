import styles from './Ratings.module.scss';
function PlayerRatings() {
  return (
    <>
      <div className={styles.optionLine}>
        <div className={styles.sortByOption}>
          <label>Sort players By:</label>
          <select>
            <option value="experience">Experience</option>
            <option value="experience">Total fights</option>
            <option value="experience">Total won</option>
          </select>
        </div>
        <div className={styles.sortByType}>
          <label>Dinosaur-type:</label>
          <select>
            <option value="experience">All</option>
            <option value="experience">Carnivore</option>
            <option value="experience">Herbivore</option>
          </select>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <th>Username</th>
            <th>Type</th>
            <th>Experience</th>
            <th>Total fights</th>
            <th>Total won</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th>solodeni</th>
            <th>Carnivore</th>
            <th>3</th>
            <th>4</th>
            <th>3</th>
          </tr>
          <tr>
            <th>solodeni</th>
            <th>Carnivore</th>
            <th>3</th>
            <th>4</th>
            <th>3</th>
          </tr>
          <tr>
            <th>solodeni</th>
            <th>Carnivore</th>
            <th>3</th>
            <th>4</th>
            <th>3</th>
          </tr>
          <tr>
            <th>solodeni</th>
            <th>Carnivore</th>
            <th>3</th>
            <th>4</th>
            <th>3</th>
          </tr>
          <tr>
            <th>solodeni</th>
            <th>Carnivore</th>
            <th>3</th>
            <th>4</th>
            <th>3</th>
          </tr>
        </tbody>
      </table>
      <div className={styles.pagination}>
        <ion-icon id={styles.icon} name="chevron-back-outline"></ion-icon>
        <span>2</span>
        <ion-icon id={styles.icon} name="chevron-forward-outline"></ion-icon>
      </div>
    </>
  );
}

export default PlayerRatings;
