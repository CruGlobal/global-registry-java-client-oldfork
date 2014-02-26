package org.cru.globalreg.client;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by ryancarlson on 2/25/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person
{

    private String firstName;
    private String lastName;
    private String campus;
    private Integer id;

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}
