import { Component, ChangeDetectionStrategy, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import {
  AnalyticsService,
  AnalyticsOverview,
  DailyStatistic,
  ActiveUser,
  TrendingMessage,
  SpamStatistics
} from '../../../services/admin/analytics.service';

@Component({
  selector: 'app-admin-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink],
  templateUrl: './admin-home.component.html'
})
export class AdminHomeComponent implements OnInit {
  private readonly analyticsService = inject(AnalyticsService);

  protected readonly overview = signal<AnalyticsOverview | null>(null);
  protected readonly todayStats = signal<DailyStatistic | null>(null);
  protected readonly activeUsers = signal<ActiveUser[]>([]);
  protected readonly trendingMessages = signal<TrendingMessage[]>([]);
  protected readonly spamStats = signal<SpamStatistics | null>(null);

  protected readonly isLoading = signal(true);
  protected readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadAnalytics();
  }

  private loadAnalytics(): void {
    this.isLoading.set(true);
    this.error.set(null);

    forkJoin({
      overview: this.analyticsService.getOverview(),
      dailyStats: this.analyticsService.getDailyStatistics(1),
      activeUsers: this.analyticsService.getActiveUsers(10),
      trendingMessages: this.analyticsService.getTrendingMessages(10, 7),
      spamStats: this.analyticsService.getSpamStatistics()
    }).subscribe({
      next: (data) => {
        this.overview.set(data.overview);

        // Get today's stats (first item in the daily stats array)
        if (data.dailyStats.statistics.length > 0) {
          this.todayStats.set(data.dailyStats.statistics[0]);
        }

        this.activeUsers.set(data.activeUsers.users);
        this.trendingMessages.set(data.trendingMessages.messages);
        this.spamStats.set(data.spamStats);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading analytics:', err);
        this.error.set('Failed to load analytics data. Please try again later.');
        this.isLoading.set(false);
      }
    });
  }

  protected formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    });
  }

  protected formatDateTime(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  protected truncateContent(content: string, maxLength: number = 150): string {
    if (content.length <= maxLength) return content;
    return content.substring(0, maxLength) + '...';
  }

  protected getPercentage(value: number): string {
    return (value * 100).toFixed(2) + '%';
  }
}
