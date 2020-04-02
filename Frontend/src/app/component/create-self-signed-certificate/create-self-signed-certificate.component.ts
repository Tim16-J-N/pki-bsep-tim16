import { AddSubjectComponent } from './../add-subject/add-subject.component';
import { ExtendedKeyUsage } from './../../model/extended.key.usage';
import { KeyUsage } from './../../model/key.usage';
import { CreateCertificate } from './../../model/create.certificate';
import { Certificate } from './../../model/certificate';
import { CertificateService } from './../../service/certificate.service';
import { SubjectService } from './../../service/subject.service';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Template } from './../../model/template';
import { Entity } from './../../model/entity';
import { ValidatorFn, FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { formatDate } from '@angular/common';

const TimeValidator: ValidatorFn = (fg: FormGroup) => {
  const from = fg.get('validFrom').value;
  const to = fg.get('validTo').value;
  if (!from || !to) {
    return null;
  }
  return from !== null && to !== null && from < to ? null : { validError: true };
};

@Component({
  selector: 'app-create-self-signed-certificate',
  templateUrl: './create-self-signed-certificate.component.html',
  styleUrls: ['./create-self-signed-certificate.component.css']
})
export class CreateSelfSignedCertificateComponent implements OnInit {
  createCertificateFormSubject: FormGroup;
  createCertificateFormOtherData: FormGroup;
  createCertificateInfoAboutKeyStorage: FormGroup;
  minDate = new Date();
  subjects: Entity[] = [];

  createdNewSubject: Subscription;
  selectedTemplate: Template;

  constructor(private toastr: ToastrService, private formBuilder: FormBuilder, public dialog: MatDialog, private subjectService: SubjectService,
    private certificateService: CertificateService) { }

  ngOnInit() {
    this.createCertificateFormSubject = this.formBuilder.group({
      selectedSubject: new FormControl(null, Validators.required),
    });

    this.functionForCreatingFormCertificateFormOtherData();
    this.functionForCreatingFormCertificateInfoAboutKeyStorage();

    this.createdNewSubject = this.subjectService.createSuccessEmitter.subscribe(
      () => {
        //getSubjects();
      }
    );

    if (JSON.parse(localStorage.getItem('selectedTemplate'))) {
      this.selectedTemplate = JSON.parse(localStorage.getItem('selectedTemplate'));
      localStorage.removeItem('selectedTemplate');
      this.setExtensions();
    }

  }

  functionForCreatingFormCertificateInfoAboutKeyStorage() {
    this.createCertificateInfoAboutKeyStorage = this.formBuilder.group({
      alias: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required)
    });
    this.subjects.push(new Entity("user", "mera", "oaoaj", "sss", "ssds", "s", "sss", "ddd", "sss", "sss"));
  }

  functionForCreatingFormCertificateFormOtherData() {
    this.createCertificateFormOtherData = this.formBuilder.group({
      validFrom: new FormControl(null, Validators.required),
      validTo: new FormControl(null, Validators.required),
      authorityKeyIdentifier: new FormControl(false, Validators.required),
      subjectKeyIdentifier: new FormControl(false, Validators.required),
      keyUsage: this.formBuilder.group({
        certificateSigning: new FormControl(false),
        crlSign: new FormControl(false),
        dataEncipherment: new FormControl(false),
        decipherOnly: new FormControl(false),
        digitalSignature: new FormControl(false),
        enchiperOnly: new FormControl(false),
        keyAgreement: new FormControl(false),
        keyEncipherment: new FormControl(false),
        nonRepudiation: new FormControl(false),
      }),
      extentendedKeyUsage: this.formBuilder.group({
        serverAuth: new FormControl(false),
        clientAuth: new FormControl(false),
        codeSigning: new FormControl(false),
        emailProtection: new FormControl(false),
        timeStamping: new FormControl(false),
        ocspSigning: new FormControl(false)
      }),
    }, {
      validator: [TimeValidator]
    });
  }

  getSelectedSubject() {
    return this.createCertificateFormSubject.get('selectedSubject').value;
  }

  createCertificate() {
    if (this.createCertificateFormSubject.invalid) {
      this.toastr.error("Please choose subject", 'Create certificate');
      return;
    }
    if (this.createCertificateFormOtherData.invalid) {
      this.toastr.error("Please set valid period", 'Create certificate');
      return;
    }

    const keyUsage = this.createKeyUsage();
    const extendedKeyUsage = this.createExtendedKeyUsage();

    const validFrom = formatDate(this.createCertificateFormOtherData.value.validFrom, 'yyyy-MM-dd', 'en-US')
    const validTo = formatDate(this.createCertificateFormOtherData.value.validTo, 'yyyy-MM-dd', 'en-US')

    const certificate = new Certificate(this.createCertificateFormSubject.value.selectedSubject, this.createCertificateFormSubject.value.selectedSubject,
      validFrom, validTo, this.createCertificateFormOtherData.value.authorityKeyIdentifier, this.createCertificateFormOtherData.value.subjectKeyIdentifier,
      true, keyUsage, extendedKeyUsage);
    const createCertificate = new CreateCertificate(certificate, this.createCertificateInfoAboutKeyStorage.value.alias,
      this.createCertificateInfoAboutKeyStorage.value.password);

    this.certificateService.addSelfSigned(createCertificate).subscribe(
      () => {
        this.createCertificateFormOtherData.reset();
        this.createCertificateFormSubject.reset();
        this.createCertificateInfoAboutKeyStorage.reset();
        this.toastr.success('Successfully created a new certificate.', 'Create certificate');
      },
      () => {
        this.toastr.error('Error ', 'Create certificate');
      }
    );
  }

  createKeyUsage(): KeyUsage {
    return new KeyUsage(this.createCertificateFormOtherData.value.keyUsage.certificateSigning,
      this.createCertificateFormOtherData.value.keyUsage.crlSign,
      this.createCertificateFormOtherData.value.keyUsage.dataEncipherment,
      this.createCertificateFormOtherData.value.keyUsage.decipherOnly,
      this.createCertificateFormOtherData.value.keyUsage.digitalSignature,
      this.createCertificateFormOtherData.value.keyUsage.enchiperOnly,
      this.createCertificateFormOtherData.value.keyUsage.keyAgreement,
      this.createCertificateFormOtherData.value.keyUsage.keyEncipherment,
      this.createCertificateFormOtherData.value.keyUsage.nonRepudiation
    )
  }

  createExtendedKeyUsage(): ExtendedKeyUsage {
    return new ExtendedKeyUsage(
      this.createCertificateFormOtherData.value.extentendedKeyUsage.serverAuth,
      this.createCertificateFormOtherData.value.extentendedKeyUsage.clientAuth,
      this.createCertificateFormOtherData.value.extentendedKeyUsage.codeSigning,
      this.createCertificateFormOtherData.value.extentendedKeyUsage.emailProtection,
      this.createCertificateFormOtherData.value.extentendedKeyUsage.timeStamping,
      this.createCertificateFormOtherData.value.extentendedKeyUsage.ocspSigning
    )
  }

  setExtensions() {
    this.createCertificateFormOtherData.patchValue(
      {
        'authorityKeyIdentifier': this.selectedTemplate.authorityKeyId,
        'subjectKeyIdentifier': this.selectedTemplate.subjectKeyId,
        'keyUsage': {
          'digitalSignature': this.selectedTemplate.digitalSigniture,
          'keyEncipherment': this.selectedTemplate.keyEncipherment,
          'certificateSigning': this.selectedTemplate.certSigning,
          'crlSign': this.selectedTemplate.CRLSign,
        },
        'extentendedKeyUsage': {
          'serverAuth': this.selectedTemplate.TLSWebServerAuth,
          'clientAuth': this.selectedTemplate.TLSWebClientAuth,
          'codeSigning': this.selectedTemplate.codeSigning
        }
      }
    );
  }

  openAddSubject() {
    this.dialog.open(AddSubjectComponent);
  }

}
