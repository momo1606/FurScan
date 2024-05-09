import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { LoginPageComponent } from './login-page/login-page.component';
import { SignUpPageComponent } from './sign-up-page/sign-up-page.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ThankYouForRegisteringComponent } from './thank-you-for-registering/thank-you-for-registering.component';
import { DashBoardComponent } from './dash-board/dash-board.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { FAQsComponent } from './faqs/faqs.component';
import { PetDetailsComponent } from './pet-details/pet-details.component';
import { UserAccountComponent } from './user-account/user-account.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { FeaturesComponent } from './features/features.component';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { FilterByPetNamePipe } from './filter-by-pet-name.pipe';
import { DetailsComponent } from './details/details.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    LoginPageComponent,
    SignUpPageComponent,
    ThankYouForRegisteringComponent,
    DashBoardComponent,
    AboutUsComponent,
    FAQsComponent,
    PetDetailsComponent,
    UserAccountComponent,
    ContactUsComponent,
    FeaturesComponent,
    DoctorDashboardComponent,
    FilterByPetNamePipe,
    DetailsComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
    MatToolbarModule ,
    FormsModule , 
    HttpClientModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatTableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
