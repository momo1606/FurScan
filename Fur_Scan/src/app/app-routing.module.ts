import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { SignUpPageComponent } from './sign-up-page/sign-up-page.component';
import { ThankYouForRegisteringComponent } from './thank-you-for-registering/thank-you-for-registering.component';
import { DashBoardComponent } from './dash-board/dash-board.component';
import { FAQsComponent } from './faqs/faqs.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { authGuard } from './auth.guard';
import { auth2Guard } from './auth2.guard';
import { PetDetailsComponent } from './pet-details/pet-details.component';
import { UserAccountComponent } from './user-account/user-account.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { FeaturesComponent } from './features/features.component';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { DetailsComponent } from './details/details.component';
import { auth3Guard } from './auth3.guard';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { authThankYouPageGuard } from './auth-thank-you-page.guard';

const routes: Routes = [
  {path : '' , component: HomepageComponent , canActivate :[authGuard]},
  {path:'login' , component : LoginPageComponent , canActivate :[authGuard]},
  {path:'signup' , component : SignUpPageComponent , canActivate :[authGuard]},
  {path:'thankYouRegistering' , component:ThankYouForRegisteringComponent , canActivate :[authThankYouPageGuard]},
  {path : 'dashboard' , component : DashBoardComponent , canActivate :[auth2Guard]},
  {path : 'faqs' , component : FAQsComponent , canActivate :[authGuard]},
  {path:'aboutus', component : AboutUsComponent, canActivate :[authGuard]},
  {path:'petdetails' , component:PetDetailsComponent , canActivate :[auth2Guard]},
  {path:'account', component:UserAccountComponent , canActivate :[auth2Guard]},
  {path:'contactUs', component:ContactUsComponent ,canActivate :[authGuard]},
  {path:'features' , component:FeaturesComponent ,canActivate :[authGuard]},
  {path:'doctorDashboard' , component:DoctorDashboardComponent , canActivate :[auth2Guard]},
  { path: 'details', component: DetailsComponent ,  canActivate :[auth3Guard] },
  {path:'forgotPass' , component : ForgotPasswordComponent , canActivate : [authGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
