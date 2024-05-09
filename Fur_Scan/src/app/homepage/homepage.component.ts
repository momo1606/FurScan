
import { trigger, state, style, animate, transition } from '@angular/animations';
import {Component, HostListener, OnInit, NgZone, ElementRef, Output, Directive, AfterViewInit, OnDestroy, EventEmitter
} from '@angular/core';
import { Router } from '@angular/router';
import { Subscription,interval } from 'rxjs';
import { fromEvent } from 'rxjs';
import { startWith } from 'rxjs/operators';


@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css'],
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
export class HomepageComponent implements OnInit , AfterViewInit , OnDestroy{

 
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
    this.startCarousel();
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

  
  carouselItems = [
    {
      image: 'assets/homepage/Dog_Image_1.jpg',
      text: 'Discover a world of health and happiness for your furry friend at FurScan. We provide advanced health solutions, ensuring your pets live their best lives.'
    },
    {
      image: 'assets/homepage/Dog_Image_2.jpg',
      text: 'At FurScan, we recognize the unique bond between you and your pet. Our mission is to offer cutting-edge pet health services, guaranteeing your pets\' well-being.'
    },
    {
      image: 'assets/homepage/Dog_Image_3.jpg',
      text: 'Empower your pet\'s health journey with FurScan. We specialize in early disease detection and personalized wellness plans, tailored to your pet\'s needs.'
    }
  ];

  slideItems = [
    {
      title: 'Accurate Results, Instantly:',
      description: 'FurScan swiftly identifies dog diseases through pictures, ensuring accurate results within seconds.',
    },
    {
      title: 'Early Detection, Better Health:',
      description: "Catch potential health issues early, improving your dog's chances of a full recovery and a healthier life.",
    },
    {
      title: 'User-Friendly & Quick:',
      description: 'Our simple interface allows effortless uploads, making it accessible for all pet owners. Quick, easy, and effective.',
    },
  ];
   

  startCarousel() {
    setInterval(() => {
      this.nextSlide();
    }, 3000); // Change 3000 to the desired interval in milliseconds (e.g., 3000 for 3 seconds)
  }

  nextSlide() {
    this.currentIndex = (this.currentIndex + 1) % this.carouselItems.length;
  }

  prevSlide() {
    this.currentIndex = (this.currentIndex - 1 + this.carouselItems.length) % this.carouselItems.length;
  }

  LoginPage(){
    this.route.navigateByUrl('/login')
  }
}
