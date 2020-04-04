import { CertificateDetailsComponent } from './../certificate-details/certificate-details.component';
import { ToastrService } from 'ngx-toastr';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Certificate } from './../../model/certificate';
import { CertificateService } from './../../service/certificate.service';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ChooseTemplateComponent } from '../choose-template/choose-template.component';
import { MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-list-certificates',
  templateUrl: './list-certificates.component.html',
  styleUrls: ['./list-certificates.component.css']
})
export class ListCertificatesComponent implements OnInit {

  keyStoreForm: FormGroup;
  displayedColumns: string[] = ['serialNumber', 'subjectCN', 'issuerCN', 'validFrom', 'validTo', 'buttons'];
  certificatesDataSource: MatTableDataSource<Certificate>;

  constructor(
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
    private certificateService: CertificateService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.keyStoreForm = this.formBuilder.group({
      certRole: new FormControl(null, Validators.required),
      keyStorePassword: new FormControl(null, Validators.required)
    });
  }

  fetchCertificates() {
    this.certificateService.getCertificates(this.keyStoreForm.value.certRole, this.keyStoreForm.value.keyStorePassword).subscribe(
      (data: Certificate[]) => {
        this.certificatesDataSource = new MatTableDataSource(data)
      },
      () => {
        this.toastr.error('Wrong password. Please try again.', 'Show certificates');
      });

    // const subject = new Entity("USER", "Perica", "pera@mail.com", null, "Firma doo", "RS", "Peric", "Petar", null, null, 1);
    // const issuer = new Entity("SOFTWARE", "izdavac.com", null, "Izdavaci sertifikata", "Izdavac doo", "RS", null, null, "Novi Sad", "Vojvodina", 2);
    // const keyUsage = new KeyUsage(true, true, false, true, false, true, true, false, false);
    // const extKeyUsg = new ExtendedKeyUsage(true, false, false, true, false, false);
    // const cert = new Certificate(subject, issuer, "7/5/2020", "7/30/2021", true, true, false, keyUsage, extKeyUsg, 156489);
    // this.certificatesDataSource = new MatTableDataSource([cert]);
  }

  viewDetails(cert: Certificate) {
    this.dialog.open(CertificateDetailsComponent, { data: cert });
  }

  download(cert: Certificate) {

  }

  revoke(cert: Certificate) {

  }

  openTemplatesDialog() {
    this.dialog.open(ChooseTemplateComponent);
  }


}
