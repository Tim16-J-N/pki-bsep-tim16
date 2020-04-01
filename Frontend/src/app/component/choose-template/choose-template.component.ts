import { Template } from './../../model/template';
import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-choose-template',
  templateUrl: './choose-template.component.html',
  styleUrls: ['./choose-template.component.css']
})
export class ChooseTemplateComponent implements OnInit {

  chooseTemplateForm: FormGroup;
  templateSelected: Template;

  blank = new Template(false, false, false, false, false, false, false, false, false, false);
  ca = new Template(true, true, true, false, false, true, true, false, false, false);
  sslServer = new Template(true, true, false, true, true, false, false, true, false, false);
  sslClient = new Template(true, true, false, true, true, false, false, false, true, false);
  codeSigning = new Template(true, true, false, true, false, false, false, false, false, true);

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<ChooseTemplateComponent>
  ) {
    this.templateSelected = this.blank;
  }

  ngOnInit() {
    this.chooseTemplateForm = this.formBuilder.group({
      template: new FormControl(this.blank, Validators.required)
    });
  }

  chooseTemplate() {
    this.templateSelected = this.chooseTemplateForm.value;
    this.dialogRef.close();
  }
}