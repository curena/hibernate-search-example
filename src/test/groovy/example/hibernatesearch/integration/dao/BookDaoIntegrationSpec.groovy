package example.hibernatesearch.integration.dao

import com.github.javafaker.Faker
import example.hibernatesearch.HibernateSearchExampleConfiguration
import example.hibernatesearch.dao.BookDao
import example.hibernatesearch.entity.AuthorEntity
import example.hibernatesearch.entity.BookEntity
import example.hibernatesearch.repository.AuthorRepository
import example.hibernatesearch.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.transaction.Transactional

@ContextConfiguration(classes = HibernateSearchExampleConfiguration)
class BookDaoIntegrationSpec extends Specification {
    @Autowired
    BookDao bookDao

    @Autowired
    BookRepository bookRepository

    @Autowired
    AuthorRepository authorRepository

    def faker = Faker.instance()

    @Transactional
    def "should find a book by title"() {
        given:
        def titles = insertStuff()

        when:
        def searchResults = bookDao.searchBookByTitle(titles[0])

        then:
        searchResults.size() == 1
        searchResults[0].title == titles[0]
    }

    def "should find a book by author"() {

    }

    @Transactional //Must be done in a transaction in order for index to have access to it.
    def insertStuff() {
        AuthorEntity author = new AuthorEntity()
        author.setName(faker.book().author())
        authorRepository.save(author)

        BookEntity aBook = new BookEntity()
        def titleOne = "foo"//faker.book().title()
        aBook.setAuthor(author)
        aBook.setTitle(titleOne)

        BookEntity anotherBook = new BookEntity()
        def titleTwo = "bar"//faker.book().title()
        anotherBook.setAuthor(author)
        anotherBook.setTitle(titleTwo)


        bookRepository.save(aBook)
        bookRepository.save(anotherBook)
        author.books = [aBook, anotherBook]
        authorRepository.save(author)

        [titleOne, titleTwo]
    }
}