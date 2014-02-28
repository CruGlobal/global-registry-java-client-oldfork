package org.cru.globalreg.client.entitytypes;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;
import org.cru.globalreg.jackson.GlobalRegistryApiNamingStrategy;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntityTypeEndpointsImpl implements EntityTypeEndpoints
{
    private String apiUrl = new String("http://gr.stage.uscm.org");
    private String accessToken;
    final private ObjectMapper objectMapper;

    final private static Set<Integer> statusesWithEntity  = Sets.newHashSet(200, 201);
    final private static Set<Integer> statusWithoutEntity = Sets.newHashSet(204);

    public final void setAccessToken(final String accessToken)
    {
        this.accessToken = accessToken;
    }

    public EntityTypeEndpointsImpl()
    {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(new GlobalRegistryApiNamingStrategy());
        this.objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<EntityType> getAll()
    {
        Response response = webTarget()
                .path("/entity_types")
                .queryParam("access_token", accessToken)
                .request()
                .get();

        handleErrorResponses(response);

        try
        {
            JsonNode jsonResults = objectMapper.readTree(response.readEntity(String.class));
            return objectMapper.readValue(jsonResults.path("entity_types"), new TypeReference<List<EntityType>>(){});
        }
        catch(Exception e)
        {
            Throwables.propagate(e);
            return null; /*unreachable*/
        }
    }

    @Override
    public EntityType create(EntityType newEntityType)
    {
        Response response = webTarget()
                .path("/entity_types")
                .queryParam("access_token", accessToken)
                .request()
                .post(Entity.json(prepareData(newEntityType,"entity_type")));

        handleErrorResponses(response);

        try
        {
            JsonNode jsonResults = objectMapper.readTree(response.readEntity(String.class));
            return objectMapper.readValue(jsonResults, EntityType.class);
        }
        catch(Exception e)
        {
            Throwables.propagate(e);
            return null; /*unreachable*/
        }
    }

    private WebTarget webTarget()
    {
        Client client = ClientBuilder.newBuilder().build();
        return client.target(apiUrl);
    }

    private void handleErrorResponses(Response response)
    {
        if(response.getStatus() >= 400)
        {
            throw new WebApplicationException(response);
        }
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
//
//        final ObjectNode entity = objectMapper.createObjectNode();
//        entity.put(entityType, jsonData);

        final ObjectNode root = objectMapper.createObjectNode();
        root.put("entity_type", jsonData);

        return root;
    }
}
