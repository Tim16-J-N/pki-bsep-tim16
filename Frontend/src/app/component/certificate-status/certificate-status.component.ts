import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { OCSPService } from './../../service/ocsp.service';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CertificateItem } from './../../model/certificate.item';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Component, OnInit, Inject } from '@angular/core';

@Component({
  selector: 'app-certificate-status',
  templateUrl: './certificate-status.component.html',
  styleUrls: ['./certificate-status.component.css']
})
export class CertificateStatusComponent implements OnInit {

  cert: CertificateItem;
  status: String;
  revokeCertificateForm: FormGroup;

  constructor(
    private toastr: ToastrService,
    private formBuilder: FormBuilder,
    public dialog: MatDialogRef<CertificateStatusComponent>,
    private ocspService: OCSPService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit() {
    this.checkStatus();

    this.revokeCertificateForm = this.formBuilder.group({
      rootKeyStorePass: new FormControl(null, Validators.required),
      intermediateKeyStorePass: new FormControl(null, Validators.required),
      endEntityKeyStorePass: new FormControl(null, Validators.required),
    });
  }

  checkStatus() {
    this.ocspService.checkStatus(this.data.cert.certificateID).subscribe(
      (response: String) => {
        this.status = response;
      },
      (httpErrorResponse: HttpErrorResponse) => {
        this.toastr.error(httpErrorResponse.error.message, 'Check certificate status');
      }
    );
  }

  revoke() {
    this.ocspService.revoke(this.data.cert.certificateID.serialNumber, this.data.cert.alias, this.data.certRole, this.revokeCertificateForm.value.rootKeyStorePass,
      this.revokeCertificateForm.value.intermediateKeyStorePass, this.revokeCertificateForm.value.endEntityKeyStorePass).subscribe(
        () => {
          this.toastr.success('Certificate is successfully revoked.', 'Revoke certificate');
          this.dialog.close();
        }, (httpErrorResponse: HttpErrorResponse) => {
          this.toastr.error(httpErrorResponse.error.message, 'Revoke certificate');
        }
      );
  }

}
