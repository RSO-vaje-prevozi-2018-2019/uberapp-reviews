package si.fri.rso.samples.reviews.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService("uberapp-reviews")
@ApplicationPath("/v1")
public class ReviewApplication extends Application {
}
