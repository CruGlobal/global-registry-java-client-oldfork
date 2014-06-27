package org.cru.globalreg.client.entity;

import java.io.IOException;
import java.util.UUID;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cru.globalreg.client.Filter;
import org.cru.globalreg.jackson.GlobalRegistryApiNamingStrategy;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by ryancarlson on 2/23/14.
 */

public class EntityEndpointFunctionalTest
{
	private final String ACCESS_TOKEN = "";

    private EntityEndpoints getApi()
    {
        final EntityEndpointsImpl api = new EntityEndpointsImpl();
        api.setAccessToken(ACCESS_TOKEN);
		api.setApiUrl("http://gr.stage.uscm.org");
        return api;
    }

    @Test
    public void testGetEndpoint()
    {
        EntityEndpoints entityApi = this.getApi();

        Person entity = entityApi.get(new EntityClass<Person>(Person.class), UUID.fromString("8a8a708e-fe2b-11e3-97a6-12725f8f377c"), "person");

        Assert.assertNotNull(entity);

        Assert.assertEquals(entity.getFirstName(), "Ryan");
    }

    @Test
    public void testSearchEndpoint()
    {
        EntityEndpoints entityApi = this.getApi();

        EntitySearchResponse<Person> response = entityApi.search(new EntityClass<Person>(Person.class), "person",
                new Filter().path("last_name").value("TestUser Carlson"));

        Assert.assertNotNull(response);

        Assert.assertEquals(response.get(0).getLastName(), "TestUser Carlson");
    }

    @Test
    public void testCreateEndpoint() throws IOException
    {
        EntityEndpoints entityApi = this.getApi();

        Person testData = getTestJson();

        Person postResponseData = entityApi.create(new EntityData<Person>(Person.class, testData), "person");

		Assert.assertNotNull(postResponseData);

		Assert.assertEquals(UUID.fromString("8a8a708e-fe2b-11e3-97a6-12725f8f377c"), postResponseData.getId());
    }

    @Test
    public void testUpdateEndpoint() throws IOException
    {
        EntityEndpoints entityApi = this.getApi();

        /*get some JSON that we'll POST to ensure it's there, if it already exists,
        the API will just updated it, thankfully not creating a dupe*/
        Person testData = getTestJson();

		Person postResponseData = entityApi
				.create(new EntityData<Person>(Person.class, testData), "person");

		UUID currentId = postResponseData.getId();

            /*get the same JSON, but provide a new campus name*/
		Person updatedTestData = getTestJson();
		updatedTestData.setCampus("Bowling Green");
		updatedTestData.setId(currentId);
            /*execute the update*/

		Person putResponseData = entityApi.update(
				new EntityData<Person>(Person.class, updatedTestData), currentId, "person");

		Assert.assertNotNull(putResponseData);

		Assert.assertEquals(UUID.fromString("8a8a708e-fe2b-11e3-97a6-12725f8f377c"), postResponseData.getId());
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

		return mapper.treeToValue(jsonNode.path("person"), Person.class);

    }
}
