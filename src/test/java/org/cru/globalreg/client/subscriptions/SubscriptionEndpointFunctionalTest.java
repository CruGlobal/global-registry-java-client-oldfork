package org.cru.globalreg.client.subscriptions;

import org.cru.globalreg.client.entity.EntityEndpoints;
import org.cru.globalreg.client.entity.EntityEndpointsImpl;
import org.testng.annotations.Test;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class SubscriptionEndpointFunctionalTest
{
    static final String ACCESS_TOKEN = "";

    private SubscriptionEndpoints getApi()
    {
        final SubscriptionEndpointsImpl api = new SubscriptionEndpointsImpl();
        api.setAccessToken(ACCESS_TOKEN);
        return api;
    }

    @Test
    public void testSubscriptionsPost()
    {
        Subscription subscription = new Subscription();

        subscription.setEndpoint("http://dev.formvent.org");
        subscription.setEntityTypeId(293);

        getApi().subscribe(subscription);
    }
}
