package org.cru.globalreg.client;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by ryancarlson on 2/25/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person
{
    PersonData person;

    public PersonData getPerson()
    {
        return person;
    }

    public void setPerson(PersonData person)
    {
        this.person = person;
    }
}
