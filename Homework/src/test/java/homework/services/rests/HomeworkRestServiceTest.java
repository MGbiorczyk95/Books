package homework.services.rests;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import homework.entities.BookEntity;
import homework.services.TOs.AuthorTO;
import homework.services.interfaces.HomeworkService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = HomeworkRestService.class, secure = false)
public class HomeworkRestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private HomeworkService homeworkService;

	@Before
	public void setup() {
		BookEntity book1 = new BookEntity();
		book1.setIsbn("9781592432172");
		book1.setAverageRating(3.0);
		book1.setPublishedDate(621467216);
		book1.setLanguage("en");
		book1.setTitle("Java learning");
		book1.setPageCount(30);
		String[] authors = { "John", "Mikel" };
		book1.setAuthors(authors);
		String[] categories = { "British", "Football" };
		book1.setCategories(categories);

		Mockito.when(homeworkService.getByISBN("9781592432172")).thenReturn(book1);

		BookEntity book2 = new BookEntity();
		book2.setIsbn("9781592432173");
		book2.setAverageRating(4.2);
		book2.setPublishedDate(6323236);
		book2.setLanguage("ru");
		book2.setTitle("Java");
		book2.setPageCount(30);
		String[] authors2 = { "John", "Mikel" };
		book2.setAuthors(authors2);
		String[] categories2 = { "British", "Football" };
		book2.setCategories(categories2);

		BookEntity book3 = new BookEntity();
		book3.setIsbn("9781592432174");
		book3.setAverageRating(3.0);
		book3.setPublishedDate(621467216);
		book3.setLanguage("en");
		book3.setTitle("British learning");
		book3.setPageCount(30);
		book3.setAuthors(authors);
		book1.setCategories(categories);

		List<BookEntity> books = Arrays.asList(book1, book2, book3);

		Mockito.when(homeworkService.getByCategory("British")).thenReturn(books);

		List<AuthorTO> authorsOfBooks = Arrays.asList(new AuthorTO("John", 3.0), new AuthorTO("Michael", 2.5),
				new AuthorTO("Rick", 2.0));
		Mockito.when(homeworkService.getByRatingDesc()).thenReturn(authorsOfBooks);
	}

	@Test
	public void getByISBNSuccess() throws Exception {
		// given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/book/9781592432172")
				.accept(MediaType.APPLICATION_JSON);
		// when
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "{\"isbn\":\"9781592432172\",\"title\":\"Java learning\",\"publishedDate\":621467216,\"pageCount\":30,\"language\":\"en\",\"averageRating\":3.0,\"authors\":[\"John\",\"Mikel\"],\"categories\":[\"British\",\"Football\"]}";
		// then
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getByISBNNotFound() throws Exception {
		// given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/book/9781592432171")
				.accept(MediaType.APPLICATION_JSON);
		// when
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "404 'No results found'";
		// then
		Assert.assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	public void getByCategorySuccess() throws Exception {
		// given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/category/British/books")
				.accept(MediaType.APPLICATION_JSON);
		// when
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"isbn\":\"9781592432172\",\"title\":\"Java learning\",\"publishedDate\":621467216,\"pageCount\":30,\"language\":\"en\",\"averageRating\":3.0,\"authors\":[\"John\",\"Mikel\"],\"categories\":[\"British\",\"Football\"]},{\"isbn\":\"9781592432173\",\"title\":\"Java\",\"publishedDate\":6323236,\"pageCount\":30,\"language\":\"ru\",\"averageRating\":4.2,\"authors\":[\"John\",\"Mikel\"],\"categories\":[\"British\",\"Football\"]},{\"isbn\":\"9781592432174\",\"title\":\"British learning\",\"publishedDate\":621467216,\"pageCount\":30,\"language\":\"en\",\"averageRating\":3.0,\"authors\":[\"John\",\"Mikel\"]}]";
		// then
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getByCategoryBooksNotFound() throws Exception {
		// given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/category/british/books")
				.accept(MediaType.APPLICATION_JSON);
		// when
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[]";
		// then
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void getByRatingDescSuccess() throws Exception {
		// given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/rating").accept(MediaType.APPLICATION_JSON);
		// when
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String expected = "[{\"author\":\"John\",\"averageRating\":3.0},{\"author\":\"Michael\",\"averageRating\":2.5},{\"author\":\"Rick\",\"averageRating\":2.0}]";
		// then
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
	}
}
