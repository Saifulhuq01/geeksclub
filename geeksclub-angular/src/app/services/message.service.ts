import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { getApiBaseUrl } from '../../environments/environment';

export interface Author {
  id: number;
  username: string;
  fullName?: string;
}

export interface Votes {
  upvoteCount: number;
  downvoteCount: number;
  score: number;
}

export type VoteType = 'UP' | 'DOWN' | null;

export interface Message {
  id: number;
  content: string;
  author: Author;
  status: string;
  isSpam: boolean;
  votes: Votes;
  userVote: VoteType;
  createdAt: string;
  updatedAt?: string;
}

export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort?: string;
  offset: number;
}

export interface PagedResponse<T> {
  content: T[];
  pageable: Pageable;
  totalPages: number;
  totalElements: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  numberOfElements: number;
  empty: boolean;
}

export type SortOption = 'recent' | 'upvoted' | 'downvoted' | 'trending';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${getApiBaseUrl()}/api`;

  getMessages(page: number = 0, size: number = 20, sort: SortOption = 'recent', username?: string): Observable<PagedResponse<Message>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (username) {
      params = params.set('user', username);
    }

    return this.http.get<PagedResponse<Message>>(`${this.apiUrl}/messages`, { params });
  }

  createMessage(content: string): Observable<Message> {
    return this.http.post<Message>(`${this.apiUrl}/messages`, { content });
  }

  deleteMessage(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/messages/${id}`);
  }

  voteOnMessage(messageId: number, voteType: 'UP' | 'DOWN'): Observable<any> {
    return this.http.post(`${this.apiUrl}/messages/${messageId}/vote`, { voteType });
  }

  removeVote(messageId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/messages/${messageId}/vote`);
  }

  searchMessages(query: string, page: number = 0, size: number = 20): Observable<PagedResponse<Message>> {
    let params = new HttpParams()
      .set('q', query)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PagedResponse<Message>>(`${this.apiUrl}/messages/search`, { params });
  }
}
