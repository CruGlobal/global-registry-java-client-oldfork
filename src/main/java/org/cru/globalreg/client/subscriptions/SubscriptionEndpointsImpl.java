package org.cru.globalreg.client.subscriptions;

import com.google.common.collect.Sets;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.cru.globalreg.client.ErrorHandler;
import org.cru.globalreg.jackson.GlobalRegistryApiNamingStrategy;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class SubscriptionEndpointsImpl implements SubscriptionEndpoints
{
    private String apiUrl = new String("http://gr.stage.uscm.org/subscriptions");
    private String accessToken;
    final private ObjectMapper objectMapper;

    final private static Set<Integer> statusesWithEntity  = Sets.newHashSet(200, 201);
    final private static Set<Integer> statusWithoutEntity = Sets.newHashSet(204);

    public SubscriptionEndpointsImpl()
    {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(new GlobalRegistryApiNamingStrategy());
        this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public final void setApiUrl(final String apiUrl)
    {
        this.apiUrl = apiUrl;
    }

    public final void setAccessToken(final String accessToken)
    {
        this.accessToken = accessToken;
    }

    @Override
    public void subscribe(Subscription subscription)
    {
        Response response = webTarget()
                .queryParam("access_token", accessToken)
                .request()
                .post(Entity.json(subscription));

        ErrorHandler.handleErrorResponses(response);
    }

    private WebTarget webTarget()
    {
        Client client = ClientBuilder.newBuilder().build();
        return client.target(apiUrl);
    }
}
