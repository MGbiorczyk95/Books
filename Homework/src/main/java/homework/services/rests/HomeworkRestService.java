package homework.services.rests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import homework.services.TOs.AuthorTO;
import homework.services.TOs.BookTO;
import homework.services.interfaces.HomeworkService;
import homework.services.mappers.BookMapper;


@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api")
public class HomeworkRestService {

	private HomeworkService homeworkService;

	@Autowired
	public HomeworkRestService(HomeworkService bookService) {
		this.homeworkService = bookService;
	}

	@RequestMapping(value = "/book/{isbn}", method = RequestMethod.GET)
	@ResponseBody
	public Object getByISBN(@PathVariable String isbn) {
		BookTO bookTO = BookMapper.map(homeworkService.getByISBN(isbn));
		if (bookTO != null)
			return bookTO;
		else
			return new String("404 'No results found'");
	}

	@RequestMapping(value = "/category/{categoryName}/books", method = RequestMethod.GET)
	@ResponseBody
	public List<BookTO> getByCategory(@PathVariable String categoryName) {
		return BookMapper.map2TOs(homeworkService.getByCategory(categoryName));
	}

	@RequestMapping(value = "/rating", method = RequestMethod.GET)
	@ResponseBody
	public List<AuthorTO> getByRatingDesc() {
		return homeworkService.getByRatingDesc();
	}
}
