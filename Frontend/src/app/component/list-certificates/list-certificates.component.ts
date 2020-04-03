import { CertificateService } from './../../service/certificate.service';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ChooseTemplateComponent } from '../choose-template/choose-template.component';

@Component({
  selector: 'app-list-certificates',
  templateUrl: './list-certificates.component.html',
  styleUrls: ['./list-certificates.component.css']
})
export class ListCertificatesComponent implements OnInit {

  constructor(public dialog: MatDialog, private certificateService: CertificateService) { }

  ngOnInit() {
  }

  openTemplatesDialog() {
    this.dialog.open(ChooseTemplateComponent);
  }

}
