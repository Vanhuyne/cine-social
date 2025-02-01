import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrl: './callback.component.scss'
})
export class CallbackComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router : Router,
    private route : ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      const state = params['state'];
      const error = params['error'];

      if (error) {
        console.error('Error during authentication: ', error);
        this.router.navigate(['/']);
        return;
      }
      const savedState = localStorage.getItem('oauth_state');
      if (state !== savedState) {
        // console.error('State does not match');
        this.router.navigate(['/'], { queryParams: { error: 'invalid_state' } });
        return;
      }

      this.authService.exchangeCodeForToken(code).subscribe({
        next: () => this.router.navigate(['/home']),
        error: (err) => {
          console.error('Error exchanging code for token: ', err.error.message);
          this.router.navigate(['/login'], { queryParams: { error: err.error.message } });
        }
      });
    });
  }
}
