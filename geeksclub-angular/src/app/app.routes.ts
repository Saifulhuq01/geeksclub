import { Routes } from '@angular/router';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./layout/layout.component').then(m => m.LayoutComponent),
    children: [
      {
        path: '',
        loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
      },
      {
        path: 'login',
        loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
      },
      {
        path: 'register',
        loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
      },
      {
        path: 'profile',
        loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent)
      },
      {
        path: 'user/:username',
        loadComponent: () => import('./pages/user-messages/user-messages.component').then(m => m.UserMessagesComponent)
      }
    ]
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./admin/dashboard-layout/dashboard-layout.component').then(m => m.DashboardLayoutComponent),
    canActivate: [adminGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./admin/pages/home/admin-home.component').then(m => m.AdminHomeComponent)
      },
      {
        path: 'messages',
        loadComponent: () => import('./admin/pages/messages/admin-messages.component').then(m => m.AdminMessagesComponent)
      }
    ]
  }
];
