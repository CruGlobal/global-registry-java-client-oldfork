package org.cru.globalreg.client.entitytypes;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntityTypesEndpointFunctionalTest
{

    private final String ACCESS_TOKEN = "";

    private EntityTypeEndpoints getApi()
    {
        final EntityTypeEndpointsImpl api = new EntityTypeEndpointsImpl();
        api.setAccessToken(ACCESS_TOKEN);
        return api;
    }

    @Test
    public void testGetAllEndpoint()
    {
        EntityTypeEndpoints entityTypesApi = this.getApi();

        List<EntityType> entityTypes = entityTypesApi.getAll();

        Assert.assertNotNull(entityTypes);
        Assert.assertFalse(entityTypes.isEmpty());
    }

    @Test
    public void testCreateEndpoint()
    {
        EntityTypeEndpoints entityTypesApi = this.getApi();

        EntityType createdEntityTypeResult = entityTypesApi.create(testEntityType());

        Assert.assertNotNull(createdEntityTypeResult);
    }

    private EntityType  testEntityType()
    {
        EntityType entityType;

        entityType = new EntityType();
        entityType.setName("favorite_food");
        entityType.setFieldType("string");
        entityType.setParentId(UUID.fromString("d0d86c43-311e-476f-81a5-9877aae4b4d8"));

        return entityType;
    }
}
