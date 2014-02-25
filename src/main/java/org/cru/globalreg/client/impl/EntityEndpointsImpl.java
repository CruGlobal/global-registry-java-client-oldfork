package org.cru.globalreg.client.impl;

import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.cru.globalreg.client.EntityEndpoints;
import org.cru.globalreg.client.Filter;

import com.google.common.collect.Sets;
import org.cru.globalreg.jackson.GlobalRegistryApiNamingStrategy;

/**
 * Created by ryancarlson on 2/23/14.
 */
public class EntityEndpointsImpl implements EntityEndpoints
{
    //TODO: this should be read/injected from default.properties
    private String apiUrl = new String("https://api.leadingwithinformation.com/entities");
    private String accessToken;
    final private ObjectMapper objectMapper;

    final private static Set<Integer> statusesWithEntity  = Sets.newHashSet(200,201);
    final private static Set<Integer> statusWithoutEntity = Sets.newHashSet(204);

    public EntityEndpointsImpl()
    {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(new GlobalRegistryApiNamingStrategy());
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
    public <T> EntitySearchResponse<T> search(Class<T> entityType, String type, final Filter... filters)
    {
        return this.search(entityType, type, 1, filters);
    }

    @Override
    public <T> EntitySearchResponse<T> search(Class<T> entityType, String type, int page, final Filter... filters)
    {
        WebTarget target = webTarget()
                .queryParam("access_token", accessToken)
                .queryParam("entity_type", type)
                .queryParam("page", page);

        // set the filters on the target
        for (final Filter filter : filters)
        {
            target = target.queryParam(filter.getField(), filter.getValue());
        }

        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return handleSearchResponse(response, (Class<T>) entityType);
    }

    @Override
    public  <T> T get(Class<T> entityType, Integer id, String type)
    {
        Response response = webTarget()
                .path("/" + id)
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        return handleResponse(response, (Class<T>) entityType);
    }

    @Override
    public  <T> T create(T entity, String type)
    {
        Response response = webTarget()
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .post(Entity.json(prepareData(entity)));

       return  handleResponse(response, (Class<T>)entity.getClass());
    }

    @Override
    public  <T> T update(T entity, Integer id, String type)
    {
        Response response = webTarget()
                .path("/" + id)
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .put(Entity.json(prepareData(entity)));

        return handleResponse(response, (Class<T>)entity.getClass());
    }

    @Override
    public void delete(Integer id, String type)
    {
        Response response = webTarget()
                .path("/" + id)
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .delete();
    }

    private WebTarget webTarget()
    {
        Client client = ClientBuilder.newBuilder().build();
        return client.target(apiUrl);
    }

    /**
     * This method prepares the data for a POST or PUT with two important steps
     *  1. It converts the POJO fields into underscore field names in the objectMapper.valueToTree call
     *  2. It wraps the data in an object called "entity" so meet the API requirements
     *    - this requirement may soon be going away :)
     * @param data
     * @param <T>
     * @return
     */
    private <T> JsonNode prepareData(T data)
    {
        JsonNode jsonData = objectMapper.valueToTree(data);

        ObjectNode wrappedJsonData = objectMapper.createObjectNode();
        wrappedJsonData.put("entity", jsonData);

        return wrappedJsonData;
    }


    private <T> T handleResponse(Response response, Class<T> entityType)
    {
        handleErrorResponses(response);

        if(statusesWithEntity.contains(response.getStatus()))
        {
            try
            {
                JsonNode jsonNode = objectMapper.readTree(response.readEntity(String.class));
                return objectMapper.readValue(jsonNode, entityType);

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


    private <T> EntitySearchResponse<T> handleSearchResponse(Response response, Class<T> entityType)
    {
        handleErrorResponses(response);

        if(response.getStatus() == 200)
        {
            try
            {
                EntitySearchResponse<T> searchResults = new EntitySearchResponse<T>();

                JsonNode jsonNode = objectMapper.readTree(response.readEntity(String.class));

                for(JsonNode result : jsonNode.path("entities"))
                {
                    searchResults.getResults().add(objectMapper.readValue(result, entityType));
                }

                searchResults.setMeta(objectMapper.treeToValue(jsonNode.path("meta"), MetaResults.class));

                return searchResults;

            }
            catch(Exception e)
            {
                throw new WebApplicationException(e, 500);
            }
        }

        else throw new IllegalStateException("Unexpected status: " + response.getStatus() + " was returned.");
    }

    private void handleErrorResponses(Response response)
    {
        if(response.getStatus() >= 400)
        {
            throw new WebApplicationException(response);
        }
    }
}
