package org.cru.globalreg.client;

import org.codehaus.jackson.JsonNode;
import org.cru.globalreg.client.impl.EntityEndpointsImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by ryancarlson on 2/23/14.
 */

public class EntityEndpointFunctionalTest
{

    @Test
    public void testGetEndpoint()
    {
        EntityEndpoints entityApi = new EntityEndpointsImpl();

        JsonNode json = entityApi.get("20992", "person", "");

        Assert.assertNotNull(json);

        Assert.assertEquals(json.path("person").path("first_name").getTextValue(), "Michele");
    }
}
