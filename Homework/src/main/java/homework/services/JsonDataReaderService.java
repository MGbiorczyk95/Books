package homework.services;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import homework.dao.interfaces.BookDao;
import homework.entities.BookEntity;

@Service
public class JsonDataReaderService implements CommandLineRunner {

	@Autowired
	BookDao bookDao;

	@Override
	public void run(String... args) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/" + args[0]);
		JsonNode booksJ = mapper.readTree(inputStream);
		int numOfBooks = booksJ.get("items").size();
		List<BookEntity> books = new ArrayList<BookEntity>();

		for (int i = 0; i < numOfBooks; i++) {
			JsonNode volumeInfo = booksJ.get("items").get(i).get("volumeInfo");
			BookEntity book = new BookEntity();

			int sizeOfIndustryIdentifiers = volumeInfo.get("industryIdentifiers").size();
			for (int j = 0; j < sizeOfIndustryIdentifiers; j++) {
				String typeOfIndustryID = volumeInfo.get("industryIdentifiers").get(j).get("type").textValue();
				if (typeOfIndustryID.equals("ISBN_13") || typeOfIndustryID.equals("OTHER")) {
					book.setIsbn(volumeInfo.get("industryIdentifiers").get(j).get("identifier").textValue());
					break;
				}
			}

			if (volumeInfo.has("title"))
				book.setTitle(volumeInfo.get("title").textValue());

			if (volumeInfo.has("subTitle"))
				book.setSubtitle(volumeInfo.get("subTitle").textValue());

			if (volumeInfo.has("publisher"))
				book.setPublisher(volumeInfo.get("publisher").textValue());

			if (volumeInfo.has("publishedDate")) {
				String dateS = volumeInfo.get("publishedDate").textValue();
				DateFormat formatter;
				if (dateS.length() > 4)
					formatter = new SimpleDateFormat("yyyy-mm-dd");
				else
					formatter = new SimpleDateFormat("yyyy");

				Date date = (Date) formatter.parse(dateS);
				long longDate = date.getTime();
				book.setPublishedDate(longDate);
			}

			if (volumeInfo.has("description")) {
				if (volumeInfo.get("description").textValue().length() > 255) {
					String description = volumeInfo.get("description").textValue().substring(0, 255);
					book.setDescription(description);
				} else
					book.setDescription(volumeInfo.get("description").textValue());
			}

			if (volumeInfo.has("pageCount"))
				book.setPageCount(volumeInfo.get("pageCount").intValue());

			if (volumeInfo.has("imageLinks") && volumeInfo.get("imageLinks").has("thumbnail"))
				book.setThumbnailUrl(volumeInfo.get("imageLinks").get("thumbnail").textValue());

			if (volumeInfo.has("language"))
				book.setLanguage(volumeInfo.get("language").textValue());

			if (volumeInfo.has("previewLink"))
				book.setPreviewLink(volumeInfo.get("previewLink").textValue());

			if (volumeInfo.has("averageRating"))
				book.setAverageRating(volumeInfo.get("averageRating").doubleValue());

			if (volumeInfo.has("authors")) {
				int numOfAuthors = volumeInfo.get("authors").size();
				String[] authors = new String[numOfAuthors];
				for (int j = 0; j < numOfAuthors; j++) {
					authors[j] = volumeInfo.get("authors").get(j).textValue();
				}
				book.setAuthors(authors);
			}

			if (volumeInfo.has("categories")) {
				int numOfCategories = volumeInfo.get("categories").size();
				String[] categories = new String[numOfCategories];
				for (int j = 0; j < numOfCategories; j++) {
					categories[j] = volumeInfo.get("categories").get(j).textValue();
				}
				book.setCategories(categories);
			}
			books.add(book);
		}

		bookDao.saveList(books);
	}

}
