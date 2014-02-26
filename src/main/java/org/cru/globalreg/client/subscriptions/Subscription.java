package org.cru.globalreg.client.subscriptions;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class Subscription
{
    private String endpoint;
    private Integer entityTypeId;

    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public Integer getEntityTypeId()
    {
        return entityTypeId;
    }

    public void setEntityTypeId(Integer entityTypeId)
    {
        this.entityTypeId = entityTypeId;
    }
}
