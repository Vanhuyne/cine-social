import { Component } from '@angular/core';
import { UpdateProfileResponse, UserProfile } from '../../models/User';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  profileForm: FormGroup;
  selectedFile: File | null = null;
  profileImageUrl: string | null = null;

  constructor(private fb: FormBuilder,
    private userService: UserService) {
    // Create the reactive form with validation rules
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      // Note: The file input is handled separately
    });
  }

  ngOnInit(): void {
    // Load the current user profile
    this.userService.getCurrentUserProfile().subscribe(
      (user: UserProfile) => {
        this.profileForm.patchValue({
          username: user.username,
          email: user.email,
        });
        // If a profile picture exists, construct the image URL
        if (user.profilePicture) {
          this.profileImageUrl = this.userService.getProfileImageUrl(user.profilePicture);
        }},
      (error) => {
        console.error('Error loading user profile:', error);
      }
    );
  }

  // Handle file selection and preview the image
  onFileSelected(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    if (element.files && element.files[0]) {
      this.selectedFile = element.files[0];

      const reader = new FileReader();
      reader.onload = () => (this.profileImageUrl = reader.result as string);
      reader.readAsDataURL(this.selectedFile);
    }
  }

  // Submit the profile update form
  onSubmit(): void {
    if (this.profileForm.invalid) {
      return;
    }

    const formData = new FormData();
    formData.append('username', this.profileForm.get('username')?.value);
    formData.append('email', this.profileForm.get('email')?.value);
    if (this.selectedFile) {
      formData.append('profilePicture', this.selectedFile);
    }

    // Call the service to update the profile
    this.userService.updateProfile(formData).subscribe(
      (response: UpdateProfileResponse) => {
        // Update tokens (if you store them in localStorage or a service)
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
        alert(response.message);
      },
      (error) => {
        console.error('Failed to update profile:', error);
        alert('Failed to update profile. Please try again.');
      }
    );
  }
}
