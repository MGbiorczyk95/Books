package homework.services.mappers;

import java.util.List;
import java.util.stream.Collectors;

import homework.entities.BookEntity;
import homework.services.TOs.BookTO;

public class BookMapper {
	public static BookTO map(BookEntity bookEntity) {
		if (bookEntity != null) {
			BookTO bookTO = new BookTO(bookEntity.getIsbn(), bookEntity.getTitle(), bookEntity.getSubtitle(),
					bookEntity.getPublisher(), bookEntity.getPublishedDate(), bookEntity.getDescription(),
					bookEntity.getPageCount(), bookEntity.getThumbnailUrl(), bookEntity.getLanguage(),
					bookEntity.getPreviewLink(), bookEntity.getAverageRating(), bookEntity.getAuthors(),
					bookEntity.getCategories());
			return bookTO;
		}
		return null;
	}

	public static List<BookTO> map2TOs(List<BookEntity> bookEntities) {
		return bookEntities.stream().map(BookMapper::map).collect(Collectors.toList());
	}
}
