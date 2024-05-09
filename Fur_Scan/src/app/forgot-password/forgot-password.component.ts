import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ApisService } from '../apis.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit , AfterViewInit {
  emailSubmitted: boolean = false;
  apiResponseOtp: any;
  newOtp: boolean = false;
  otpVerificationStatus: any;
  apiResponseFinal: any;
  projectLogo = "assets/homepage/projectLogo.png";
  email: any 
  otp : any
  emailErrorMessage: string = '';
  isEmailValid: boolean = false;
  apiResponse: any | undefined;
  password1: string = '';
  password2: string = '';
  passwordMatchErrorMessage: string = '';
  isPasswordValid: boolean = false;
  ngOnInit(): void {
    window.scrollTo({ top: 0, behavior: 'instant' });
  }

  ngAfterViewInit(): void {
      
  }

  constructor(private http : HttpClient , private apiService : ApisService , private snackBar: MatSnackBar , private route : Router){}
  
  
  validateEmail() {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(this.email)) {
      this.emailErrorMessage = 'Please enter a valid email address.';
      this.isEmailValid = false;
    } else {
      this.emailErrorMessage = '';
      this.isEmailValid = true;
    }
  }

  resetPassword() {
    if (this.isEmailValid) {
      const apiUrl = this.apiService.apiPrefix+'otp';
      const formData = new FormData();
      formData.append('email', this.email);
      this.http.post(apiUrl, formData)
        .subscribe(response => {
          console.log('API Response successful');
          this.apiResponse = response
          if (this.apiResponse.status === 'Success') {
            this.emailSubmitted = true;
          }
        }, error => {
          console.error('API Error:', error);
        });
    }
  }

  verifyOtp() {
    if (this.email && this.otp) {
      const apiUrl = this.apiService.apiPrefix+ 'otp-verification';
      const formData = new FormData();
      formData.append('email', this.email);
      formData.append('otp', this.otp);
      this.http.post(apiUrl, formData)
        .subscribe(response => {
          console.log('API Response successful');
          this.apiResponseOtp = response
          if(this.apiResponseOtp.status === "Success")
          {
            this.newOtp = true 
            this.otpVerificationStatus = 'Success'
          }
          else{
            this.otpVerificationStatus = 'error'
          }
        }, error => {
          console.error('API Error:', error);
          this.otpVerificationStatus = 'error'
        });
    }
  }

  navigateLogin(){
    this.route.navigateByUrl('login')
  }

  navigateSignUp(){
    this.route.navigateByUrl('signup')
  }

  validatePasswords() {
      // Check if passwords match
      this.isPasswordValid = this.password1 === this.password2;
      this.passwordMatchErrorMessage = this.isPasswordValid ? '' : 'Passwords do not match';
  }

  passwordSet() {
      if (this.isPasswordValid) {
          const apiUrl = this.apiService.apiPrefix + 'reset-password';
          const formData = new FormData();
          formData.append('email', this.email);
          formData.append('password', this.password1);

          this.http.post(apiUrl, formData)
              .subscribe(response => {
                  console.log('API Response successful');
                  this.apiResponseFinal = response
                  if(this.apiResponseFinal.status === 'Success')
                  {
                    this.openSnackBar('Password Set Successfully')
                  }
                  else{
                    this.openSnackBar('Some issue from our end kindly try after sometime')
                  }
                  // Handle the response as needed
              }, error => {
                this.openSnackBar('Some issue from our end kindly try after sometime')
                  console.error('API Error:', error);
              });
      }
  }

  openSnackBar(message: string) {
    let snackBarRef = this.snackBar.open(message, 'OK', {
      duration: 5000, // Snackbar will be visible for 5 seconds
    });
    
    snackBarRef.afterDismissed().subscribe(() => {
      this.route.navigateByUrl('')
    });
  }
  
}
