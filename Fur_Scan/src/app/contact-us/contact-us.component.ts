import { trigger, state, style, animate, transition } from '@angular/animations';
import {Component, HostListener, OnInit, NgZone, ElementRef, Output, Directive, AfterViewInit, OnDestroy, EventEmitter
} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.css'],
  animations: [
    trigger('imageChange', [
      state('true', style({
        transform: 'rotate(180deg)' // Rotate 45 degrees when true
      })),
      state('false', style({
        transform: 'rotate(0deg)' // No rotation when false
      })),
      transition('true <=> false', animate('0.5s ease-in-out'))
    ]) ,

    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('0.5s ease-in', style({ opacity: 1 })),
      ]),
    ]),
  ]
})
export class ContactUsComponent implements OnInit , AfterViewInit , OnDestroy{


  constructor(private element: ElementRef, private ngZone: NgZone , private route : Router) {
  }



  // Images
  projectLogo = "assets/homepage/projectLogo.png";
  hamburger_menu = "assets/homepage/list.svg" ;
  cross = "assets/homepage/cross.svg";
  dog_carousel1="assets/homepage/Dog_Image_1.jpg"
  dog_carousel2="assets/homepage/Dog_Image_2.jpg"
  dog_carousel3="assets/homepage/Dog_Image_3.jpg"


  // Variables
  hamburger: boolean = false;
  expandedList : boolean = false
  screenWidth: number | undefined;
  screenHeight: number | undefined;
  imageChangeTrigger: boolean = false;
  public rotateTrigger = false;
  currentIndex = 0;

  ngOnInit() {
    window.scrollTo({ top: 0, behavior: 'instant' });
    // Initialize the screen size and hamburger status
    this.getScreenSize();
  }

  ngAfterViewInit(): void {

  }

  ngOnDestroy(): void {

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
    if(this.screenWidth > 900)
    {
      this.expandedList = false
    }
  }

  expandList(){
    this.expandedList= !this.expandedList
  }



  toggleMenu() {
    this.imageChangeTrigger = !this.imageChangeTrigger;
    this.rotateTrigger = !this.rotateTrigger;
    this.expandedList = !this.expandedList;
  }


  LoginPage(){
    this.route.navigateByUrl('/login')
  }

  Home(){
    this.route.navigateByUrl('/')
  }

  AboutUs(){
    this.route.navigateByUrl('/aboutus')
  }
}
