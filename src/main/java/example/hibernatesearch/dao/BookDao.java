package example.hibernatesearch.dao;

import example.hibernatesearch.entity.BookEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class BookDao {
  @PersistenceContext
  private EntityManager entityManager;

  public List<BookEntity> searchBookByTitle(String title) {
    Query keywordQuery = getQueryBuilder()
        .phrase()
        .onField("title")
        .sentence(title)
        .createQuery();

    List<BookEntity> results = getJpaQuery(keywordQuery).getResultList();

    return results;
  }

  public List<BookEntity> searchBookByAuthor(String author) {
    Query keywordQuery = getQueryBuilder()
        .keyword()
        .onField()
        .matching(author)
        .createQuery();

    List<BookEntity> results = getJpaQuery(keywordQuery).getResultList();

    return results;
  }

  private FullTextQuery getJpaQuery(org.apache.lucene.search.Query luceneQuery) {

    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    return fullTextEntityManager.createFullTextQuery(luceneQuery, BookEntity.class);
  }

  private QueryBuilder getQueryBuilder() {

    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    return fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder()
        .forEntity(BookEntity.class)
        .get();
  }
}
