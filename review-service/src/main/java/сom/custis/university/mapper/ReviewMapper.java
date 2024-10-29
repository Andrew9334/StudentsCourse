package сom.custis.university.mapper;

import сom.custis.university.dto.ReviewDTO;
import сom.custis.university.model.Review;

public class ReviewMapper {

    public static ReviewDTO toDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getCourseId(),
                review.getStudentId(),
                review.getContent()
        );
    }

    public static Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setCourseId(review.getCourseId());
        review.setStudentId(review.getStudentId());
        review.setContent(review.getContent());
        return review;
    }
}
