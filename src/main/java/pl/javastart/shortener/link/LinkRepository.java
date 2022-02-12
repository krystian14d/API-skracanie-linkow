package pl.javastart.shortener.link;

import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends CrudRepository<Link, String> {

}
