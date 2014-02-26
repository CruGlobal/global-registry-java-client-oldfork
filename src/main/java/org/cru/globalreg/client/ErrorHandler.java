package org.cru.globalreg.client;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by ryancarlson on 2/25/14.
 */
public class ErrorHandler
{
    static public void handleErrorResponses(Response response)
    {
        if(response.getStatus() >= 400)
        {
            throw new WebApplicationException(response);
        }
    }
}
