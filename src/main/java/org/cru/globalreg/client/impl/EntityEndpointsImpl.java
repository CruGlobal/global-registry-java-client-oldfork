package org.cru.globalreg.client.impl;

import com.google.common.collect.Sets;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.cru.globalreg.client.EntityEndpoints;
import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * Created by ryancarlson on 2/23/14.
 */
public class EntityEndpointsImpl implements EntityEndpoints
{
    //TODO: this should be read/injected from default.properties
    private String apiUrl = new String("https://api.leadingwithinformation.com/entities");

    final private static Set<Integer> statusesWithEntity  = Sets.newHashSet(200,201);
    final private static Set<Integer> statusWithoutEntity = Sets.newHashSet(204);

    @Override
    public JsonNode get(String id, String type, String accessToken)
    {
        Response response = webTarget()
                .path("/" + id)
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return handleResponse(response);
    }

    @Override
    public JsonNode create(JsonNode entity, String type, String accessToken)
    {
        Response response = webTarget()
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .post(Entity.json(entity));

       return  handleResponse(response);
    }

    @Override
    public JsonNode update(JsonNode entity, String type, String accessToken)
    {
        Response response = webTarget()
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .put(Entity.json(entity));

        return handleResponse(response);
    }

    @Override
    public JsonNode delete(JsonNode entity, String type, String accessToken)
    {
        Response response = webTarget()
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .delete();

       return handleResponse(response);
    }

    private WebTarget webTarget()
    {
        Client client = ClientBuilder.newBuilder().build();
        return client.target(apiUrl);
    }

    private JsonNode handleResponse(Response response)
    {
        if(response.getStatus() >= 400)
        {
            throw new WebApplicationException(response);
        }

        else if(statusesWithEntity.contains(response.getStatus()))
        {
            try
            {
                return new ObjectMapper().readTree(response.readEntity(String.class));
            }
            catch(Exception e)
            {
                throw new WebApplicationException(e, 500);
            }
        }

        else if(statusWithoutEntity.contains(response.getStatus()))
        {
            return null;
        }

        else throw new IllegalStateException("Unexpected status: " + response.getStatus() + " was returned.");
    }
}
