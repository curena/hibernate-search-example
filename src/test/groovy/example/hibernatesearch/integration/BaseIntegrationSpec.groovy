package example.hibernatesearch.integration

import example.hibernatesearch.HibernateSearchExampleConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.persistence.EntityManager

@ContextConfiguration(classes = HibernateSearchExampleConfiguration)
class BaseIntegrationSpec extends Specification {
    @Autowired
    EntityManager entityManager

    def "should_load_context"() {
        expect:
        entityManager
    }
}