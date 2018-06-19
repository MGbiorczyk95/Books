package homework.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import homework.dao.interfaces.BookDao;
import homework.entities.BookEntity;
import homework.services.TOs.AuthorTO;

@RunWith(SpringJUnit4ClassRunner.class)
public class HomeworkServiceTest {

	@Mock
	private BookDao bookDao;
	@InjectMocks
	private HomeworkServiceImpl homeworkService;

	@Before
	public void setup() {

		BookEntity book = new BookEntity();
		book.setIsbn("9781592432172");
		Mockito.when(bookDao.getByISBN("9781592432172")).thenReturn(book);

		List<String[]> categories = new ArrayList<String[]>();
		List<String> isbns = new ArrayList<String>();

		String[] categories1 = { "british", "Science" };
		isbns.add("9781592432171");
		categories.add(categories1);

		String[] categories2 = { "British", "Work" };
		isbns.add("9781592432173");
		categories.add(categories2);

		String[] categories3 = { "C+", "British" };
		isbns.add("9781592432174");
		categories.add(categories3);

		Mockito.when(bookDao.getCategories()).thenReturn(categories);
		Mockito.when(bookDao.getISBNs()).thenReturn(isbns);

		book = new BookEntity();
		book.setIsbn("9781592432171");
		book.setCategories(categories1);
		Mockito.when(bookDao.getByISBN("9781592432171")).thenReturn(book);

		book = new BookEntity();
		book.setIsbn("9781592432173");
		book.setCategories(categories2);
		Mockito.when(bookDao.getByISBN("9781592432173")).thenReturn(book);

		book = new BookEntity();
		book.setIsbn("9781592432174");
		book.setCategories(categories3);
		Mockito.when(bookDao.getByISBN("9781592432174")).thenReturn(book);

		List<String[]> authorsOfBooks = bookDao.getAuthors();
		String[] authors1 = { "Michael" };
		authorsOfBooks.add(authors1);
		String[] authors2 = { "Chris" };
		authorsOfBooks.add(authors2);
		String[] authors3 = { "John" };
		authorsOfBooks.add(authors3);
		String[] authors4 = { "John" };
		authorsOfBooks.add(authors4);
		Mockito.when(bookDao.getAuthors()).thenReturn(authorsOfBooks);

		List<Double> ratings = bookDao.getAverageRatings();
		ratings.add(2.0);
		ratings.add(1.0);
		ratings.add(3.0);
		ratings.add(3.0);
		Mockito.when(bookDao.getAverageRatings()).thenReturn(ratings);
	}

	@Test()
	public void getByISBNSuccess() {
		// given
		String isbn = "9781592432172";
		// when
		BookEntity returnedBook = homeworkService.getByISBN(isbn);
		// then
		Assert.assertEquals(isbn, returnedBook.getIsbn());
	}

	@Test()
	public void getByCategorySuccess() {
		// given
		String category = "British";
		// when
		List<BookEntity> returnedBooks = homeworkService.getByCategory(category);
		String expectedIsbn = "9781592432173";
		String expectedSecondIsbn = "9781592432174";
		// then
		Assert.assertEquals(expectedIsbn, returnedBooks.get(0).getIsbn());
		Assert.assertEquals(expectedSecondIsbn, returnedBooks.get(1).getIsbn());
	}

	@Test()
	public void getByRatingsSuccess() {
		// given

		// when
		List<AuthorTO> returnedAuthors = homeworkService.getByRatingDesc();
		List<AuthorTO> expectedAuthors = new ArrayList<AuthorTO>();
		expectedAuthors.add(new AuthorTO("John", 3.0));
		expectedAuthors.add(new AuthorTO("Michael", 2.0));
		expectedAuthors.add(new AuthorTO("Chris", 1.0));
		// then
		Assert.assertEquals(expectedAuthors, returnedAuthors);
	}
	
}
