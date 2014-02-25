package org.cru.globalreg.client.entitytypes;

import com.google.common.collect.Sets;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.cru.globalreg.client.entity.EntityClass;
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
    private String apiUrl = new String("https://api.leadingwithinformation.com/entity_types");
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
                .queryParam("access_token", accessToken)
                .request()
                .get();

        try
        {
            JsonNode jsonResults = objectMapper.readTree(response.readEntity(String.class));
            return objectMapper.readValue(jsonResults.path("entity_types"), new TypeReference<List<EntityType>>(){});
        }
        catch(Exception e)
        {
            throw new WebApplicationException(e, 500);
        }
    }

    @Override
    public EntityType create(EntityType newEntityType)
    {
        Response response = webTarget()
                .queryParam("access_token", accessToken)
                .request()
                .post(Entity.json(newEntityType));

        try
        {
            JsonNode jsonResults = objectMapper.readTree(response.readEntity(String.class));
            return objectMapper.readValue(jsonResults, EntityType.class);
        }
        catch(Exception e)
        {
            throw new WebApplicationException(e, 500);
        }
    }

    private <T> T handleResponse(Response response, EntityClass<T> entityClass)
    {
        handleErrorResponses(response);

        if(statusesWithEntity.contains(response.getStatus()))
        {
            try
            {
                JsonNode data = objectMapper.readTree(response.readEntity(String.class));
                return objectMapper.treeToValue(data, entityClass.getType());
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
}
