import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { RegisterRequest } from '../../models/auth/RegisterRequest';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  @Output() closeModal = new EventEmitter<void>();
  @Output() openLogin = new EventEmitter<void>();

  registerForm !: FormGroup;
  isLoading = false;
  errorMessage : string | null = null;
  sucessMessage : string | null = null;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      retypePassword: ['', [Validators.required, Validators.minLength(6)]]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    return form.get('password')!.value === form.get('retypePassword')!.value ? null : { mismatch: true };
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
      this.isLoading = true;
      this.errorMessage = '';
      this.sucessMessage = '';

      const registerData : RegisterRequest = {
        username: this.registerForm.value.username,
        email: this.registerForm.value.email,
        password: this.registerForm.value.password
      }
      this.authService.register(registerData).subscribe({
        next: (userDTO) => {
          this.sucessMessage = 'Registration successful';
          this.isLoading = false;
          this.registerForm.reset();
        },
        error: (error) => {
          console.error('Registration error:', error);
          this.isLoading = false;
          this.errorMessage = error.error.message || 'An error occurred during registration';
        },
      });
    
  }

  openLoginForm() {
    this.openLogin.emit();
  }
  onClose() {
    this.closeModal.emit();
  }
}
