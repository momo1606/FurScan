import { animate, state, style, transition, trigger } from '@angular/animations';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApisService } from '../apis.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-doctor-dashboard',
  templateUrl: './doctor-dashboard.component.html',
  styleUrls: ['./doctor-dashboard.component.css'],
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
export class DoctorDashboardComponent implements OnInit {

  projectLogo = "assets/homepage/projectLogo.png";
  hamburger_menu = "assets/homepage/list.svg";
  cross = "assets/homepage/cross.svg";

  imageChangeTrigger: boolean = false;
  hamburger: boolean = false;
  public rotateTrigger = false;
  expandedList: boolean = false;
  screenWidth: number | undefined;
  screenHeight: number | undefined;
  userFirstName: any;
  clinicName: any;
  clinicLocation: any;
  isEditing: boolean = false;
  editedClinicName: string = '';
  editedClinicLocation: string = '';
  petHistory: any;
  expandedPet: any;

  searchName: string = '';
  itemsPerPage = 5 // Number of items per page
  currentPage = 1; // Current page
  totalPages: number | undefined;
  paginatedPetData: any;

  constructor(private route: Router, private http: HttpClient, private apiservice: ApisService, private modalService: NgbModal , private snackBar:MatSnackBar) { }
  ngOnInit(): void {
    this.getUserData()
    this.location()
    this.getPetList()
    window.scrollTo({ top: 0, behavior: 'instant' });
  }

  location() {
    if (localStorage.getItem('clinicName')) {
      this.clinicName = localStorage.getItem('clinicName')
    }
    else {
      this.clinicName = 'Halifax Clinic'
    }

    if (localStorage.getItem('clinicLocation')) {
      this.clinicLocation = localStorage.getItem('clinicLocation')
    }
    else {
      this.clinicLocation = 'Downtown'
    }
  }


  toggleEdit() {
    this.isEditing = !this.isEditing;
    // Initialize edited values
    this.editedClinicName = this.clinicName;
    this.editedClinicLocation = this.clinicLocation;
  }

  saveChanges() {
    // Save changes to local storage
    localStorage.setItem('clinicName', this.editedClinicName);
    localStorage.setItem('clinicLocation', this.editedClinicLocation);

    // Update displayed values
    this.clinicName = this.editedClinicName;
    this.clinicLocation = this.editedClinicLocation;

    this.isEditing = false;
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

  getPetList(): void {
    let apiPrefix = this.apiservice.apiPrefix
    let apiUrl = apiPrefix + 'vet/list'
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

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${AuthToken}`,
      'Content-Type': 'application/json',
    });

    this.http.get(apiUrl, { headers }).subscribe(
      (response) => {
        console.log('API Response successful');
        if (response != null) {
          let temp: any = response
          this.petHistory = temp.data
          this.totalPages = Math.ceil(this.petHistory.length / this.itemsPerPage);
          this.paginate()
        }
      },
      (error) => {
        console.error('API Error:', error);
      }
    );
  }

  paginate(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedPetData = this.petHistory.slice(startIndex, endIndex);
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.paginate();
    }
  }

  nextPage(): void {
    if(this.totalPages){
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.paginate();
    }
  }
  }

  addRemarks(petId: number, remarks: string): void {
    let apiPrefix = this.apiservice.apiPrefix;
    let apiUrl = apiPrefix + 'vet/add-remarks';

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
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${AuthToken}`,
      'Content-Type': 'application/json',
    });

    const body = {
      id: petId,
      remarks: remarks
    };

    this.http.put(apiUrl, body, { headers }).subscribe(
      (response) => {
        console.log('API Response successful');
        this.openSnackBar('Review has been sent to User. Kindly reload the page to see updated report')
      },
      (error) => {
        console.error('API Error:', error);
      }
    );
  }

  openSnackBar(message: string) {
    let snackBarRef = this.snackBar.open(message, 'OK', {
      duration: 5000, 
    });
    
    // snackBarRef.afterDismissed().subscribe(() => {
    //   this.route.navigateByUrl('')
    // });
  }

  expandDetails(pet: any) {
    this.expandedPet = pet;
  }

  closeModal() {
    this.expandedPet = null;
  }

  sendRemarks() {
    let petId = this.expandedPet.pet_report_id
    let remarks = this.expandedPet.remarks;
    this.addRemarks(petId , remarks)
    this.expandedPet = null;
  }

  getUserData(): void {
    let apiPrefix = this.apiservice.apiPrefix
    let apiUrl = apiPrefix + 'me';
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
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${AuthToken}`
    });

    this.http.get(apiUrl, { headers }).subscribe(
      (response) => {
        console.log('API Response succesful');
        let temp: any = response
        this.userFirstName = temp.data.user.first_name
        localStorage.setItem('userPersonalData', JSON.stringify(temp.data.user))
      },
      (error) => {
        this.logout()
        console.error('API Error:', error);
      }
    );
  }
}
