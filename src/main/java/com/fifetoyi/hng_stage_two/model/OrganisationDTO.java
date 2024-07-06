package com.fifetoyi.hng_stage_two.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class OrganisationDTO {

    @Size(max = 255)
    @OrganisationOrgIdValid
    private String orgId;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(final String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

}
