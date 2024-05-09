import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApisService {
  apiPrefix: string = 'http://furscan-env.eba-epem2tmm.us-east-1.elasticbeanstalk.com/';
  baseUrl: string = 'http://52.23.167.187/predict/';

  constructor() {
    // You can leave the constructor empty or include additional setup logic if needed.
  }
}
