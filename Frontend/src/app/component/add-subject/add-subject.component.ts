import { SubjectService } from './../../service/subject.service';
import { Entity } from 'src/app/model/entity';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators, ValidatorFn, FormBuilder } from '@angular/forms';

const ValidForm: ValidatorFn = (fg: FormGroup) => {
  const type = fg.get('type').value;

  if (type == "user") {
    if (!fg.get('email').value || !fg.get('surname').value || !fg.get('givename').value) {
      return { formIsInvalid: true };
    }
  } else {
    if (!fg.get('organizationUnitName').value || !fg.get('localityName').value || !fg.get('state').value) {
      return { formIsInvalid: true };
    }
  }

  return null;
};
@Component({
  selector: 'app-add-subject',
  templateUrl: './add-subject.component.html',
  styleUrls: ['./add-subject.component.css']
})
export class AddSubjectComponent implements OnInit {
  createSubjectForm: FormGroup;
  constructor(private toastr: ToastrService, private subjectService: SubjectService,
    private dialogRef: MatDialogRef<AddSubjectComponent>, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.createSubjectForm = this.formBuilder.group({
      type: new FormControl('user', [Validators.required]),
      commonName: new FormControl(null, [Validators.required]),
      email: new FormControl(null, Validators.email),
      organizationUnitName: new FormControl(null),
      organization: new FormControl(null, [Validators.required, Validators.maxLength(64)]),
      countryCode: new FormControl(null, [Validators.required, Validators.maxLength(2), Validators.minLength(2)]),
      surname: new FormControl(null),
      givename: new FormControl(null),
      localityName: new FormControl(null),
      state: new FormControl(null),
    }, {
      validator: [ValidForm]
    });

  }

  onChange() {
    this.createSubjectForm.patchValue(
      {
        'commonName': '',
        'email': '',
        'organizationUnitName': '',
        'organization': '',
        'countryCode': '',
        'surname': '',
        'givename': '',
        'localityName': '',
        'state': ''
      }
    );

  }
  create() {
    if (this.createSubjectForm.invalid) {
      this.toastr.error("Please enter file out all the fields.", 'Create subject');
      return;
    }

    const subject = new Entity(this.createSubjectForm.value.type, this.createSubjectForm.value.commonName,
      this.createSubjectForm.value.email, this.createSubjectForm.value.organizationUnitName, this.createSubjectForm.value.organization, this.createSubjectForm.value.countryCode,
      this.createSubjectForm.value.surname, this.createSubjectForm.value.givename, this.createSubjectForm.value.localityName, this.createSubjectForm.value.state);

    this.subjectService.add(subject).subscribe(
      () => {
        this.createSubjectForm.reset();
        this.dialogRef.close();
        this.toastr.success('Successfully added a new subject.', 'Create subject');
        this.subjectService.createSuccessEmitter.next(subject);
      },
      () => {
        this.toastr.error('Subject with same email or common name already exist.', 'Create subject');
      }
    );
  }
}
