import { AdminGuard } from './guard/admin.guard';
import { ErrorComponent } from './component/error/error.component';
import { NonAuthorizedErrorPageComponent } from './component/non-authorized-error-page/non-authorized-error-page.component';
import { NonAuthenticatedErrorPageComponent } from './component/non-authenticated-error-page/non-authenticated-error-page.component';
import { LoginComponent } from './component/login/login.component';
import { ListCertificatesComponent } from './component/list-certificates/list-certificates.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'certificates',
    component: ListCertificatesComponent,
    canActivate: [AdminGuard],
  },
  //******************* ERROR PAGES ************************
  {
    path: 'error/non-authenticated',
    component: NonAuthenticatedErrorPageComponent,
  },
  {
    path: 'error/non-authorized',
    component: NonAuthorizedErrorPageComponent,
  },
  {
    path: '**',
    component: ErrorComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
