import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Validators, FormBuilder, FormGroup, AbstractControl, ValidatorFn } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ApisService } from '../apis.service';

@Component({
  selector: 'app-sign-up-page',
  templateUrl: './sign-up-page.component.html',
  styleUrls: ['./sign-up-page.component.css']
})
export class SignUpPageComponent implements OnInit {
  projectLogo = "assets/homepage/projectLogo.png";
  signUpForm: FormGroup;
  passwordFieldType: string = 'password';
  fileSizeError: boolean = false; // Property to track file size error
  onSubmitResponse: any | undefined;


  constructor(private route: Router, private http: HttpClient, private formBuilder: FormBuilder, private snackBar: MatSnackBar , private apiservice : ApisService) {
    this.signUpForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.alphanumericValidator , Validators.minLength(8)]],
      last_name: ['', Validators.required],
      first_name: ['', Validators.required],
      title: [''],

      country_code: ['', [Validators.required , this.numberValidator()]],
      phone_no: ['', [Validators.required , this.numberValidator()]],

      profile_image_text: [''],
      is_doctor: ['', Validators.required],
      is_active: ['', Validators.required]
    });

    this.signUpForm.get('is_active')?.valueChanges.subscribe((value) => {
      this.signUpForm.get('is_active')?.setValue(value ? '1' : '');
    });
  }

  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'instant' });
  }

   numberValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;
      if (!/^[0-9]*$/.test(value)) {
        return { 'invalidNumber': true };
      }
      return null;
    };
  }

  alphanumericValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const value: string = control.value;
    if (value) {
      const alphanumericRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$/;
      if (!alphanumericRegex.test(value)) {
        return { 'invalidAlphanumeric': true };
      }
    }
    return null;
  }

  onFileSelected(event: any) {
    let file: File = event.target.files[0];
    if (file) {
      if (file.size <= 50 * 1024) {
        this.fileSizeError = false; // Reset the error state
        this.convertToBase64(file).then((base64: string) => {
          this.signUpForm.patchValue({
            profile_image_text: base64
          });
        });
      }else{
        this.fileSizeError = true; // Reset the error state
       console.log('File size Exceeds') ;
      }
    }else {
      // If no file is selected, reset the error state
      this.fileSizeError = false;
    }
  } 

  convertToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();
      reader.onloadend = () => {
        let base64String = reader.result as string;
        resolve(base64String.split(',')[1]); // Extract base64 data without the data URL prefix
      };
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }

  togglePasswordVisibility() {
    this.passwordFieldType = (this.passwordFieldType === 'password') ? 'text' : 'password';
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      let userTitle = this.signUpForm.value.title
      if (userTitle == "1") {
        this.signUpForm.value.first_name = "Mr." + this.signUpForm.value.first_name
      }
      else if (userTitle == "2") {
        this.signUpForm.value.first_name = "Ms." + this.signUpForm.value.first_name
      }
      else {
        this.signUpForm.value.first_name = "Mrs." + this.signUpForm.value.first_name
      }
    }

    this.signUpForm.value.phone_no = this.signUpForm.value.country_code + this.signUpForm.value.phone_no
    this.signUpForm.value.is_active = '1'

    delete this.signUpForm.value.country_code
    delete this.signUpForm.value.title;
    let apiPrefix = this.apiservice.apiPrefix
    this.http.post(apiPrefix+'create-user', this.signUpForm.value)
      .subscribe(response => {

        this.onSubmitResponse = JSON.stringify(response) 
        
        this.onSubmitResponse = JSON.parse(this.onSubmitResponse)

        if(this.onSubmitResponse.status == "Success"){

        localStorage.setItem('thankYouPage' , "1")

        this.route.navigate(['/thankYouRegistering'] , { queryParams: { cameFromSignup: true } });
        }
        else{
          this.openSnackBar('Failed to register. Please try again later.');
        }
      }, error => {
        this.onSubmitResponse = error;
        console.error('API Error:', error);
        this.openSnackBar('User with above credentials already exist, kindly use differet email');
      });
  }

  openSnackBar(message: string) {
    let snackBarRef = this.snackBar.open(message, 'OK', {
      duration: 5000, // Snackbar will be visible for 5 seconds
    });

    snackBarRef.afterDismissed().subscribe(() => {
      // Clear the form after the snackbar is dismissed
      this.signUpForm.reset();
      this.signUpForm.get('title')?.setValue('');
      this.signUpForm.get('is_doctor')?.setValue('');
    });
  }
}
