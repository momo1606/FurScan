import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-thank-you-for-registering',
  templateUrl: './thank-you-for-registering.component.html',
  styleUrls: ['./thank-you-for-registering.component.css']
})
export class ThankYouForRegisteringComponent implements OnInit {
  constructor(private route : Router){}

  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'instant' });
    localStorage.removeItem('thankYouPage');
  }


  projectLogo = "assets/homepage/projectLogo.png";
  redirectToLogin(){
    this.route.navigateByUrl('/login')  }
}
