import { Component, EventEmitter, Output } from '@angular/core';
import { MovieService } from '../../../service/movie.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent {
  @Output() search = new EventEmitter<string>();
  searchQuery : string = '';

  onInput(): void {
    this.search.emit(this.searchQuery.trim());
  }

  clearSearch(): void {
    this.searchQuery = '';
    this.search.emit('');
  }
 
}
