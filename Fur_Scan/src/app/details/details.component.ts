import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ApisService } from '../apis.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css'],
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
export class DetailsComponent implements OnInit, OnDestroy {
  projectLogo = "assets/homepage/projectLogo.png";
  hamburger_menu = "assets/homepage/list.svg";
  cross = "assets/homepage/cross.svg";

  imageChangeTrigger: boolean = false;
  hamburger: boolean = false;
  public rotateTrigger = false;
  expandedList: boolean = false;
  screenWidth: number | undefined;
  screenHeight: number | undefined;
  petData: any;

  constructor(private route: Router, private http: HttpClient, private apiservice: ApisService, private snackBar: MatSnackBar) { }

  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'instant' });
    this.setPetData()
    this.getScreenSize()
  }

  ngOnDestroy(): void {
    localStorage.removeItem('petData');
  }

  setPetData() {
    let pet = localStorage.getItem('petData')
    if (pet !== null) { this.petData = JSON.parse(pet) }
    localStorage.removeItem('petData');
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
    this.getScreenSize();
  }

  getScreenSize() {
    this.screenWidth = window.innerWidth;
    this.screenHeight = window.innerHeight;
    this.hamburger = this.screenWidth <= 900;
    if (this.screenWidth > 900) {
      this.expandedList = false
    }
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

  onCheckboxChange(pet: any): void {
    if (!pet.checked) {

      let loginInfo = localStorage.getItem('loginSuccess');
      let parseLoginInfo = null

      const formData: FormData = new FormData();
      formData.append('id', pet.pet_report_id);

      if (loginInfo !== null) {
        parseLoginInfo = JSON.parse(loginInfo);
      }

      let AuthToken = ""
      if (parseLoginInfo != null) {
        AuthToken = parseLoginInfo.data.accessToken
      }

      let apiPrefix = this.apiservice.apiPrefix
      let apiUrl = apiPrefix + 'pet/send-to-doctor'

      const headers = new HttpHeaders({
        'Authorization': `Bearer ${AuthToken}`
      });
      this.http.post(apiUrl, formData, { headers }).subscribe(
        (response) => {
          this.openSnackBar('Sent to doc. Kindly visit after sometime to see the updated report')
          console.log('API call successful:');
          pet.checked = true;
        },
        (error) => {
          this.openSnackBar('Sorry some issue on our side. Kindly try after sometime.')
          console.error('API call failed:', error);
          pet.checked = false;
        }
      );

    }
  }
  openSnackBar(message: string) {
    let snackBarRef = this.snackBar.open(message, 'OK', {
      duration: 5000, // Snackbar will be visible for 5 seconds
    });

    snackBarRef.afterDismissed().subscribe(() => {
      this.route.navigateByUrl('/dashboard')
    });
  }

  getDiseaseMessage(disease: string): string {
    switch (disease) {
      case 'Fungal':
        return 'Your pet has been diagnosed with a fungal infection. Fungal infections in pets are typically caused by various types of fungi such as yeast and dermatophytes. Common symptoms include skin irritation, itching, and sometimes changes in coat color. Treatment may involve antifungal medications (e.g., ketoconazole) and proper hygiene. <strong> Send the AI generated report to veterinarian for personalized advice. </strong>';
      case 'Bacterial':
        return 'Your pet is suffering from a bacterial infection. Bacterial infections in pets can affect various body systems, including the skin, ears, and urinary tract. Symptoms may include redness, swelling, discharge, or changes in behavior. Treatment often involves antibiotics (e.g., amoxicillin) prescribed by the veterinarian. It is important to follow the prescribed treatment plan and attend follow-up appointments. <strong>Send the AI generated report to veterinarian for personalized advice.</strong>';
      case 'Hypersensitivity':
        return 'Your pet has been diagnosed with hypersensitivity, which means an exaggerated response of the immune system to certain substances. Common allergens include certain foods, environmental factors, or insect bites. Symptoms may include itching, redness, and gastrointestinal upset. Identifying and avoiding triggers is essential. Consult with the veterinarian to develop an effective management plan for your pet\'s hypersensitivity. In some cases, antihistamines (e.g., diphenhydramine) or corticosteroids may be recommended. <strong>Send the AI generated report to veterinarian for personalized advice.</strong>';
      default:
        return 'No specific information available for this disease.';
    }
  }


}
