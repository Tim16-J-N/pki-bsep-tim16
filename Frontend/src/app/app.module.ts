import { AdminGuard } from './guard/admin.guard';
import { ErrorComponent } from './component/error/error.component';
import { NonAuthorizedErrorPageComponent } from './component/non-authorized-error-page/non-authorized-error-page.component';
import { NonAuthenticatedErrorPageComponent } from './component/non-authenticated-error-page/non-authenticated-error-page.component';
import { MaterialModule } from './material-module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChooseTemplateComponent } from './component/choose-template/choose-template.component';
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

@NgModule({
  declarations: [
    AppComponent,
    ChooseTemplateComponent,
    HeaderComponent,
    ListCertificatesComponent,
    LoginComponent,
    NonAuthenticatedErrorPageComponent,
    NonAuthorizedErrorPageComponent,
    ErrorComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    LayoutModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    MaterialModule,
  ],
  entryComponents: [
    ListCertificatesComponent,
    ChooseTemplateComponent,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    AdminGuard,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
