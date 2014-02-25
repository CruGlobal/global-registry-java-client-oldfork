package org.cru.globalreg.client;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.cru.globalreg.client.impl.EntityEndpointsImpl;
import org.cru.globalreg.client.impl.EntitySearchResponse;
import org.cru.globalreg.jackson.GlobalRegistryApiNamingStrategy;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by ryancarlson on 2/23/14.
 */

public class EntityEndpointFunctionalTest
{
    static final String ACCESS_TOKEN = "zATBSYVCS_PCEnKFq-OmuaLyECW5ULQUUp9zFmDDgr8";

    private EntityEndpoints getApi()
    {
        final EntityEndpointsImpl api = new EntityEndpointsImpl();
        api.setAccessToken(ACCESS_TOKEN);
        return api;
    }

    @Test
    public void testGetEndpoint()
    {
        EntityEndpoints entityApi = this.getApi();

        Person entity = entityApi.get(Person.class, 20992, "person");

        Assert.assertNotNull(entity);

        Assert.assertEquals(entity.getPerson().getFirstName(), "Michele");
    }

    @Test
    public void testSearchEndpoint()
    {
        EntityEndpoints entityApi = this.getApi();

        EntitySearchResponse<PersonData> response = entityApi.search(PersonData.class, "person", new Filter("filters[first_name]", "Michele"));

        Assert.assertNotNull(response);

        Assert.assertEquals(response.getResults().get(0).getFirstName(), "Michele");
    }

    @Test
    public void testPostEndpoint() throws IOException
    {
        EntityEndpoints entityApi = this.getApi();

        Person testData = getTestJson();

        Person postResponseData = entityApi.create(testData, "person");

        try
        {
            Assert.assertNotNull(postResponseData);

            Assert.assertEquals(postResponseData.getPerson().getFirstName(), "Ryan");
            Assert.assertEquals(postResponseData.getPerson().getLastName(), "TestUser Carlson");
            Assert.assertEquals(postResponseData.getPerson().getCampus(), "Ohio University");
            Assert.assertNotNull(postResponseData.getPerson().getId());
        }
        finally
        {
            entityApi.delete(postResponseData.getPerson().getId(), "person");
        }
    }

    @Test
    public void testPutEndpoint() throws IOException
    {
        EntityEndpoints entityApi = this.getApi();

        /*get some JSON that we'll POST to ensure it's there, if it already exists,
        the API will just updated it, thankfully not creating a dupe*/
        Person testData = getTestJson();

        Person postResponseData = entityApi.create(testData, "person");

        try
        {
            Integer currentId = postResponseData.getPerson().getId();

            /*get the same JSON, but provide a new campus name*/
            Person updatedTestData = getTestJson();
            updatedTestData.getPerson().setCampus("Bowling Green");
            updatedTestData.getPerson().setId(currentId);
            /*execute the update*/

            Person putResponseData = entityApi.update(updatedTestData, currentId, "person");

            Assert.assertNotNull(putResponseData);

            Assert.assertEquals(putResponseData.getPerson().getFirstName(), "Ryan");
            Assert.assertEquals(putResponseData.getPerson().getLastName(), "TestUser Carlson");
            Assert.assertEquals(putResponseData.getPerson().getCampus(), "Bowling Green");
            Assert.assertEquals(putResponseData.getPerson().getId(), currentId);
        }
        finally
        {
            entityApi.delete(postResponseData.getPerson().getId(), "person");
        }

    }

    @Test
    public void testDeleteEndpoint()
    {
        /*do nothing... this endpoint is implied to work
        if the tests above don't fail while doing clean-up
        */
    }


    private Person getTestJson() throws IOException
    {
        JsonNode jsonNode = new ObjectMapper().readTree(Thread.currentThread()
                .getContextClassLoader()
                .getResource("testPost.json"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(new GlobalRegistryApiNamingStrategy());

        return mapper.readValue(jsonNode, Person.class);
    }
}
