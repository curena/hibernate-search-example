package example.hibernatesearch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

@Entity
@Slf4j
@Data
@Indexed
@EqualsAndHashCode(of = "id")
@Table(name = "book")
public class BookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Field(termVector = TermVector.YES)
  private String title;

  @ManyToOne
  private AuthorEntity author;

  @Override
  public String toString() {
    return this.author.getName() + ", " + this.title;
  }
}
