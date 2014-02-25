package org.cru.globalreg.client;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.cru.globalreg.client.impl.EntityEndpointsImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by ryancarlson on 2/23/14.
 */

public class EntityEndpointFunctionalTest
{
    static final String ACCESS_TOKEN = "zATBSYVCS_PCEnKFq-OmuaLyECW5ULQUUp9zFmDDgr8";

    private EntityEndpoints getApi() {
        final EntityEndpointsImpl api = new EntityEndpointsImpl();
        api.setAccessToken(ACCESS_TOKEN);
        return api;
    }

    @Test
    public void testGetEndpoint()
    {
        EntityEndpoints entityApi = this.getApi();

        JsonNode json = entityApi.get(20992, "person");

        Assert.assertNotNull(json);

        Assert.assertEquals(json.path("person").path("first_name").getTextValue(), "Michele");
    }

    @Test
    public void testSearchEndpoint() {
        EntityEndpoints entityApi = this.getApi();

        JsonNode json = entityApi.search("person", new Filter("filters[first_name]", "Michele"));

        Assert.assertNotNull(json);

        Assert.assertEquals(json.path("entities").path(0).path("first_name").getTextValue(), "Michele");
    }

    @Test
    public void testPostEndpoint() throws IOException
    {
        EntityEndpoints entityApi = this.getApi();

        JsonNode json = getTestJson();

        JsonNode responseJson = entityApi.create(json, "person");

        try
        {
            Assert.assertNotNull(responseJson);

            //logging to the console in case an assertion fails and manual data clean up is needed
            System.out.println(responseJson.toString());

            Assert.assertEquals(responseJson.path("person").path("first_name").getTextValue(), "Ryan");
            Assert.assertEquals(responseJson.path("person").path("last_name").getTextValue(), "TestUser Carlson");
            Assert.assertEquals(responseJson.path("person").path("campus").getTextValue(), "Ohio University");
            Assert.assertNotNull(responseJson.path("person").path("id").getIntValue());
        }
        finally
        {
            entityApi.delete(responseJson.path("person").path("id").getIntValue(), "person");
        }
    }

    @Test
    public void testPutEndpoint() throws IOException
    {
        EntityEndpoints entityApi = this.getApi();

        /*get some JSON that we'll POST to ensure it's there, if it already exists,
        the API will just updated it, thankfully not creating a dupe*/
        JsonNode json = getTestJson();

        JsonNode responseJson = entityApi.create(json, "person");

        try
        {
            /*get the same JSON, but provide a new campus name*/
            JsonNode updateJson = getTestJson();
            ((ObjectNode)updateJson.path("entity").path("person")).put("campus", "Bowling Green");

            /*execute the update*/
            int currentId = responseJson.path("person").path("id").getIntValue();
            JsonNode updateResponseJson = entityApi.update(currentId, updateJson, "person");

            Assert.assertNotNull(updateResponseJson);

            //logging to the console in case an assertion fails and manual data clean up is needed
            System.out.println(updateResponseJson.toString());

            Assert.assertEquals(updateResponseJson.path("person").path("first_name").getTextValue(), "Ryan");
            Assert.assertEquals(updateResponseJson.path("person").path("last_name").getTextValue(), "TestUser Carlson");
            Assert.assertEquals(updateResponseJson.path("person").path("campus").getTextValue(), "Bowling Green");
            Assert.assertEquals(updateResponseJson.path("person").path("id").getIntValue(), currentId);
        }
        finally
        {
            entityApi.delete(responseJson.path("person").path("id").getIntValue(), "person");
        }

    }

    @Test
    public void testDeleteEndpoint()
    {
        /*do nothing... this endpoint is implied to work
        if the tests above don't fail while doing clean-up
        */
    }


    private JsonNode getTestJson() throws IOException
    {
        return new ObjectMapper().readTree(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("testPost.json"));
    }
}
