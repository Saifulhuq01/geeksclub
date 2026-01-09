import { Component, ChangeDetectionStrategy, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router, ActivatedRoute } from '@angular/router';
import { MessageService, Message, PagedResponse, SortOption } from '../../services/message.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  private readonly messageService = inject(MessageService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly messages = signal<Message[]>([]);
  readonly currentPage = signal<number>(0);
  readonly totalPages = signal<number>(0);
  readonly totalElements = signal<number>(0);
  readonly pageSize = signal<number>(20);
  readonly currentSort = signal<SortOption>('recent');
  readonly isLoading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  readonly isAuthenticated = this.authService.isAuthenticated;
  readonly currentUser = this.authService.user;
  readonly showPostMessageForm = signal<boolean>(false);
  readonly newMessageContent = signal<string>('');
  readonly isSubmitting = signal<boolean>(false);
  readonly submitError = signal<string | null>(null);

  readonly showDeleteConfirmation = signal<boolean>(false);
  readonly messageToDelete = signal<number | null>(null);
  readonly isDeleting = signal<boolean>(false);
  readonly deleteError = signal<string | null>(null);

  readonly votingMessageId = signal<number | null>(null);

  readonly searchQuery = signal<string>('');
  readonly isSearchMode = signal<boolean>(false);

  readonly sortOptions: Array<{ value: SortOption; label: string }> = [
    { value: 'recent', label: 'Most Recent' },
    { value: 'upvoted', label: 'Most Upvoted' },
    { value: 'downvoted', label: 'Most Downvoted' }
  ];

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const query = params['q'] || '';
      this.searchQuery.set(query);
      this.isSearchMode.set(!!query);
      this.currentPage.set(0);
      this.loadMessages();
    });
  }

  loadMessages(): void {
    this.isLoading.set(true);
    this.error.set(null);

    const query = this.searchQuery();
    const observable = query
      ? this.messageService.searchMessages(query, this.currentPage(), this.pageSize())
      : this.messageService.getMessages(this.currentPage(), this.pageSize(), this.currentSort());

    observable.subscribe({
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

  onSortChange(sort: SortOption): void {
    this.currentSort.set(sort);
    this.currentPage.set(0);
    this.loadMessages();
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

  openPostMessageForm(): void {
    this.showPostMessageForm.set(true);
    this.newMessageContent.set('');
    this.submitError.set(null);
  }

  closePostMessageForm(): void {
    this.showPostMessageForm.set(false);
    this.newMessageContent.set('');
    this.submitError.set(null);
  }

  submitMessage(): void {
    const content = this.newMessageContent().trim();

    if (!content) {
      this.submitError.set('Message content cannot be empty');
      return;
    }

    if (content.length > 5000) {
      this.submitError.set('Message content must not exceed 5000 characters');
      return;
    }

    this.isSubmitting.set(true);
    this.submitError.set(null);

    this.messageService.createMessage(content).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.closePostMessageForm();
        this.currentPage.set(0);
        this.currentSort.set('recent');
        this.loadMessages();
      },
      error: (err) => {
        console.error('Error creating message:', err);
        this.submitError.set(err.error?.message || 'Failed to create message. Please try again.');
        this.isSubmitting.set(false);
      }
    });
  }

  canDeleteMessage(message: Message): boolean {
    const user = this.currentUser();
    return user !== null && user.username === message.author.username;
  }

  isOwnMessage(message: Message): boolean {
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

  handleUpvote(message: Message): void {
    if (!this.isAuthenticated() || this.votingMessageId()) return;

    const messageId = message.id;
    this.votingMessageId.set(messageId);

    // If already upvoted, remove the vote
    if (message.userVote === 'UP') {
      this.messageService.removeVote(messageId).subscribe({
        next: (response) => {
          this.updateMessageVotes(messageId, response.votes, null);
          this.votingMessageId.set(null);
        },
        error: (err) => {
          console.error('Error removing vote:', err);
          this.votingMessageId.set(null);
        }
      });
    } else {
      // Add or change to upvote
      this.messageService.voteOnMessage(messageId, 'UP').subscribe({
        next: (response) => {
          this.updateMessageVotes(messageId, response.votes, 'UP');
          this.votingMessageId.set(null);
        },
        error: (err) => {
          console.error('Error voting:', err);
          this.votingMessageId.set(null);
        }
      });
    }
  }

  handleDownvote(message: Message): void {
    if (!this.isAuthenticated() || this.votingMessageId()) return;

    const messageId = message.id;
    this.votingMessageId.set(messageId);

    // If already downvoted, remove the vote
    if (message.userVote === 'DOWN') {
      this.messageService.removeVote(messageId).subscribe({
        next: (response) => {
          this.updateMessageVotes(messageId, response.votes, null);
          this.votingMessageId.set(null);
        },
        error: (err) => {
          console.error('Error removing vote:', err);
          this.votingMessageId.set(null);
        }
      });
    } else {
      // Add or change to downvote
      this.messageService.voteOnMessage(messageId, 'DOWN').subscribe({
        next: (response) => {
          this.updateMessageVotes(messageId, response.votes, 'DOWN');
          this.votingMessageId.set(null);
        },
        error: (err) => {
          console.error('Error voting:', err);
          this.votingMessageId.set(null);
        }
      });
    }
  }

  private updateMessageVotes(messageId: number, votes: any, userVote: 'UP' | 'DOWN' | null): void {
    const messages = this.messages();
    const updatedMessages = messages.map(msg =>
      msg.id === messageId
        ? { ...msg, votes, userVote }
        : msg
    );
    this.messages.set(updatedMessages);
  }

  isVoting(messageId: number): boolean {
    return this.votingMessageId() === messageId;
  }

  performSearch(query: string): void {
    if (!query.trim()) {
      this.clearSearch();
      return;
    }

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { q: query.trim() },
      queryParamsHandling: 'merge'
    });
  }

  clearSearch(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { q: null },
      queryParamsHandling: 'merge'
    });
  }
}
