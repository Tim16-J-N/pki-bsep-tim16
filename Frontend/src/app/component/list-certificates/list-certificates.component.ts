import { CertAccessInfo } from './../../model/cert.acces.info';
import { DownloadCertificateComponent } from './../download-certificate/download-certificate.component';
import { ValidationService } from './../../service/validation.service';
import { CertificateStatusComponent } from './../certificate-status/certificate-status.component';
import { CertificateItem } from './../../model/certificate.item';
import { HttpErrorResponse } from '@angular/common/http';
import { CertificateDetailsComponent } from './../certificate-details/certificate-details.component';
import { ToastrService } from 'ngx-toastr';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
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
  certificatesDataSource: MatTableDataSource<CertificateItem>;

  constructor(
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
    private certificateService: CertificateService,
    private validationService: ValidationService,
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
      (data: CertificateItem[]) => {
        this.certificatesDataSource = new MatTableDataSource(data)
        if (data.length == 0) {
          this.toastr.info('No certificates the in specified KeyStore.', 'Show certificates');
        }

      },
      (httpErrorResponse: HttpErrorResponse) => {
        const data: CertificateItem[] = []
        this.certificatesDataSource = new MatTableDataSource(data)
        this.toastr.error(httpErrorResponse.error.message, 'Show certificates');
      }
    );
  }

  viewDetails(cert: CertificateItem) {
    this.dialog.open(CertificateDetailsComponent, { data: cert });
  }

  download(cert: CertificateItem) {
    this.dialog.open(DownloadCertificateComponent,
      {
        data: {
          "certRole": this.keyStoreForm.value.certRole,
          "keyStorePass": this.keyStoreForm.value.keyStorePassword,
          "alias": cert.alias
        }
      });
  }

  checkStatus(cert: CertificateItem) {
    this.dialog.open(CertificateStatusComponent, {
      data:
      {
        "cert": cert,
        "certRole": this.keyStoreForm.value.certRole
      }
    });
  }

  checkValidity(cert: CertificateItem) {
    let certAccessInfo = new CertAccessInfo(this.keyStoreForm.value.certRole, this.keyStoreForm.value.keyStorePassword, cert.alias);
    this.validationService.checkValidity(certAccessInfo).subscribe(
      (response: String) => {
        if (response.toUpperCase() === "VALID") {
          this.toastr.success('This certificate is ' + response.toUpperCase() + '.', 'Certificate Validity');
        } else {
          this.toastr.warning('This certificate is ' + response.toUpperCase() + '.', 'Certificate Validity');
        }
      },
      (httpErrorResponse: HttpErrorResponse) => {
        this.toastr.error(httpErrorResponse.error.message, 'Certificate Validity');
      }
    );
  }

  openTemplatesDialog() {
    this.dialog.open(ChooseTemplateComponent);
  }

}
