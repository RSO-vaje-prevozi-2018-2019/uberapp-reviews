package si.fri.rso.samples.reviews.services.beans;

import si.fri.rso.samples.reviews.models.dtos.User;
import si.fri.rso.samples.reviews.models.entities.Review;
import si.fri.rso.samples.reviews.services.configuration.AppProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.List;


@RequestScoped
public class ReviewsBean {

//    private Logger log = Logger.getLogger(ReviewsBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private AppProperties appProperties;

    @Inject
    private ReviewsBean reviewsBean;

    @Inject
    private ExternalBean externalBean;

    private Client httpClient;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
//        baseUrl = "http://localhost:8081"; // only for demonstration
    }


    public List<Review> getReviews() {

        TypedQuery<Review> query = em.createNamedQuery("Review.getAll", Review.class);

        return query.getResultList();

    }


    public Review getReview(Integer reviewId) {

        Review review = em.find(Review.class, reviewId);

        if (review == null) {
            throw new NotFoundException();
        }

        return review;
    }

    public boolean createReview(Review review) {

        //ali je bil user res na tej vožnji?
        List<User> users = externalBean.getUsersFromRide(review.getRideId());

        if (users.stream().noneMatch(x -> x.getId() == review.getAuthorId())) {//ne
            return false;
        }

        //kreiraj review
        createReviewDB(review);

        //ali naj kreiram notification?

        //kreiraj notification
        boolean success = externalBean.createNotification();

        return success;

    }

    public Review createReviewDB(Review review) {

        try {
            beginTx();
            em.persist(review);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return review;
    }

    public Review putReview(String reviewId, Review review) {

        Review c = em.find(Review.class, reviewId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            review.setId(c.getId());
            review = em.merge(review);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return review;
    }

    public boolean deleteReview(String reviewId) {

        Review review = em.find(Review.class, reviewId);

        if (review != null) {
            try {
                beginTx();
                em.remove(review);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
