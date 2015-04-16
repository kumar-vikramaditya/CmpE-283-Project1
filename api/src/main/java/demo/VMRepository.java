package demo;
import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "vms", path = "vms")
public interface VMRepository extends MongoRepository<VM, String> {

	VM findById(@Param("id") long id);
	ArrayList<VM> findByUserId(@Param("userId") long userId);
	//User findByEmail(@Param("email") String email);
}
