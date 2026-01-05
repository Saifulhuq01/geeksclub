import { Component, ChangeDetectionStrategy, signal, inject, computed, ElementRef } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';

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

  private readonly router = inject(Router);
  private readonly elementRef = inject(ElementRef);
  protected readonly isDropdownOpen = signal(false);

  protected readonly isAdmin = computed(() => {
    return true;
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
    this.closeDropdown();
    this.router.navigate(['/']);
  }

  protected isAuthenticated(): boolean {
    return true;
  }

  protected getUserInitials(): string {
    const userFullName = "Siva Katamreddy";
    const names = userFullName.split(' ');
    if (names.length >= 2) {
      return `${names[0].charAt(0)}${names[names.length - 1].charAt(0)}`.toUpperCase();
    }
    return userFullName.charAt(0).toUpperCase();
  }
}
