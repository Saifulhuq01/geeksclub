import { Component, ChangeDetectionStrategy, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink],
  templateUrl: './admin-home.component.html'
})
export class AdminHomeComponent implements OnInit {
  protected readonly stats = signal({
    totalUsers: 0,
    totalMessages: 0,
    totalVotes: 0,
    activeUsers: 0
  });

  protected readonly isLoading = signal(true);

  ngOnInit(): void {
    // Simulated loading - In real app, this would fetch from API
    setTimeout(() => {
      this.stats.set({
        totalUsers: 1234,
        totalMessages: 5678,
        totalVotes: 12345,
        activeUsers: 456
      });
      this.isLoading.set(false);
    }, 500);
  }
}
