package si.fri.rso.samples.reviews.models.entities;

import org.eclipse.persistence.annotations.UuidGenerator;
import si.fri.rso.samples.reviews.models.dtos.Order;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Entity(name = "review")
@NamedQueries(value =
        {
                @NamedQuery(name = "Review.getAll", query = "SELECT r FROM review r")
        })
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "review")
    private String review;


    @Column(name = "author_id")
    private Integer authorId;


    @Column(name="ride_id")
    private Integer rideId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }
}