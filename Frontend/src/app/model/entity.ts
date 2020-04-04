export class Entity {
    id: number;
    type: string;
    commonName: string;
    email: string;
    organizationUnitName: string;
    organization: string;
    countryCode: string;
    surname: string;
    givename: string;
    localityName: string;
    state: string;
    constructor(type: string, commonName: string, email: string, organizationUnitName: string, organization: string, countryCode: string, surname: string,
        givename: string, localityName: string, state: string, id?: number) {
        this.type = type;
        this.commonName = commonName;
        this.email = email;
        this.organizationUnitName = organizationUnitName;
        this.organization = organization;
        this.countryCode = countryCode;
        this.surname = surname;
        this.id = id;
        this.givename = givename;
        this.localityName = localityName;
        this.state = state;
    }

}