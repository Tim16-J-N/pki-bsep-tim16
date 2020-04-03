package ftn.pkibseptim16.dto;

import ftn.pkibseptim16.enumeration.EntityType;
import ftn.pkibseptim16.model.Entity;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EntityDTO {
    private Long id;

    @NotEmpty(message = "Type is empty.")
    private String type;

    @NotEmpty(message = "Common name is empty.")
    private String commonName;

    @Email
    private String email;

    private String organizationUnitName;

    @NotEmpty(message = "Organization is empty.")
    private String organization;

    @NotEmpty(message = "Country code is empty.")
    @Size(message = "Max size and min size for country code is 2.", max = 2,min=2)
    private String countryCode;

    private String surname;

    private String givename;

    private String localityName;

    private String state;

    public EntityDTO(){

    }
    public EntityDTO(@NotEmpty(message = "Type is empty.") String type, @NotEmpty(message = "Common name is empty.") String commonName,
                     @Email String email, String organizationUnitName, @NotEmpty(message = "Organization is empty.") String organization,
                     @NotEmpty(message = "Country code is empty.") @Size(message = "Max size and min size for country code is 2.", max = 2, min = 2) String countryCode,
                     String surname, String givename, String localityName, String state,Long id ) {
        this.type = type;
        this.commonName = commonName;
        this.email = email;
        this.organizationUnitName = organizationUnitName;
        this.organization = organization;
        this.countryCode = countryCode;
        this.surname = surname;
        this.givename = givename;
        this.localityName = localityName;
        this.state = state;
        this.id=id;
    }



    public EntityDTO(Entity entity){
        this(entity.getType().toString(),entity.getCommonName(),entity.getEmail(),entity.getOrganizationUnitName(),entity.getOrganization(),
                entity.getCountryCode(),entity.getSurname(),entity.getGivename(),entity.getLocalityName(),entity.getState(),entity.getId());
    }

    public EntityDTO(X500Name x500Name,Long id){

        this.commonName = x500Name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
        this.organization = x500Name.getRDNs(BCStyle.O)[0].getFirst().getValue().toString();
        this.countryCode = x500Name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString();
        RDN[] email = x500Name.getRDNs(BCStyle.EmailAddress);
        if(email != null && email.length != 0 ){
            this.email =  email[0].getFirst().getValue().toString();
            this.surname =x500Name.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue().toString();
            this.givename = x500Name.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue().toString();
            this.type = "USER";
        }else{
            this.type = "SOFTWARE";
            this.organizationUnitName = x500Name.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString();
            this.localityName = x500Name.getRDNs(BCStyle.L)[0].getFirst().getValue().toString();
            this.state = x500Name.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString();
        }
        this.id=id;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivename() {
        return givename;
    }

    public void setGivename(String givename) {
        this.givename = givename;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
