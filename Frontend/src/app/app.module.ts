import { AdminGuard } from './guard/admin.guard';
import { ErrorComponent } from './component/error/error.component';
import { NonAuthorizedErrorPageComponent } from './component/non-authorized-error-page/non-authorized-error-page.component';
import { NonAuthenticatedErrorPageComponent } from './component/non-authenticated-error-page/non-authenticated-error-page.component';
import { MaterialModule } from './material-module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CreateCertificateComponent } from './component/create-certificate/create-certificate.component';
import { AddSubjectComponent } from './component/add-subject/add-subject.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from '@angular/cdk/layout';
import { ToastrModule } from 'ngx-toastr';
import { HeaderComponent } from './component/header/header.component';
import { ListCertificatesComponent } from './component/list-certificates/list-certificates.component';
import { LoginComponent } from './component/login/login.component';
import { TokenInterceptor } from './interseptor/token.interceptor';
import { ErrorInterceptor } from './interseptor/error.interceptor';
import { ChooseTemplateComponent } from './component/choose-template/choose-template.component';
import { CreateSelfSignedCertificateComponent } from './component/create-self-signed-certificate/create-self-signed-certificate.component';
import { MatDatepickerModule } from '@angular/material';
import { CertificateDetailsComponent } from './component/certificate-details/certificate-details.component';
import { CertificateStatusComponent } from './component/certificate-status/certificate-status.component';
import { DownloadCertificateComponent } from './component/download-certificate/download-certificate.component';

@NgModule({
  declarations: [
    AppComponent,
    CreateCertificateComponent,
    HeaderComponent,
    AddSubjectComponent,
    HeaderComponent,
    ListCertificatesComponent,
    LoginComponent,
    NonAuthenticatedErrorPageComponent,
    NonAuthorizedErrorPageComponent,
    ErrorComponent,
    ChooseTemplateComponent,
    CreateSelfSignedCertificateComponent,
    CertificateDetailsComponent,
    CertificateStatusComponent,
    DownloadCertificateComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    LayoutModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    AppRoutingModule,
    MaterialModule,
    HttpClientModule,
    MatDatepickerModule,
  ],
  entryComponents: [
    AddSubjectComponent,
    CreateCertificateComponent,
    ListCertificatesComponent,
    ChooseTemplateComponent,
    CertificateDetailsComponent,
    CertificateStatusComponent,
    DownloadCertificateComponent,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    AdminGuard,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
