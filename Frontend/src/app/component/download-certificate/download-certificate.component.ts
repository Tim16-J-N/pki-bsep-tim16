import { browser } from 'protractor';
import { ToastrService } from 'ngx-toastr';
import { CertificateService } from './../../service/certificate.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material';
import { Component, OnInit, Inject } from '@angular/core';

@Component({
  selector: 'app-download-certificate',
  templateUrl: './download-certificate.component.html',
  styleUrls: ['./download-certificate.component.css']
})
export class DownloadCertificateComponent implements OnInit {

  downloadForm: FormGroup;

  constructor(
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
    private certificateService: CertificateService,
    private toastr: ToastrService,
    @Inject(MAT_DIALOG_DATA) public data
  ) { }

  ngOnInit() {
    this.downloadForm = this.formBuilder.group({
      alias: new FormControl(null, Validators.required)
    });
  }

  download() {
    this.certificateService.download(this.data.certRole, this.data.keyStorePassword, this.downloadForm.value.alias).subscribe(
      () => {
        this.toastr.success('Success!', 'Download certificate');
      },
      () => {
        this.toastr.error('Error while downloading.', 'Download certificate');
      }
    )
  }

}
