function Pagination({ page, styles, setPage, pagesTotal }) {
  return (
    <div className="pagination">
      {
        <button className={`${page === 1 ? 'hide' : ''} btnNav`}>
          <ion-icon
            onClick={() => {
              setPage((page) => page - 1);
            }}
            id="icon"
            name="chevron-back-outline"
          ></ion-icon>
        </button>
      }
      {pagesTotal > 1 && <span>{page}</span>}
      {
        <button
          className={`${
            page === pagesTotal || pagesTotal === 0 ? 'hide' : ''
          } btnNav`}
        >
          <ion-icon
            onClick={() => {
              setPage((page) => page + 1);
            }}
            id="icon"
            name="chevron-forward-outline"
          ></ion-icon>
        </button>
      }
    </div>
  );
}

export default Pagination;
