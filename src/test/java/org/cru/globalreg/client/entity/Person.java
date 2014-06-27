package org.cru.globalreg.client.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

/**
 * Created by ryancarlson on 2/25/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person
{

	private UUID id;
    private String firstName;
    private String lastName;
    private String campus;
	private UUID clientIntegrationId;

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getCampus()
    {
        return campus;
    }

    public void setCampus(String campus)
    {
        this.campus = campus;
    }

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public UUID getClientIntegrationId()
	{
		return clientIntegrationId;
	}

	public void setClientIntegrationId(UUID clientIntegrationId)
	{
		this.clientIntegrationId = clientIntegrationId;
	}
}
