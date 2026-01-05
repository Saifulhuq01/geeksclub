import { Component, ChangeDetectionStrategy, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  ngOnInit(): void {

  }
}
