import { Component, ChangeDetectionStrategy, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MessageService, Message, PagedResponse } from '../../services/message.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-user-messages',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-messages.component.html'
})
export class UserMessagesComponent implements OnInit {
  private readonly messageService = inject(MessageService);
  private readonly route = inject(ActivatedRoute);
  private readonly authService = inject(AuthService);

  readonly username = signal<string>('');
  readonly messages = signal<Message[]>([]);
  readonly currentPage = signal<number>(0);
  readonly totalPages = signal<number>(0);
  readonly totalElements = signal<number>(0);
  readonly pageSize = signal<number>(20);
  readonly isLoading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  readonly currentUser = this.authService.user;
  readonly showDeleteConfirmation = signal<boolean>(false);
  readonly messageToDelete = signal<number | null>(null);
  readonly isDeleting = signal<boolean>(false);
  readonly deleteError = signal<string | null>(null);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const username = params['username'];
      if (username) {
        this.username.set(username);
        this.currentPage.set(0);
        this.loadMessages();
      }
    });
  }

  loadMessages(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.messageService.getMessages(
      this.currentPage(),
      this.pageSize(),
      'recent',
      this.username()
    ).subscribe({
      next: (response: PagedResponse<Message>) => {
        this.messages.set(response.content);
        this.totalPages.set(response.totalPages);
        this.totalElements.set(response.totalElements);
        this.currentPage.set(response.number);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading messages:', err);
        this.error.set('Failed to load messages. Please try again later.');
        this.isLoading.set(false);
      }
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages()) {
      this.currentPage.set(page);
      this.loadMessages();
    }
  }

  previousPage(): void {
    if (this.currentPage() > 0) {
      this.goToPage(this.currentPage() - 1);
    }
  }

  nextPage(): void {
    if (this.currentPage() < this.totalPages() - 1) {
      this.goToPage(this.currentPage() + 1);
    }
  }

  getPageNumbers(): number[] {
    const total = this.totalPages();
    const current = this.currentPage();
    const pages: number[] = [];

    if (total <= 7) {
      for (let i = 0; i < total; i++) {
        pages.push(i);
      }
    } else {
      if (current < 4) {
        for (let i = 0; i < 5; i++) pages.push(i);
        pages.push(-1);
        pages.push(total - 1);
      } else if (current >= total - 4) {
        pages.push(0);
        pages.push(-1);
        for (let i = total - 5; i < total; i++) pages.push(i);
      } else {
        pages.push(0);
        pages.push(-1);
        for (let i = current - 1; i <= current + 1; i++) pages.push(i);
        pages.push(-1);
        pages.push(total - 1);
      }
    }

    return pages;
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const diffInMs = now.getTime() - date.getTime();
    const diffInMinutes = Math.floor(diffInMs / 60000);
    const diffInHours = Math.floor(diffInMs / 3600000);
    const diffInDays = Math.floor(diffInMs / 86400000);

    if (diffInMinutes < 1) {
      return 'Just now';
    } else if (diffInMinutes < 60) {
      return `${diffInMinutes}m ago`;
    } else if (diffInHours < 24) {
      return `${diffInHours}h ago`;
    } else if (diffInDays < 7) {
      return `${diffInDays}d ago`;
    } else {
      return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    }
  }

  canDeleteMessage(message: Message): boolean {
    const user = this.currentUser();
    return user !== null && user.username === message.author.username;
  }

  confirmDelete(messageId: number): void {
    this.messageToDelete.set(messageId);
    this.showDeleteConfirmation.set(true);
    this.deleteError.set(null);
  }

  cancelDelete(): void {
    this.showDeleteConfirmation.set(false);
    this.messageToDelete.set(null);
    this.deleteError.set(null);
  }

  deleteMessage(): void {
    const messageId = this.messageToDelete();
    if (!messageId) return;

    this.isDeleting.set(true);
    this.deleteError.set(null);

    this.messageService.deleteMessage(messageId).subscribe({
      next: () => {
        this.isDeleting.set(false);
        this.cancelDelete();
        this.loadMessages();
      },
      error: (err) => {
        console.error('Error deleting message:', err);
        this.deleteError.set(err.error?.message || 'Failed to delete message. Please try again.');
        this.isDeleting.set(false);
      }
    });
  }
}
