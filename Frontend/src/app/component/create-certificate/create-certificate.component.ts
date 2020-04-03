import { Template } from './../../model/template';
import { CreateCertificate } from './../../model/create.certificate';
import { CertificateService } from './../../service/certificate.service';
import { Certificate } from './../../model/certificate';
import { ExtendedKeyUsage } from './../../model/extended.key.usage';
import { KeyUsage } from './../../model/key.usage';
import { SubjectService } from './../../service/subject.service';
import { AddSubjectComponent } from './../add-subject/add-subject.component';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators, ValidatorFn } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Entity } from 'src/app/model/entity';
import { MatDialog } from '@angular/material/dialog';
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

const KeyStoragePasswordValidator: ValidatorFn = (fg: FormGroup) => {
  const from = fg.get('rootKeyStoragePassword').value;
  const to = fg.get('intermediateKeyStoragePassword').value;
  if (from || to) {
    return null;
  }
  return { validKeyStoragePasword: true };
};

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.css']
})
export class CreateCertificateComponent implements OnInit {
  createCertificateFormSubject: FormGroup;
  createCertificateFormIssuer: FormGroup;
  createCertificateFormOtherData: FormGroup;
  createCertificateInfoAboutKeyStorage: FormGroup;
  createCertificateKeyStoragePasswords: FormGroup;

  minDate = new Date();
  subjects: Entity[] = [];
  //ovo ces morati da promenis posto on fakticki bira sertifikate a ne issuer-a. Vidi kako i sta ces slati sve ovde na frontend
  issuers: Entity[] = [];
  createdNewSubject: Subscription;
  selectedTemplate: Template;

  constructor(private toastr: ToastrService, private formBuilder: FormBuilder, public dialog: MatDialog, private subjectService: SubjectService,
    private certificateService: CertificateService) { }

  ngOnInit() {
    this.createCertificateFormSubject = this.formBuilder.group({
      selectedSubject: new FormControl(null, Validators.required),
    });

    this.createCertificateKeyStoragePasswords = this.formBuilder.group({
      rootKeyStoragePassword: new FormControl(null),
      intermediateKeyStoragePassword: new FormControl(null),
    }, {
      validator: [KeyStoragePasswordValidator]
    });

    this.createCertificateFormIssuer = this.formBuilder.group({
      selectedIssuer: new FormControl(null, Validators.required),
    });

    this.functionForCreatingFormCertificateFormOtherData();
    this.functionForCreatingFormCertificateInfoAboutKeyStorage();

    this.getSubjects();

    this.createdNewSubject = this.subjectService.createSuccessEmitter.subscribe(
      () => {
        this.getSubjects();
      }
    );

    if (JSON.parse(localStorage.getItem('selectedTemplate'))) {
      this.selectedTemplate = JSON.parse(localStorage.getItem('selectedTemplate'));
      localStorage.removeItem('selectedTemplate');
      this.setExtensions();
    }

  }


  getSubjects(): void {
    this.subjectService.getAll().subscribe((subjects: Entity[]) => {
      this.subjects = subjects;
    })
  }

  functionForCreatingFormCertificateInfoAboutKeyStorage() {
    this.createCertificateInfoAboutKeyStorage = this.formBuilder.group({
      alias: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required),
      privateKeyPassword: new FormControl(null, Validators.required),
<<<<<<< HEAD
      issuerKeyStorePassword: new FormControl(null, Validators.required),
=======
>>>>>>> feature/listCertificates
      issuerPrivateKeyPassword: new FormControl(null, Validators.required)
    });
  }

  functionForCreatingFormCertificateFormOtherData() {
    this.createCertificateFormOtherData = this.formBuilder.group({
      validFrom: new FormControl(null, Validators.required),
      validTo: new FormControl(null, Validators.required),
      authorityKeyIdentifier: new FormControl(false, Validators.required),
      subjectKeyIdentifier: new FormControl(false, Validators.required),
      subjectIsCa: new FormControl(false, Validators.required),
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

  getSelectedIssuer() {
    return this.createCertificateFormIssuer.get('selectedIssuer').value;
  }

  createCertificate() {
    if (this.createCertificateFormSubject.invalid) {
      this.toastr.error("Please choose subject", 'Create certificate');
      return;
    }
    if (this.createCertificateFormIssuer.invalid) {
      this.toastr.error("Please choose issuer", 'Create certificate');
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

    const certificate = new Certificate(this.createCertificateFormSubject.value.selectedSubject, this.createCertificateFormIssuer.value.selectedIssuer,
      validFrom, validTo, this.createCertificateFormOtherData.value.authorityKeyIdentifier, this.createCertificateFormOtherData.value.subjectKeyIdentifier,
      this.createCertificateFormOtherData.value.subjectIsCa, keyUsage, extendedKeyUsage);
    const createCertificate = new CreateCertificate(certificate, this.createCertificateInfoAboutKeyStorage.value.alias,
      this.createCertificateInfoAboutKeyStorage.value.password, this.createCertificateInfoAboutKeyStorage.value.privateKeyPassword,
      this.createCertificateInfoAboutKeyStorage.value.issuerPrivateKeyPassword, this.createCertificateInfoAboutKeyStorage.value.issuerKeyStorePassword);

    this.certificateService.add(createCertificate).subscribe(
      () => {
        this.createCertificateFormOtherData.reset();
        this.createCertificateFormSubject.reset();
        this.createCertificateFormIssuer.reset();
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
        'subjectIsCa': this.selectedTemplate.CA,
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

  getCACertificates() {
    this.certificateService.getCACertificates(this.createCertificateKeyStoragePasswords.value.rootKeyStoragePassword,
      this.createCertificateKeyStoragePasswords.value.intermediateKeyStoragePassword).
      subscribe((subjects: Entity[]) => {
        this.subjects = subjects;
      })
  }
}
