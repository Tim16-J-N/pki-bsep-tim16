import { ToastrService } from 'ngx-toastr';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Entity } from './../../model/entity';
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
    //   this.certificateService.getCertificates(this.keyStoreForm.value.certRole, this.keyStoreForm.value.keyStorePassword).subscribe(
    //     (data: Certificate[]) => {
    //       this.certificatesDataSource = new MatTableDataSource(data)
    //     },
    //     () => {
    //       this.toastr.error('Wrong password. Please try again.', 'Show certificates');
    //     });
    // }

    const subject = new Entity("user", "mera", "oaoaj", "sss", "ssds", "RS", "sss", "ddd", "sss", "sss", 1);
    const issuer = new Entity("issuer", "issuer", "fdhdh", "sggds", "sdgsd", "RS", "gfhgf", "ddd", "sss", "sss", 2);
    const cert = new Certificate(subject, issuer, "7/5/2020", "7/30/2021", true, true, true, null, null, 156489);
    this.certificatesDataSource = new MatTableDataSource([cert, cert, cert]);
  }

  viewDetails(cert: Certificate) {

  }

  download(cert: Certificate) {

  }

  revoke(cert: Certificate) {

  }

  openTemplatesDialog() {
    this.dialog.open(ChooseTemplateComponent);
  }

}
