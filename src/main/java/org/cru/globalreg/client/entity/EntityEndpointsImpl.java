package org.cru.globalreg.client.entity;

import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Throwables;

import org.cru.globalreg.client.Filter;
import org.cru.globalreg.client.JsonConverter;
import org.cru.globalreg.jackson.GlobalRegistryApiNamingStrategy;

import com.google.common.collect.Sets;

/**
 * Created by ryancarlson on 2/23/14.
 */
public class EntityEndpointsImpl implements EntityEndpoints
{
    private String apiUrl;
    private String accessToken;
    final private ObjectMapper objectMapper;

    final private static Set<Integer> statusesWithEntity  = Sets.newHashSet(200,201);
    final private static Set<Integer> statusWithoutEntity = Sets.newHashSet(204);

    public EntityEndpointsImpl()
    {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(new GlobalRegistryApiNamingStrategy());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
    public <T> EntitySearchResponse<T> search(EntityClass<T> entityClass, String type, final Filter... filters)
    {
        return this.search(entityClass, type, 1, filters);
    }

    @Override
    public <T> EntitySearchResponse<T> search(EntityClass<T> entityClass, String type, int page, final Filter... filters)
    {
        WebTarget target = webTarget()
                .queryParam("access_token", accessToken)
                .queryParam("entity_type", type)
                .queryParam("page", page);

        // set the filters on the target
        for (final Filter filter : filters)
        {
            if (filter.isValid())
            {
                target = target.queryParam(filter.getFilter(), filter.getValue());
            }
        }

        Response response = target
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        handleErrorResponses(response);

        final JsonNode json = handleResponse(response);

        if (json != null)
        {
            final EntitySearchResponse<T> searchResults = new EntitySearchResponse<T>();

            for (final JsonNode entity : json.path("entities"))
            {
                searchResults.add(JsonConverter.treeToValue(entity.path(type), entityClass.getType()));
            }

            try
            {
                searchResults.setMeta(objectMapper.treeToValue(json.path("meta"), MetaResults.class));
            }
            catch(Exception checkedException)
            {
                Throwables.propagate(checkedException);
            }
            return searchResults;
        }

        return null;
    }

    @Override
    public  <T> T get(EntityClass<T> entityClass, Integer id, String type)
    {
        Response response = webTarget()
                .path("/" + id)
                .queryParam("entity_type", type)
                .queryParam("access_token", accessToken)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        final JsonNode json = handleResponse(response);
        if (json != null)
        {
            return JsonConverter.treeToValue(json, entityClass.getType(), "entity", type);
        }

        return null;
    }

    @Override
    public  <T> T create(EntityData<T> entityData, String type)
    {
        Response response = webTarget()
                .queryParam("access_token", accessToken)
                .request()
                .post(Entity.json(prepareData(entityData.getData(), type)));

        final JsonNode json = handleResponse(response);
        if (json != null)
        {
            return JsonConverter.treeToValue(json, entityData.getType(), "entity", type);
        }

        return null;
    }

    @Override
    public  <T> T update(EntityData<T> entityData, Integer id, String type)
    {
        Response response = webTarget()
                .path("/" + id)
                .queryParam("access_token", accessToken)
                .request()
                .put(Entity.json(prepareData(entityData.getData(), type)));

        final JsonNode json = handleResponse(response);
        if (json != null)
        {
            return JsonConverter.treeToValue(json, entityData.getType(), "entity", type);
        }

        return null;
    }

    @Override
    public void delete(Integer id, String type)
    {
        Response response = webTarget()
                .path("/" + id)
                .queryParam("access_token", accessToken)
                .request()
                .delete();
    }

    private WebTarget webTarget()
    {
        Client client = ClientBuilder.newBuilder().build();
        return client.target(apiUrl).path("entities");
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
    private <T> JsonNode prepareData(T data, String entityType)
    {
        JsonNode jsonData = objectMapper.valueToTree(data);

        final ObjectNode entity = objectMapper.createObjectNode();
        entity.put(entityType, jsonData);

        final ObjectNode root = objectMapper.createObjectNode();
        root.put("entity", entity);

        return root;
    }

    private JsonNode handleResponse(Response response)
    {
        handleErrorResponses(response);

        if(statusesWithEntity.contains(response.getStatus()))
        {
            try
            {
                return objectMapper.readTree(response.readEntity(String.class));
            }
            catch(Exception checkedException)
            {
                Throwables.propagate(checkedException);
                return null; /*unreachable*/
            }
        }

        else if(statusWithoutEntity.contains(response.getStatus()))
        {
            return null;
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
