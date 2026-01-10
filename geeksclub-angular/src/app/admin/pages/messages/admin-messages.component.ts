import { Component, ChangeDetectionStrategy, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MessageService, Message, PagedResponse } from '../../../services/message.service';

@Component({
  selector: 'app-admin-messages',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink],
  templateUrl: './admin-messages.component.html'
})
export class AdminMessagesComponent implements OnInit {
  private readonly messageService = inject(MessageService);

  protected readonly messages = signal<Message[]>([]);
  protected readonly currentPage = signal<number>(0);
  protected readonly totalPages = signal<number>(0);
  protected readonly totalElements = signal<number>(0);
  protected readonly pageSize = signal<number>(20);
  protected readonly isLoading = signal<boolean>(false);
  protected readonly error = signal<string | null>(null);
  protected readonly sortBy = signal<'recent' | 'upvoted' | 'downvoted'>('recent');

  ngOnInit(): void {
    this.loadMessages();
  }

  protected loadMessages(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.messageService.getMessages(
      this.currentPage(),
      this.pageSize(),
      this.sortBy()
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

  protected changeSortBy(sort: 'recent' | 'upvoted' | 'downvoted'): void {
    this.sortBy.set(sort);
    this.currentPage.set(0);
    this.loadMessages();
  }

  protected goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages()) {
      this.currentPage.set(page);
      this.loadMessages();
    }
  }

  protected previousPage(): void {
    if (this.currentPage() > 0) {
      this.goToPage(this.currentPage() - 1);
    }
  }

  protected nextPage(): void {
    if (this.currentPage() < this.totalPages() - 1) {
      this.goToPage(this.currentPage() + 1);
    }
  }

  protected formatDate(dateString: string): string {
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

  protected getPageNumbers(): number[] {
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
}
