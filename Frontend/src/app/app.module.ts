import { MaterialModule } from './material-module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChooseTemplateComponent } from './component/choose-template/choose-template.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from '@angular/cdk/layout';
import { ToastrModule } from 'ngx-toastr';
import { HeaderComponent } from './component/header/header.component';
import { ListCertificatesComponent } from './component/list-certificates/list-certificates.component';

@NgModule({
  declarations: [
    AppComponent,
    ChooseTemplateComponent,
    HeaderComponent,
    ListCertificatesComponent
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
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
