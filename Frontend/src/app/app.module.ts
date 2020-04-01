import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CreateCertificateComponent } from './component/create-certificate/create-certificate.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { HeaderComponent } from './component/header/header.component';
import { MaterialModule } from './material-module';
import { AddSubjectComponent } from './component/add-subject/add-subject.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    CreateCertificateComponent,
    HeaderComponent,
    AddSubjectComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    AppRoutingModule,
    MaterialModule,
    HttpClientModule,
  ],
  entryComponents: [
    AddSubjectComponent,
    CreateCertificateComponent,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
