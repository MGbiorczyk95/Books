package homework.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import homework.dao.interfaces.BookDao;
import homework.entities.BookEntity;
import homework.services.TOs.AuthorTO;
import homework.services.interfaces.HomeworkService;

@Service
public class HomeworkServiceImpl implements HomeworkService {

	@Autowired
	BookDao bookDao;

	@Override
	public BookEntity getByISBN(String isbn) {
		return bookDao.getByISBN(isbn);
	}

	@Override
	public List<BookEntity> getByCategory(String category) {
		List<String[]> categoriesOfBooks = bookDao.getCategories();
		List<String> isbns = bookDao.getISBNs();

		List<BookEntity> booksToReturn = new ArrayList<BookEntity>();

		for (String[] categories : categoriesOfBooks) {
			Arrays.sort(categories, String.CASE_INSENSITIVE_ORDER);
			if (Arrays.binarySearch(categories, category) >= 0) {
				int index = categoriesOfBooks.indexOf(categories);
				booksToReturn.add(bookDao.getByISBN(isbns.get(index)));
			}
		}
		return booksToReturn;
	}

	@Override
	public List<AuthorTO> getByRatingDesc() {
		List<String[]> authorsOfBooks = bookDao.getAuthors();
		List<Double> ratings = bookDao.getAverageRatings();
		List<String> authors = new ArrayList<String>();
		List<Double> sumsOfRatings = new ArrayList<Double>();
		List<Integer> numsOfBooks = new ArrayList<Integer>();

		List<AuthorTO> authorsToReturn = new ArrayList<AuthorTO>();
		int i = 0;
		for (String[] authorsOfBook : authorsOfBooks) {
			for (String authorOfBook : authorsOfBook) {
				int index = authors.indexOf(authorOfBook);
				if (index == -1) {
					authors.add(authorOfBook);
					sumsOfRatings.add(ratings.get(i));
					numsOfBooks.add(1);
				} else {
					sumsOfRatings.set(index, sumsOfRatings.get(index) + ratings.get(i));
					numsOfBooks.set(index, numsOfBooks.get(index) + 1);
				}
			}
			i++;
		}

		for (i = 0; i < authors.size(); i++) {
			authorsToReturn.add(new AuthorTO(authors.get(i), sumsOfRatings.get(i) / numsOfBooks.get(i)));
		}

		authorsToReturn = authorsToReturn.stream()
				.sorted((object1, object2) -> object1.getAverageRating().compareTo(object2.getAverageRating()))
				.collect(Collectors.toList());
		Collections.reverse(authorsToReturn);

		return authorsToReturn;
	}

}
