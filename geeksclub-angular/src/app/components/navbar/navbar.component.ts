import { Component, ChangeDetectionStrategy, signal, inject, computed, ElementRef } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  host: {
    '(document:click)': 'onDocumentClick($event)'
  }
})
export class NavbarComponent {
  protected readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly elementRef = inject(ElementRef);
  protected readonly isDropdownOpen = signal(false);

  protected readonly isAdmin = computed(() => {
    const user = this.authService.user();
    return user?.role === 'ADMIN';
  });

  protected onDocumentClick(event: MouseEvent): void {
    const clickedInside = this.elementRef.nativeElement.contains(event.target);
    if (!clickedInside && this.isDropdownOpen()) {
      this.closeDropdown();
    }
  }

  protected toggleDropdown(): void {
    this.isDropdownOpen.update(value => !value);
  }

  protected closeDropdown(): void {
    this.isDropdownOpen.set(false);
  }

  protected logout(): void {
    this.authService.logout();
    this.closeDropdown();
    this.router.navigate(['/']);
  }

  protected getUserInitials(): string {
    const user = this.authService.user();
    if (!user) return '';

    const names = user.fullName.split(' ');
    if (names.length >= 2) {
      return `${names[0].charAt(0)}${names[names.length - 1].charAt(0)}`.toUpperCase();
    }
    return user.fullName.charAt(0).toUpperCase();
  }
}
