package si.fri.rso.samples.reviews.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.samples.reviews.services.beans.ReviewsBean;
import si.fri.rso.samples.reviews.services.configuration.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@Path("settings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SettingsResource {

    @Inject
    private AppProperties appProperties;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("enableNotifications")
    public Response enableNotifications() {
        appProperties.setNotificationsEnabled(true);
        if (appProperties.isNotificationsEnabled()) {
            return Response.accepted().build();
        } else {
            return Response.notModified().build();
        }

    }

    @GET
    @Path("disableNotifcations")
    public Response disableNotifcations() {
        appProperties.setNotificationsEnabled(false);
        if (!appProperties.isNotificationsEnabled()) {
            return Response.accepted().build();
        } else {
            return Response.notModified().build();
        }
    }
}
