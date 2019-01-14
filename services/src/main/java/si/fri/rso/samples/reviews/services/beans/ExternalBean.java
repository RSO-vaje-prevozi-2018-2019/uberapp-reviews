package si.fri.rso.samples.reviews.services.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import si.fri.rso.samples.reviews.models.dtos.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class ExternalBean {

    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        objectMapper = new ObjectMapper();
    }

    public List<User> getUsersFromRide(int rideId) {
        return new ArrayList<>();
    }

    public boolean createNotification(){
        return false;
    }

    private static String getJSONResponse(String fullUrl) {
        try {
            HttpGet request = new HttpGet(fullUrl);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null)
                    return EntityUtils.toString(entity);
            } else {
                String msg = "Remote server '" + fullUrl + "' is responded with status " + status + ".";
                // todo logging
                throw new InternalServerErrorException(msg);
            }

        } catch (IOException e) {
            String msg = e.getClass().getName() + " occured: " + e.getMessage();
            // todo logging
            throw new InternalServerErrorException(msg);
        }
        return "{}"; //empty json
    }

//    private List<AccountOptions> getObjects(String json) throws IOException {
//        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, AccountOptions.class));
//    }
}
