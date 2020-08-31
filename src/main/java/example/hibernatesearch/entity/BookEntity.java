package example.hibernatesearch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Slf4j
@Data
@Indexed
public class BookEntity {
  @Id
  private long id;

  @Field(analyze = Analyze.NO)
  @Facet
  @Column
  private String title;

  @Column
  private String author;
}
