import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Location } from '@angular/common';
import { ApisService } from '../apis.service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  projectLogo = "assets/homepage/projectLogo.png";
  loginForm: FormGroup;
  onSubmitResponse: any | undefined;

  constructor(private route: Router, private http: HttpClient, private formBuilder: FormBuilder, private snackBar: MatSnackBar, private location: Location , private apiservice:ApisService) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)]],
    });
  }
  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'instant' });
  }

  SignUpPage() {
    this.route.navigateByUrl('/signup');
  }

  onSubmit() {
    if (this.loginForm.valid) {
      let userData = this.loginForm.value;
      let apiPrefix = this.apiservice.apiPrefix
      this.http.post(apiPrefix+'login', userData)
        .subscribe(response => {
          this.onSubmitResponse = JSON.stringify(response)
          localStorage.setItem('loginSuccess' , this.onSubmitResponse)
          this.onSubmitResponse = JSON.parse(this.onSubmitResponse)
          if (this.onSubmitResponse.status == "Success") {
            localStorage.setItem('token' , this.onSubmitResponse.data.accessToken)
            if(this.onSubmitResponse.data.user.is_doctor == 1)
            {
              this.route.navigateByUrl('/doctorDashboard');
            }
            else{
              this.route.navigateByUrl('/dashboard');
            }
          }
          else {
            this.openSnackBar('Cannot Login currently. Please try again later.');
          }
        }, error => {
          this.onSubmitResponse = error;
          console.error('API Error:', error);
          this.openSnackBar('The credentials provided do not match our records, kindly provide corrext credentials');
        });
    } else {
      // The form is invalid, show error messages or perform other actions
      console.log('Invalid form submission. Please check your input.');
    }
  }

  forgotPassword(){
    this.route.navigateByUrl('forgotPass')
  }


  openSnackBar(message: string) {
    let snackBarRef = this.snackBar.open(message, 'OK', {
      duration: 5000, // Snackbar will be visible for 5 seconds
    });

    snackBarRef.afterDismissed().subscribe(() => {
      // Clear the form after the snackbar is dismissed
      this.loginForm.reset();
    });
  }
}
