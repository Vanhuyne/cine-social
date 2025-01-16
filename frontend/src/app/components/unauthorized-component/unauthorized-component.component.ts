import { Location } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-unauthorized-component',
  templateUrl: './unauthorized-component.component.html',
  styleUrl: './unauthorized-component.component.scss'
})
export class UnauthorizedComponentComponent {
  constructor(private location : Location) {
    
  }
  goBack() {
    this.location.back();
  }
}
