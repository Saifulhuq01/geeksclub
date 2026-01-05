import { Component, ChangeDetectionStrategy, signal, inject, OnInit, computed } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService, UserProfile, UpdateUserRequest } from '../../services/user.service';

@Component({
  selector: 'app-profile',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly userService = inject(UserService);
  private readonly router = inject(Router);

  protected readonly userProfile = signal<UserProfile | null>(null);
  protected readonly isLoading = signal(true);
  protected readonly isSaving = signal(false);
  protected readonly isEditMode = signal(false);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly successMessage = signal<string | null>(null);

  protected readonly profileForm: FormGroup;

  protected readonly userInitials = computed(() => {
    const profile = this.userProfile();
    if (!profile) return '';

    const names = profile.fullName.split(' ');
    if (names.length >= 2) {
      return `${names[0].charAt(0)}${names[names.length - 1].charAt(0)}`.toUpperCase();
    }
    return profile.fullName.charAt(0).toUpperCase();
  });

  constructor() {
    this.profileForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(2)]],
      username: ['', [Validators.required, Validators.minLength(3)]],
    });

    // Disable form initially
    this.profileForm.disable();
  }

  ngOnInit(): void {
    const currentUser = this.authService.user();
    if (!currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadProfile(currentUser.username);
  }

  private loadProfile(username: string): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.userService.getUserProfile(username).subscribe({
      next: (profile) => {
        this.userProfile.set(profile);
        this.populateForm(profile);
        this.isLoading.set(false);
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorMessage.set('Failed to load profile. Please try again.');
        console.error('Profile load error:', error);
      }
    });
  }

  private populateForm(profile: UserProfile): void {
    this.profileForm.patchValue({
      fullName: profile.fullName,
      username: profile.username
    });
  }

  protected enableEditMode(): void {
    this.isEditMode.set(true);
    this.profileForm.enable();
    // Username should remain disabled
    this.profileForm.get('username')?.disable();
    this.successMessage.set(null);
    this.errorMessage.set(null);
  }

  protected cancelEdit(): void {
    this.isEditMode.set(false);
    this.profileForm.disable();
    const profile = this.userProfile();
    if (profile) {
      this.populateForm(profile);
    }
    this.errorMessage.set(null);
  }

  protected onSubmit(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const profile = this.userProfile();
    if (!profile) return;

    this.isSaving.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    const formValue = this.profileForm.getRawValue();
    const updateData: UpdateUserRequest = {
      fullName: formValue.fullName
    };

    this.userService.updateUserProfile(profile.username, updateData).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.isEditMode.set(false);
        this.profileForm.disable();
        this.successMessage.set('Profile updated successfully!');

        // Reload profile to get fresh data
        this.loadProfile(profile.username);

        // Clear success message after 3 seconds
        setTimeout(() => {
          this.successMessage.set(null);
        }, 3000);
      },
      error: (error) => {
        this.isSaving.set(false);
        if (error.status === 403) {
          this.errorMessage.set('You do not have permission to update this profile.');
        } else if (error.status === 400) {
          this.errorMessage.set('Invalid profile data. Please check your inputs.');
        } else {
          this.errorMessage.set('Failed to update profile. Please try again.');
        }
        console.error('Profile update error:', error);
      }
    });
  }

  protected getFieldError(fieldName: string): string | null {
    const field = this.profileForm.get(fieldName);
    if (!field || !field.touched || !field.errors) {
      return null;
    }

    if (field.errors['required']) {
      return `${this.capitalize(fieldName)} is required.`;
    }
    if (field.errors['minlength']) {
      return `${this.capitalize(fieldName)} must be at least ${field.errors['minlength'].requiredLength} characters.`;
    }
    if (field.errors['maxlength']) {
      return `${this.capitalize(fieldName)} must not exceed ${field.errors['maxlength'].requiredLength} characters.`;
    }
    if (field.errors['pattern']) {
      return 'Please enter a valid phone number.';
    }

    return null;
  }

  private capitalize(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  protected formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }
}
