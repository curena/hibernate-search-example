package example.hibernatesearch.service;

import example.hibernatesearch.entity.BookEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
  private EntityManagerFactory entityManagerFactory;

  public List<?> searchFor(String query) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(
        BookEntity.class).get();
    Query fullTextQuery = queryBuilder.keyword().onField("title").matching("fooie").createQuery();
    return fullTextEntityManager.createFullTextQuery(fullTextQuery).getResultList();
  }
}
