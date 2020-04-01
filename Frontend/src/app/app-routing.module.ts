import { CreateCertificateComponent } from './component/create-certificate/create-certificate.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    component: CreateCertificateComponent,
  },
  {
    path: 'admin/create-certificate',
    component: CreateCertificateComponent,
  },

  {
    path: '**',
    component: CreateCertificateComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

