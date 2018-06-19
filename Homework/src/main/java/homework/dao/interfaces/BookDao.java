package homework.dao.interfaces;

import java.util.List;

import homework.entities.BookEntity;

public interface BookDao extends Dao<BookEntity, Long> {

	public void saveList(List<BookEntity> books);

	public BookEntity getByISBN(String isbn);

	public List<String[]> getAuthors();

	public List<Double> getAverageRatings();

	public List<String[]> getCategories();

	public List<String> getISBNs();
}
