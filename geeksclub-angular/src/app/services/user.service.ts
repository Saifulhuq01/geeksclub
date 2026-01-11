import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { getApiBaseUrl} from '../../environments/environment';

export interface UserProfile {
  id: number;
  fullName: string;
  username: string;
  email?: string;
  role: string;
  createdAt: string;
  stats?: {
    messageCount: number;
    voteCount: number;
  };
}

export interface UpdateUserRequest {
  fullName: string;
}

export interface UserSummary {
  id: number;
  fullName: string;
  username: string;
  email: string;
}

export interface UsersResponse {
  users: UserSummary[];
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${ getApiBaseUrl()}/api`;

  getUserProfile(username: string): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/users/${username}`);
  }

  updateUserProfile(username: string, data: UpdateUserRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/users/${username}`, data);
  }
}
