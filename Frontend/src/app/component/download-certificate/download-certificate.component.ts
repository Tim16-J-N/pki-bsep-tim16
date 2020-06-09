import { HttpErrorResponse } from '@angular/common/http';
import { CertAccessInfo } from './../../model/cert.acces.info';
import { CertificateService } from './../../service/certificate.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ToastrService } from 'ngx-toastr';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Component, OnInit, Inject } from '@angular/core';

@Component({
  selector: 'app-download-certificate',
  templateUrl: './download-certificate.component.html',
  styleUrls: ['./download-certificate.component.css']
})
export class DownloadCertificateComponent implements OnInit {

  downloadForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private certificateService: CertificateService,
    private toastr: ToastrService,
    public dialog: MatDialogRef<DownloadCertificateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit() {
    this.downloadForm = this.formBuilder.group({
      rootKeyStorePass: new FormControl(null, Validators.required),
      rootCertAlias: new FormControl(null, Validators.required),
      rootCertKeyPass: new FormControl(null, Validators.required),
    });
  }

  download() {
    let certAccessInfo = new CertAccessInfo(this.data.certRole, this.data.keyStorePass, this.data.alias);
    this.certificateService.download(certAccessInfo, this.downloadForm.value.rootKeyStorePass, this.downloadForm.value.rootCertAlias, this.downloadForm.value.rootCertKeyPass).subscribe(
      () => {
        this.toastr.success('Success!', 'Download certificate');
      },
      (httpErrorResponse: HttpErrorResponse) => {
        this.toastr.error(httpErrorResponse.error.message, 'Download certificate');
      }
    );
  }

}
