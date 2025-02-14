import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UpdateProfileResponse, UserProfile } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiUrl = 'http://localhost:8081/api/users';
  constructor(
    private http: HttpClient
  ) { }
  // Retrieve the current user's profile (adjust endpoint as needed)
  getCurrentUserProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/me`);
  }

  // Update the profile using a FormData object (multipart/form-data)
  updateProfile(formData: FormData): Observable<UpdateProfileResponse> {
    return this.http.put<UpdateProfileResponse>(`${this.apiUrl}/profile`, formData);
  }

  getProfileImageUrl(fileName: string): string {
    return `${this.apiUrl}/image/${fileName}`;
  }
}
