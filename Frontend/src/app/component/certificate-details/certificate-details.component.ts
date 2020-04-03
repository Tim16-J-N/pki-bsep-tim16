import { Certificate } from './../../model/certificate';
import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-certificate-details',
  templateUrl: './certificate-details.component.html',
  styleUrls: ['./certificate-details.component.css']
})
export class CertificateDetailsComponent implements OnInit {



  constructor(
    @Inject(MAT_DIALOG_DATA) public selectedCert: Certificate
  ) { }

  getSubject() {
    return this.selectedCert.subject;
  }

  getIssuer() {
    return this.selectedCert.issuer;
  }

  isCA() {
    return this.selectedCert.subjectIsCa ? "Yes" : "No";
  }

  hasKeyUsage() {
    return this.selectedCert.keyUsage != null
  }

  hasExtendedKeyUsage() {
    return this.selectedCert.extendedKeyUsage != null
  }

  getKeyUsage() {
    return this.selectedCert.keyUsage
  }

  getExtendedKeyUsage() {
    return this.selectedCert.extendedKeyUsage
  }

  ngOnInit() {
  }

}
