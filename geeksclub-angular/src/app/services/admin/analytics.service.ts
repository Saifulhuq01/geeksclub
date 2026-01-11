import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { getApiBaseUrl } from '../../../environments/environment';

export interface AnalyticsOverview {
  totalUsers: number;
  activeUsersLast7Days: number;
  totalMessages: number;
  messagesLast24Hours: number;
  totalVotes: number;
  votesLast24Hours: number;
  spamDetected: number;
  spamRate: number;
  timestamp: string;
}

export interface DailyStatistic {
  date: string;
  messageCount: number;
  activeUsers: number;
  spamCount: number;
  totalVotes: number;
}

export interface DailyAnalyticsResponse {
  statistics: DailyStatistic[];
  period: {
    startDate: string;
    endDate: string;
    days: number;
  };
}

export interface ActiveUser {
  userId: number;
  fullName: string;
  username: string;
  email: string;
  messageCount: number;
  voteCount: number;
  totalActivity: number;
  lastActivityAt: string;
  joinedAt: string;
}

export interface ActiveUsersResponse {
  users: ActiveUser[];
  limit: number;
}

export interface TrendingMessageAuthor {
  id: number;
  fullName: string;
  username: string;
}

export interface TrendingMessage {
  id: number;
  content: string;
  author: TrendingMessageAuthor;
  upvoteCount: number;
  downvoteCount: number;
  score: number;
  recentVotes: number;
  createdAt: string;
}

export interface TrendingMessagesResponse {
  messages: TrendingMessage[];
  period: {
    days: number;
    startDate: string;
  };
}

export interface SpamByDate {
  date: string;
  totalMessages: number;
  spamCount: number;
  spamRate: number;
}

export interface FlaggedMessage {
  id: number;
  content: string;
  spamConfidence: number;
  status: string;
  createdAt: string;
}

export interface SpamStatistics {
  totalMessages: number;
  spamDetected: number;
  spamRate: number;
  averageConfidence: number;
  byDate: SpamByDate[];
  flaggedMessages: FlaggedMessage[];
}

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${getApiBaseUrl()}/api/admin/analytics`;

  getOverview(): Observable<AnalyticsOverview> {
    return this.http.get<AnalyticsOverview>(`${this.apiUrl}/overview`);
  }

  getDailyStatistics(days: number = 30): Observable<DailyAnalyticsResponse> {
    return this.http.get<DailyAnalyticsResponse>(`${this.apiUrl}/daily?days=${days}`);
  }

  getActiveUsers(limit: number = 20): Observable<ActiveUsersResponse> {
    return this.http.get<ActiveUsersResponse>(`${this.apiUrl}/users/active?limit=${limit}`);
  }

  getTrendingMessages(limit: number = 20, days: number = 7): Observable<TrendingMessagesResponse> {
    return this.http.get<TrendingMessagesResponse>(`${this.apiUrl}/messages/trending?limit=${limit}&days=${days}`);
  }

  getSpamStatistics(): Observable<SpamStatistics> {
    return this.http.get<SpamStatistics>(`${this.apiUrl}/spam`);
  }
}
