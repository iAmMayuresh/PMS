import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PensionDetail } from './pension-detail';
import { PensionerInput } from './pensioner-input';
import { ProcessPensionInput } from './process-pension-input';
import { ProcessPensionResponse } from './process-pension-response';

@Injectable({
  providedIn: 'root'
})
export class ProcessPensionService {

  // add your base URL here
  baseUrl: string = 'http://localhost:8082'

  constructor(private http: HttpClient) { }

  // Method to process the pension
  processPension(processPensionInput: ProcessPensionInput): Observable<ProcessPensionResponse> {
    return this.http.post<ProcessPensionResponse>(`${this.baseUrl}/processPension`, processPensionInput);
  }

  // Method to get pension details
  getPensionDetails(pensionerInput: PensionerInput): Observable<PensionDetail> {
    return this.http.post<PensionDetail>(`${this.baseUrl}/pensionerInput`, pensionerInput);
  }

}