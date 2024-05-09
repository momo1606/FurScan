import { trigger, state, style, animate, transition } from '@angular/animations';
import { Component,HostListener, OnInit, NgZone, ElementRef, Output, Directive, AfterViewInit, OnDestroy, EventEmitter
} from '@angular/core';
import { Router } from '@angular/router';
import { Subscription,interval } from 'rxjs';

@Component({
  selector: 'app-about-us',
  templateUrl: './about-us.component.html',
  styleUrls: ['./about-us.component.css'],
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
export class AboutUsComponent {

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
    // Initialize the screen size and hamburger status
    this.getScreenSize();
    this.startCarousel();
    window.scrollTo({ top: 0, behavior: 'instant' });
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
      image: 'assets/AboutUs/t1.jpg',
      text: '"Furscan has been a game-changer for us! Within minutes, we had a diagnosis for our dog\'s ' +
        'skin condition, and the report was promptly reviewed by a qualified vet. It\'s like having a ' +
        'dermatologist for our furry friend right in our pocket!” - Sarah H., Dog Owner'
    },
    {
      image: 'assets/AboutUs/t2.jpg',
      text: '"Furscan is an invaluable tool in my practice. It not only expedites the diagnosis process but also ' +
        'ensures that pets receive the right treatment from the very beginning. It\'s a true game-changer for ' +
        'the veterinary community.” - Dr. James K., Veterinarian'
    },
    {
      image: 'assets/AboutUs/t3.jpg',
      text: ' "can\'t express how relieved I am to have found Furscan. It takes away the uncertainty and worry when ' +
        'you see your furry friend suffering. The accuracy of diagnosis and the care provided by the vets is ' +
        'exceptional.” - Emma S., Pet Parent'
    },
    {
      image: 'assets/AboutUs/t4.jpg',
      text: '"“Woof! Thanks to Furscan, my itchy days are over! The process was quick, and the vet\'s recommendations ' +
        'really made a difference. I\'m back to wagging my tail with joy!” - Max, the Dog'
    }
  ];

  startCarousel() {
    setInterval(() => {
      this.nextSlide();
    }, 3000);
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

  HomePage(){
    this.route.navigateByUrl('/')
  }

  ContactUs(){
    this.route.navigateByUrl('/contactUs')
  }


}
