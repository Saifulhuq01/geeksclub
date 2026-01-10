import { Component, ChangeDetectionStrategy, OnInit, signal, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MessageService, Message, PagedResponse } from '../../services/message.service';
import { AuthService } from '../../services/auth.service';
import { UserService, UserProfile } from '../../services/user.service';

@Component({
  selector: 'app-user-messages',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-messages.component.html'
})
export class UserMessagesComponent implements OnInit {
  private readonly messageService = inject(MessageService);
  private readonly userService = inject(UserService);
  private readonly route = inject(ActivatedRoute);
  private readonly authService = inject(AuthService);

  readonly username = signal<string>('');
  readonly userProfile = signal<UserProfile | null>(null);
  readonly isLoadingProfile = signal<boolean>(false);
  readonly profileError = signal<string | null>(null);
  readonly messages = signal<Message[]>([]);
  readonly currentPage = signal<number>(0);
  readonly totalPages = signal<number>(0);
  readonly totalElements = signal<number>(0);
  readonly pageSize = signal<number>(20);
  readonly isLoading = signal<boolean>(false);
  readonly error = signal<string | null>(null);

  readonly userInitials = computed(() => {
    const profile = this.userProfile();
    if (!profile) {
      const username = this.username();
      return username ? username.charAt(0).toUpperCase() : '';
    }
    const names = profile.fullName.split(' ');
    if (names.length >= 2) {
      return `${names[0].charAt(0)}${names[names.length - 1].charAt(0)}`.toUpperCase();
    }
    return profile.fullName.charAt(0).toUpperCase();
  });

  readonly currentUser = this.authService.user;
  readonly isAuthenticated = this.authService.isAuthenticated;
  readonly showDeleteConfirmation = signal<boolean>(false);
  readonly messageToDelete = signal<number | null>(null);
  readonly isDeleting = signal<boolean>(false);
  readonly deleteError = signal<string | null>(null);

  readonly votingMessageId = signal<number | null>(null);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const username = params['username'];
      if (username) {
        this.username.set(username);
        this.currentPage.set(0);
        this.loadUserProfile();
        this.loadMessages();
      }
    });
  }

  loadUserProfile(): void {
    this.isLoadingProfile.set(true);
    this.profileError.set(null);

    this.userService.getUserProfile(this.username()).subscribe({
      next: (profile: UserProfile) => {
        this.userProfile.set(profile);
        this.isLoadingProfile.set(false);
      },
      error: (err) => {
        console.error('Error loading user profile:', err);
        this.profileError.set('Failed to load user profile.');
        this.isLoadingProfile.set(false);
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

  formatJoinDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }
}
