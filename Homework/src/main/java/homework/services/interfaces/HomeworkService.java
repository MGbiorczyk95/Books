package homework.services.interfaces;

import java.util.List;

import homework.entities.BookEntity;
import homework.services.TOs.AuthorTO;

public interface HomeworkService {

	public BookEntity getByISBN(String isbn);

	public List<BookEntity> getByCategory(String category);

	public List<AuthorTO> getByRatingDesc();

}
