import { ListCertificatesComponent } from './component/list-certificates/list-certificates.component';
import { ChooseTemplateComponent } from './component/choose-template/choose-template.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    component: ListCertificatesComponent,
  },

  {
    path: '**',
    component: ListCertificatesComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
