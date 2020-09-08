package example.hibernatesearch.dao;

import example.hibernatesearch.entity.BookEntity;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSelection;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class BookDao {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional(readOnly = true)
  public List<BookEntity> searchBookByTitle(String title) {
    Query keywordQuery = getQueryBuilder()
        .phrase()
        .onField("title")
        .sentence(title)
        .createQuery();
    log.info(keywordQuery.toString());
    List<BookEntity> results = getWrappedJpaQueryFor(keywordQuery).getResultList();

    return results;
  }

  @Transactional(readOnly = true)
  public List<BookEntity> searchAllBooks() {
    return getWrappedJpaQueryForAllBooks().getResultList();
  }

  @Transactional(readOnly = true)
  public List<BookEntity> applyFacetSelection(List<Facet> facets) {
    FacetManager facetManager = getWrappedJpaQueryForAllBooks().getFacetManager();
    for (Facet facet : facets) {
      FacetSelection facetSelection = facetManager.getFacetGroup(facet.getFacetingName());
      facetSelection.selectFacets(facet);
    }

    return searchAllBooks();
  }

  @Transactional(readOnly = true)
  public List<BookEntity> removeFacetSelection(List<Facet> facets) {
    FacetManager facetManager = getWrappedJpaQueryForAllBooks().getFacetManager();
    FacetSelection facetSelection = facetManager.getFacetGroup("price");
    facets.forEach(facetSelection::deselectFacets);

    return searchAllBooks();
  }

  @Transactional(readOnly = true)
  public List<Facet> getTitleFacets() {
    FacetingRequest titleFacetingRequest = getQueryBuilder().facet()
        .name("titleFacetingRequest")
        .onField("title")
        .discrete()
        .createFacetingRequest();

    FacetManager facetManager = getWrappedJpaQueryForAllBooks().getFacetManager();
    facetManager.enableFaceting(titleFacetingRequest);
    List<Facet> facets = facetManager.getFacets("titleFacetingRequest");
    facets.forEach(facet -> log.info("{} - {}", facet.getValue(), facet.getCount()));
    return facets;
  }

  @Transactional(readOnly = true)
  public List<Facet> getAuthorFacets() {
    FacetingRequest authorFacetingRequest = getQueryBuilder().facet()
        .name("authorFacetingRequest")
        .onField("authors.name")
        .discrete()
        .createFacetingRequest();

    FacetManager facetManager = getWrappedJpaQueryForAllBooks().getFacetManager();
    facetManager.enableFaceting(authorFacetingRequest);

    return facetManager.getFacets("authorFacetingRequest");
  }

  @Transactional(readOnly = true)
  public List<Facet> getPriceFacets() {
    FacetingRequest priceFacetingRequest = getQueryBuilder().facet()
        .name("priceFacetingRequest")
        .onField("price")
        .range()
        .from(10.0D).to(20.0D)
        .createFacetingRequest();

    FacetManager facetManager = getWrappedJpaQueryForAllBooks().getFacetManager();
    facetManager.enableFaceting(priceFacetingRequest);
    List<Facet> facets = facetManager.getFacets("priceFacetingRequest");
    facets.forEach(facet -> log.info("{} - {}", facet.getValue(), facet.getCount()));
    return facets;
  }

  @Transactional
  public void purgeIndices() {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    fullTextEntityManager.purgeAll(BookEntity.class);
  }

  public Set<String> getAvailableFacets() {
    Set<String> fields = new HashSet<>();
    for (Field field : BookEntity.class.getDeclaredFields()) {
      if (field.getAnnotation(org.hibernate.search.annotations.Facet.class) != null) {
        fields.add(field.getName());
      }
    }
    return fields;
  }

  private FullTextQuery getWrappedJpaQueryFor(org.apache.lucene.search.Query luceneQuery) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    return fullTextEntityManager.createFullTextQuery(luceneQuery, BookEntity.class);
  }

  private FullTextQuery getWrappedJpaQueryForAllBooks() {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    Query query = getQueryBuilder().all().createQuery();
    return fullTextEntityManager
        .createFullTextQuery(query, BookEntity.class);
  }

  private QueryBuilder getQueryBuilder() {

    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    return fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder()
        .forEntity(BookEntity.class)
        .get();
  }
}
