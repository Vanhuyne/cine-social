import { Component, Input } from '@angular/core';
import { SearchService } from '../../../service/search.service';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { Router } from '@angular/router';


@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent {
  searchQuery: string = '';
  private searchSubject = new Subject<string>();

  // Allow customization of input styling based on where it's used
  @Input() inputClass: string = 'w-full bg-gray-800/60 rounded-full px-6 py-3 border border-gray-700 focus:outline-none focus:border-cyan-500';
  @Input() isHeader: boolean = false;
  constructor(
    private searchService: SearchService,
    private router: Router
  ) { }

  ngOnInit() {
    // Debounce search to avoid too many API calls
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(query => {
      // this.loading = true;
      this.searchService.updateSearchQuery(query);
      // this.loading = false;
    });
  }

  clearSearch() {
    this.searchQuery = '';
    this.searchService.clearSearch();
    if (this.isHeader) {
      this.router.navigate(['/explore'], {
        queryParams: {
          page: 1
        }
      });
    }
  }
  onExplore() {
    if (this.isHeader) {
      this.router.navigate(['/explore'], {
        queryParams: {
          query: this.searchQuery || null,
          page: 1
        }
      });
    } else {
      this.searchService.updateSearchQuery(this.searchQuery);
    }
  }

}
