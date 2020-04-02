package ftn.pkibseptim16.model;
import ftn.pkibseptim16.enumeration.EntityType;

import javax.persistence.*;

@javax.persistence.Entity
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntityType type;

    @Column(columnDefinition = "VARCHAR(64)", nullable = false,unique = true)
    private String commonName;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "VARCHAR(64)")
    private String organizationUnitName;

    @Column(columnDefinition = "VARCHAR(64)", nullable = false)
    private String organization;

    @Column(columnDefinition = "VARCHAR(2)", nullable = false)
    private String countryCode;

    @Column
    private String surname;

    @Column
    private String givename;

    @Column(columnDefinition = "VARCHAR(64)")
    private String localityName;

    @Column(columnDefinition = "VARCHAR(64)")
    private String state;

    public Entity(){

    }
    public Entity(EntityType type, String commonName, String email,
                  String organizationUnitName, String organization, String countryCode, String surname, String givename, String localityName, String state) {
        this.type = type;
        this.commonName = commonName;
        this.countryCode = countryCode;
        this.organization = organization;
        if(type.toString() == "USER"){
            this.email = email;
            this.surname = surname;
            this.givename = givename;
            this.organizationUnitName = "";
            this.localityName = "";
            this.state = "";
        }else {
            this.organizationUnitName = organizationUnitName;
            this.localityName = localityName;
            this.state = state;
            this.email = "";
            this.surname = "";
            this.givename = "";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
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
