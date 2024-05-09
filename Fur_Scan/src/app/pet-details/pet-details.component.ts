import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ApisService } from '../apis.service';
import { Observable, map } from 'rxjs';

@Component({
  selector: 'app-pet-details',
  templateUrl: './pet-details.component.html',
  styleUrls: ['./pet-details.component.css']
})
export class PetDetailsComponent implements OnInit {
  projectLogo = "assets/homepage/projectLogo.png";
  petForm: FormGroup;
  image_text: any;
  AuthToken: any;
  petId: any;
  jpgend: any;
  imageSource: string = 'local'; // Initialize with a default value

  constructor(private route: Router, private http: HttpClient, private formBuilder: FormBuilder, private snackBar: MatSnackBar, private apiservice: ApisService) {

    let loginInfo = localStorage.getItem('loginSuccess');
    let parseLoginInfo = null
    if (loginInfo !== null) {
      parseLoginInfo = JSON.parse(loginInfo);
    }
    let user_id = ""
    if (parseLoginInfo != null) {
      user_id = parseLoginInfo.data.user.user_id
      user_id = user_id.toString()
      this.AuthToken = parseLoginInfo.data.accessToken
    }

    this.petForm = this.formBuilder.group({
      user_id: [user_id],
      pet_name: [''],
      age: [''],
      breed: [''],
      status: [''],
    });
  }

  ngOnInit() {
    if(!localStorage.getItem('petDetailToken')){
      this.route.navigateByUrl('')
    }
    window.scrollTo({ top: 0, behavior: 'instant' });

    localStorage.removeItem('petDetailToken');
  }


  onImageSourceSelected(event: any) {
    this.imageSource = event.target.value;
  }

  onFileSelected(event: any) {
    if (this.imageSource === 'local') {
      const file: File = event.target.files[0];
      this.image_text = file;
    } else if (this.imageSource === 'cloud') {
      const cloudImageUrl = event.target.value;
      this.downloadAndStoreImage(cloudImageUrl);
    }
  }

  downloadAndStoreImage(gDriveLink: string): void {
    const fileId = gDriveLink.match(/\/d\/(.+?)\//)?.[1];
    if (fileId) {
      const downloadLink = `https://drive.google.com/uc?export=open&id=${fileId}`;
      this.http.get(downloadLink, { responseType: 'blob' }).subscribe(
        (blob: Blob) => {
          this.storeImageInVariable(blob);
        },
        (error) => {
          console.error('Error downloading file:', error);
        }
      );
    } else {
      console.error('Invalid Google Drive link:', gDriveLink);
    }
  }

  storeImageInVariable(blob: Blob): void {
    const defaultFileName = 'downloaded_image';
    const fileExtension = '.jpg';
    const fileNameWithExtension = defaultFileName + fileExtension;

    const file = new File([blob], fileNameWithExtension, { type: 'image/jpg' });
    this.image_text = file;
  }

  onSubmit() {
    this.uploadPetData(this.image_text, this.petForm.value)
  }

  openSnackBar(message: string) {
    let snackBarRef = this.snackBar.open(message, 'OK', {
      duration: 5000, // Snackbar will be visible for 5 seconds
    });

    snackBarRef.afterDismissed().subscribe(() => {
      this.route.navigateByUrl('/dashboard')
    });
  }



  uploadPetData(file: File, petData: any): void {
    const formData: FormData = new FormData();
    formData.append('image_text', file, file.name);
    formData.append('petReport', JSON.stringify(petData));
    const headers = {
      Authorization: `Bearer ${this.AuthToken}`
    };
    let apiPrefix = this.apiservice.apiPrefix

    this.http.post(apiPrefix + 'pet/create', formData, { headers })
      .subscribe(
        (response) => {
          console.log('API Response submission');

          let parsepetCreateResponse: any = response;

          if (parsepetCreateResponse && parsepetCreateResponse.data) {
            this.petId = parsepetCreateResponse.data.pet_report_id;
            this.jpgend = parsepetCreateResponse.data.image_text;

            if (this.jpgend) {
              const parts = this.jpgend.split('/');
              this.jpgend = parts[1];
            } else {
              // Handle the case where image_text is not available
              this.jpgend = 'N/A';
            }
            this.postPrediction(this.petId, this.jpgend)
          } else {
            // Handle the case where data is not available in the response
            console.error('Invalid response format:', parsepetCreateResponse);
          }
        },
        (error) => {
          this.openSnackBar('Some error , Kindly login again as the token might have expired');
          console.error('API Error:', error);
        }
      );
    this.openSnackBar('Pet registered. Report download will start in sometime');
  }

  postPrediction(petId: number, imageText: string): void {
    const baseUrl = this.apiservice.baseUrl;
    const apiUrl = `${baseUrl}${petId}/${imageText}`;
    const headers = {
    };

    this.http.post(apiUrl, {}, { headers })
      .subscribe(
        (response) => {
          console.log('Prediction API successful');
          let awshit: any = response
          awshit = awshit.report_object_key

          this.downloadPdf(awshit)

        },
        (error) => {
          console.error('Prediction API Error:', error);
          // Handle errors here
        }
      );
  }


  downloadPdf(reportObjectKey: string): void {
    let s3BucketUrl = 'https://furscan.s3.amazonaws.com/';
    const pdfUrl = s3BucketUrl + reportObjectKey;
    // Fetch the PDF file as an ArrayBuffer
    this.http.get(pdfUrl, { responseType: 'arraybuffer' }).subscribe((data) => {
      // Create a Blob from the ArrayBuffer
      const blob = new Blob([data], { type: 'application/pdf' });

      // Create a temporary URL for the Blob
      const blobUrl = URL.createObjectURL(blob);
      const anchor = document.createElement('a');
      anchor.href = blobUrl;
      anchor.download = 'pet_report.pdf'; // Specify the desired file name
      anchor.click();

      // Clean up: revoke the temporary URL and remove the anchor element
      URL.revokeObjectURL(blobUrl);
      anchor.remove();
    });
  }
}


