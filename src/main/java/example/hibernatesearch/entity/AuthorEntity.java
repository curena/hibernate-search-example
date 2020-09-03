package example.hibernatesearch.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Slf4j
@Data
@Indexed
@EqualsAndHashCode(of = "id")
@Table(name = "author")
public class AuthorEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Field(analyze = Analyze.NO)
  @Facet
  @Column
  private String name;

//  @ManyToMany
//  private Set<BookEntity> books;

  @Override
  public String toString() {
    return this.name;
  }
}
