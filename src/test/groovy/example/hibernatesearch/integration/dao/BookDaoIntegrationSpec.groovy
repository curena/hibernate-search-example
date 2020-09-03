package example.hibernatesearch.integration.dao

import com.github.javafaker.Faker
import example.hibernatesearch.HibernateSearchExampleConfiguration
import example.hibernatesearch.dao.BookDao
import example.hibernatesearch.entity.AuthorEntity
import example.hibernatesearch.entity.BookEntity
import example.hibernatesearch.repository.AuthorRepository
import example.hibernatesearch.repository.BookRepository
import org.jeasy.random.EasyRandom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.EntityManager

@ContextConfiguration(classes = HibernateSearchExampleConfiguration)
class BookDaoIntegrationSpec extends Specification {
    @Autowired
    BookDao bookDao

    @Autowired
    BookRepository bookRepository

    @Autowired
    AuthorRepository authorRepository

    Set<AuthorEntity> authorEntities

    def faker = Faker.instance()

    def cleanup() {
        bookRepository.deleteAll()
        authorRepository.deleteAll()
    }

    def "should find a book by title"() {
        given:
        def titles = insertStuff()

        when:
        def searchResults = bookDao.searchBookByTitle(titles[0])

        then:
        searchResults.size() == 1
        searchResults[0].title == titles[0]
    }

    def "should get available facets"() {
        when: "gets available facets - in the case of BookEntity it's 'price' and 'title'"
        def availableFacets = bookDao.getAvailableFacets()

        then:
        availableFacets.size() == 2
        availableFacets.contains("price")
        availableFacets.contains("title")
    }

    def "should get correct number of facet occurrences"() {
        given:
        insertStuff()

        when:
        def facets = bookDao.getAuthorFacets()

        then:
        facets.size() == 3
        facets[0].count == 2
        facets[0].value == authorEntities[0].name

        cleanup:
        bookDao.purgeIndices()
    }

    def insertStuff() {
        def easyRandom = new EasyRandom()
        Set<AuthorEntity> unsavedAuthors = new HashSet<>()
        5.times ({
            def author = easyRandom.nextObject(AuthorEntity)
            author.name = faker.book().author()
            unsavedAuthors.add(author)
        })

        authorRepository.saveAll(unsavedAuthors)
        authorEntities = authorRepository.findAll() as Set

        BookEntity aBook = new BookEntity()
        def titleOne = faker.book().title()
        aBook.setPrice(12.99)
        aBook.setAuthors([authorEntities[0], authorEntities[1]] as Set)
        aBook.setTitle(titleOne)


        BookEntity anotherBook = new BookEntity()
        def titleTwo = faker.book().title()
        anotherBook.setAuthors([authorEntities[2], authorEntities[0]] as Set)
        anotherBook.setPrice(11.99)
        anotherBook.setTitle(titleTwo)

        bookRepository.saveAll([aBook, anotherBook])

        [titleOne, titleTwo]
    }
}