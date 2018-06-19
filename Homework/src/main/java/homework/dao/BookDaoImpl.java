package homework.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import homework.dao.interfaces.BookDao;
import homework.entities.BookEntity;

@Repository
public class BookDaoImpl extends AbstractDao<BookEntity, Long> implements BookDao {

	@Override
	@Transactional
	public void saveList(List<BookEntity> books) {
		for (BookEntity book : books)
			entityManager.persist(book);
	}

	@Override
	public BookEntity getByISBN(String isbn) {
		try {
			TypedQuery<BookEntity> query = entityManager
					.createQuery("select book from BookEntity book where book.isbn = :isbn", BookEntity.class);
			query.setParameter("isbn", isbn);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<String[]> getAuthors() {
		TypedQuery<String[]> query = entityManager.createQuery(
				"select authors from BookEntity book where book.authors is not null and book.averageRating > 0",
				String[].class);
		return query.getResultList();
	}

	@Override
	public List<Double> getAverageRatings() {
		TypedQuery<Double> query = entityManager
				.createQuery("select averageRating from BookEntity book where book.averageRating > 0", Double.class);
		return query.getResultList();
	}

	@Override
	public List<String[]> getCategories() {
		TypedQuery<String[]> query = entityManager.createQuery(
				"select categories from BookEntity book where book.categories is not null", String[].class);
		return query.getResultList();

	}

	@Override
	public List<String> getISBNs() {
		TypedQuery<String> query = entityManager
				.createQuery("select isbn from BookEntity book where book.categories is not null", String.class);
		return query.getResultList();
	}
}
