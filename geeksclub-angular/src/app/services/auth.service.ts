import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { getApiBaseUrl} from '../../environments/environment';

export interface User {
  fullName: string;
  username: string;
  email: string;
  role: string;
}

export interface LoginResponse {
  accessToken: string;
  accessTokenExpiresAt: string;
  refreshToken: string;
  refreshTokenExpiresAt: string;
  fullName: string;
  username: string;
  email: string;
  role: string;
}

export interface RegisterRequest {
  fullName: string;
  username: string;
  email: string;
  password: string;
}

export interface RegisterResponse {
  fullName: string;
  username: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${ getApiBaseUrl()}/api`;
  private readonly TOKEN_KEY = 'geeksclub_auth_token';
  private readonly USER_KEY = 'geeksclub_auth_user';

  private readonly currentUser = signal<User | null>(this.loadUserFromStorage());
  private readonly token = signal<string | null>(this.loadTokenFromStorage());

  readonly isAuthenticated = computed(() => this.currentUser() !== null && this.token() !== null);
  readonly user = this.currentUser.asReadonly();

  private loadTokenFromStorage(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem(this.TOKEN_KEY);
    }
    return null;
  }

  private loadUserFromStorage(): User | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      const userJson = localStorage.getItem(this.USER_KEY);
      return userJson ? JSON.parse(userJson) : null;
    }
    return null;
  }

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, { email, password }).pipe(
      tap((response) => {
        this.setAuthData(response);
      })
    );
  }

  register(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.apiUrl}/users`, data);
  }

  private setAuthData(response: LoginResponse): void {
    const user: User = {
      fullName: response.fullName,
      username: response.username,
      email: response.email,
      role: response.role
    };

    this.token.set(response.accessToken);
    this.currentUser.set(user);

    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.setItem(this.TOKEN_KEY, response.accessToken);
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    }
  }

  logout(): void {
    this.token.set(null);
    this.currentUser.set(null);

    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.removeItem(this.TOKEN_KEY);
      localStorage.removeItem(this.USER_KEY);
    }
  }

  getToken(): string | null {
    return this.token();
  }
}
