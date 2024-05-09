import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef, Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ApisService } from '../apis.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.css'],
  animations: [
    trigger('imageChange', [
      state('true', style({
        transform: 'rotate(180deg)' // Rotate 45 degrees when true
      })),
      state('false', style({
        transform: 'rotate(0deg)' // No rotation when false
      })),
      transition('true <=> false', animate('0.5s ease-in-out'))
    ]),

    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('0.5s ease-in', style({ opacity: 1 })),
      ]),
    ]),
  ]
})
export class UserAccountComponent {
  projectLogo = "assets/homepage/projectLogo.png";
  hamburger_menu = "assets/homepage/list.svg";
  cross = "assets/homepage/cross.svg";

  imageChangeTrigger: boolean = false;
  hamburger: boolean = false;
  public rotateTrigger = false;
  expandedList: boolean = false;
  screenWidth: number | undefined;
  screenHeight: number | undefined;
  userData: any;
  userFirstname: any;
  userLastname: any;
  userEmail: any;
  userPhoneNo: any;
  userProfileImage: any;
  userId: any;
  profileImage: any;

  accountForm!: FormGroup;

  editFirstName: boolean = false;
  editLastName: boolean = false;
  editEmail: boolean = false;
  editPhoneNo: boolean = false;
  editedFirstName: string = '';
  editedLastName: string = '';
  editedEmail: string = '';
  editedPhoneNo: string = '';
  isSubmitEnabled: boolean = false;
  imageAltText: string ='';


  constructor(private route: Router, private http: HttpClient, private sanitizer: DomSanitizer, private apiservice: ApisService, private cdr: ChangeDetectorRef, private fb: FormBuilder , private snackBar: MatSnackBar) { }
  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'instant' });
    // Initialize the screen size and hamburger status
    this.initForm(); // Call the initialization in ngOnInit
    this.getScreenSize();
    this.getUserData();
  }

  initForm() {
    this.accountForm = this.fb.group({
      editedFirstName: [''],
      editedLastName: [''],
      editedEmail: [''],
      editedPhoneNo: [''],
    });
  }
  getUserData(): void {
    let data = localStorage.getItem('userPersonalData')
    if (data != null) {
      let JsonData = JSON.parse(data)
      this.userFirstname = JsonData.first_name
      this.userLastname = JsonData.last_name
      this.userEmail = JsonData.email
      this.userPhoneNo = JsonData.phone_no
      this.userId = JsonData.user_id
      this.profileImage = JsonData.profile_image_text
      if (JsonData.profile_image_text) {
        this.userProfileImage = this.convertBase64ToImage(JsonData.profile_image_text);
    } else {
        this.userProfileImage = null; 
        this.imageAltText = "Image not uploaded by user";
    }
    }
  }
  convertBase64ToImage(base64Data: string): SafeUrl {
    const imageUrl = 'data:image/jpeg;base64,' + base64Data; // Change the MIME type to image/jpeg
    return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
  }
  backToAccount() {
    let data = localStorage.getItem('userPersonalData');
    if (data !== null) {
      let jsonData = JSON.parse(data);
      if (jsonData.is_doctor === 1) {
        this.route.navigateByUrl('/doctorDashboard');
      } else {
        this.route.navigateByUrl('/dashboard');
      }
    } else {
      this.route.navigateByUrl('');
    }
  }

  logout() {
    const clinicName = localStorage.getItem('clinicName');
    const clinicLocation = localStorage.getItem('clinicLocation');
    localStorage.clear();
    if (clinicName !== null) {
      localStorage.setItem('clinicName', clinicName);
    }
    if (clinicLocation !== null) {
      localStorage.setItem('clinicLocation', clinicLocation);
    }
    this.route.navigateByUrl('');
  }

  toggleMenu() {
    this.imageChangeTrigger = !this.imageChangeTrigger;
    this.rotateTrigger = !this.rotateTrigger;
    this.expandedList = !this.expandedList;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    // Update the screen size
    this.getScreenSize();
  }

  getScreenSize() {
    this.screenWidth = window.innerWidth;
    this.screenHeight = window.innerHeight;

    // Check screen width and set hamburger status
    this.hamburger = this.screenWidth <= 900;
    if (this.screenWidth > 900) {
      this.expandedList = false
    }
  }


  editField(fieldName: string) {
    if (this.accountForm?.valid) {
      const editedFirstNameControl = this.accountForm.get('editedFirstName');
      const editedLastNameControl = this.accountForm.get('editedLastName');
      const editedEmailControl = this.accountForm.get('editedEmail');
      const editedPhoneNoControl = this.accountForm.get('editedPhoneNo');


      if (editedFirstNameControl && editedLastNameControl && editedEmailControl && editedPhoneNoControl) {
        switch (fieldName) {
          case 'first_name':
            editedFirstNameControl.setValue(this.userFirstname);
            this.editFirstName = true;
            break;
          case 'last_name':
            editedLastNameControl.setValue(this.userLastname);
            this.editLastName = true;
            break;
          case 'email':
            editedEmailControl.setValue(this.userEmail);
            this.editEmail = true;
            break;
          case 'phone_no':
            editedPhoneNoControl.setValue(this.userPhoneNo);
            this.editPhoneNo = true
            break;
        }
      }
    }
    this.isSubmitEnabled = true;
  }


  submitChanges() {
    let loginInfo = localStorage.getItem('loginSuccess');
    let parseLoginInfo = null
    if (loginInfo !== null) {
      parseLoginInfo = JSON.parse(loginInfo);
    }
    let user_id = ""
    let AuthToken = ""
    if (parseLoginInfo != null) {
      user_id = parseLoginInfo.data.user.user_id
      user_id = user_id.toString()
      AuthToken = parseLoginInfo.data.accessToken
    }
    let apiPrefix = this.apiservice.apiPrefix

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${AuthToken}`,
      'Content-Type': 'application/json', // Include this header for JSON content
    });
    const url = `${apiPrefix}update/${user_id}`;
    if (this.accountForm?.valid) {
      const formData = this.accountForm.value;
      if (formData.editedEmail == '') {
        formData.editedEmail = this.userEmail
      }
      if (formData.editedFirstName == '') {
        formData.editedFirstName = this.userFirstname
      }
      if (formData.editedLastName == '') {
        formData.editedLastName = this.userLastname
      }
      if (formData.editedPhoneNo == '') {
        formData.editedPhoneNo = this.userPhoneNo
      }
      let requestBody = {
        first_name: formData.editedFirstName,
        last_name: formData.editedLastName,
        email: formData.editedEmail,
        phone_no: formData.editedPhoneNo,
        profile_image_text: this.profileImage
      };
      let request = JSON.stringify(requestBody)
      this.http.post(url, request, { headers }).subscribe(
        (response) => {
          console.log('Request successful');

          this.userEmail = formData.editedEmail
          this.userFirstname = formData.editedFirstName
          this.userLastname = formData.editedLastName
          this.userPhoneNo = formData.editedPhoneNo

          this.openSnackBar("Profile Updated Successfully. Kindly click on OK to go to Dashboard")
        },
        (error) => {
          console.error('Error making request', error);
          this.openSnackBar("Some issue , kindly login again as the token might have expired")
        }
      );

      this.accountForm.reset();
      this.editFirstName = false;
      this.editLastName = false;
      this.editPhoneNo = false;
      this.editEmail = false;
      this.isSubmitEnabled = false;
    }
  }
  
  openSnackBar(message: string) {
    let snackBarRef = this.snackBar.open(message, 'OK', {
      duration: 5000, 
    });

    snackBarRef.afterDismissed().subscribe(() => {
      this.backToAccount()
    });
  }

}
