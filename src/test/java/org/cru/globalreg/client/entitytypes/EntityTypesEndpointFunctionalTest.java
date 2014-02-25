package org.cru.globalreg.client.entitytypes;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class EntityTypesEndpointFunctionalTest
{

    private EntityTypeEndpoints getApi()
    {
        final EntityTypeEndpointsImpl api = new EntityTypeEndpointsImpl();
        api.setAccessToken("");
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
        /*without a delete function, not sure i want to test this in production*/
//        EntityTypeEndpoints entityTypesApi = this.getApi();
//
//        EntityType createdEntityTypeResult = entityTypesApi.create(null);
//
//        Assert.assertNotNull(createdEntityTypeResult);
    }

}
