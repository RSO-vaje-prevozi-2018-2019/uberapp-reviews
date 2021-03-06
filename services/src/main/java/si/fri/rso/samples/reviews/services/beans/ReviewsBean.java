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

    public Review createReview(Review review) {

        //ali je bil user res na tej vožnji?
        List<User> users = externalBean.getUsersFromRide(review.getRideId());

        if (users.stream().noneMatch(x -> x.getId() == review.getAuthorId())) {//ne
            return null;
        }

        //kreiraj review
        Review newReview = createReviewDB(review);

        System.out.println("ali kreiram notif" + appProperties.isNotificationsEnabled());
        //ali naj kreiram notification?
        if (appProperties.isNotificationsEnabled()) {
            System.out.println("notri");
            int driveId = review.getRideId();
            System.out.println("driveID: " + driveId);
            int driverId = externalBean.getDriverId(driveId);
            System.out.println("driver: " + driverId);
            String text = "Na vašo vožnjo id: '" + driveId + "' je bila ustvarjena recenzija z oceno " + review.getRating() + ".";
            boolean success = externalBean.createNotification( driverId,  driveId,  text);
            System.out.println("ali je vse kuil: "+ success);
        }

        return newReview;

    }

    private Review createReviewDB(Review review) {

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
