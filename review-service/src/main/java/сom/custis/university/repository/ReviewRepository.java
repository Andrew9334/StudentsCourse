package сom.custis.university.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import сom.custis.university.model.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
}
