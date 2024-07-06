package com.fifetoyi.hng_stage_two.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class UserDTO {

    @Size(max = 255)
    private String userId;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @Size(max = 255)
    private String phone;

    private List<String> organisations;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public List<String> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(final List<String> organisations) {
        this.organisations = organisations;
    }

}
