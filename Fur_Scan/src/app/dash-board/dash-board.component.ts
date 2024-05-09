import { animate, state, style, transition, trigger } from '@angular/animations';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef, Component, HostListener, NgZone, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApisService } from '../apis.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-dash-board',
  templateUrl: './dash-board.component.html',
  styleUrls: ['./dash-board.component.css'],
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
export class DashBoardComponent implements OnInit {
  itemsPerPage = 5 // Number of items per page
  currentPage = 1; // Current page
  totalPages: number | undefined;
  paginatedPetData: any;


  projectLogo = "assets/homepage/projectLogo.png";
  hamburger_menu = "assets/homepage/list.svg";
  cross = "assets/homepage/cross.svg";

  imageChangeTrigger: boolean = false;
  hamburger: boolean = false;
  public rotateTrigger = false;
  expandedList: boolean = false;
  screenWidth: number | undefined;
  screenHeight: number | undefined;
  userPetHistory: any = null
  userFirstName: any;
  searchName: string = '';
  tableShow: boolean = true;

  constructor(private route: Router, private http: HttpClient, private apiservice: ApisService, private snackBar: MatSnackBar, private ngZone: NgZone, private cdr: ChangeDetectorRef) { }
  ngOnInit() {
    this.getUserData();
    this.getScreenSize();
    this.getData();
    window.scrollTo({ top: 0, behavior: 'instant' });
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
        console.log('API Response successful');
        let temp: any = response
        this.userFirstName = temp.data.user.first_name
        if(temp.data.petReport.length == 0)
        {
          this.tableShow = false
        }
        localStorage.setItem('userPersonalData', JSON.stringify(temp.data.user))
      },
      (error) => {
        this.logout()
        console.error('API Error:', error);
      }
    );
  }

  getData(): void {
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
    let apiUrl = apiPrefix + 'pet/list'

    const id = user_id;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${AuthToken}`
    });

    const url = `${apiUrl}?id=${user_id}`;

    this.http.get(url, { headers }).subscribe(
      (response) => {
        console.log('API Response successful');
        if (response != null) {
          let temp: any = response
          this.userPetHistory = temp.data

          this.totalPages = Math.ceil(this.userPetHistory.length / this.itemsPerPage);
    this.paginate();
        }
      },
      (error) => {
        console.error('API Error:', error);
        // Handle errors here
      }
    );
  }

  paginate(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedPetData = this.userPetHistory.slice(startIndex, endIndex);
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


  getStatusMessage(remarks: string): string {
    return remarks ? 'Reviewed' : 'Under Review';
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
  openDetailsPage(pet: any): void {
    localStorage.setItem('petData' , JSON.stringify(pet))
    this.route.navigateByUrl('details');
  }

  routingToPetDetails(){
    localStorage.setItem('petDetailToken' , "1")
    this.route.navigateByUrl('petdetails')
  }
}
