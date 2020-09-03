package example.hibernatesearch.entity;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.TermVector;
import org.hibernate.search.bridge.builtin.DoubleBridge;

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

  @Field(analyze = Analyze.NO, termVector = TermVector.YES)
  @Facet
  private String title;

  @Field(
      analyze = Analyze.NO,
      termVector = TermVector.YES,
      bridge = @FieldBridge(impl = DoubleBridge.class)//TermVector used for counting matches "more like this"
  )
  @Facet
  private double price;

  @IndexedEmbedded //Used in ManyToMany and *ToOne relations to allow Lucene to index these
  // as part of owning entity. So in this case authors are indexed as part of books.
  @ManyToMany(fetch = FetchType.EAGER)
  private Set<AuthorEntity> authors;

  @Override
  public String toString() {

    String authors = String
        .join(", ", this.authors.stream().map(AuthorEntity::getName).collect(Collectors.toSet()));
    return authors + ": " + title;
  }
}
