package example.hibernatesearch.repository;

import example.hibernatesearch.entity.BookEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Long> {
  List<BookEntity> findByTitle(String title);
}
