package si.fri.rso.samples.reviews.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.samples.reviews.models.entities.Review;
import si.fri.rso.samples.reviews.services.beans.ReviewsBean;
import si.fri.rso.samples.reviews.services.configuration.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Path("reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Log
public class ReviewResource {

    private Logger log = Logger.getLogger(ReviewResource.class.getName());

    @Inject
    private AppProperties appProperties;

    @Inject
    private ReviewsBean reviewsBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getReviews() {

        List<Review> reviews = reviewsBean.getReviews();

        return Response.ok(reviews).build();
    }

    @GET
    @Path("/{reviewId}")
    public Response getReview(@PathParam("reviewId") Integer reviewId) {

        Review review = reviewsBean.getReview(reviewId);

        if (review == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(review).build();
    }

    @POST
    @Path("create")
    public Response createReview(Review review) {

        if (review.getAuthorId() == null || review.getRating() == null || review.getReview() == null || review.getRideId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean success = reviewsBean.createReview(review);

        if (success) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }



}
