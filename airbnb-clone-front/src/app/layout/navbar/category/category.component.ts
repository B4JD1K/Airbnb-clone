import {Component, inject, OnInit} from '@angular/core';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {CategoryService} from "./category.service";
import {Category} from "./category.model";

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [
    FontAwesomeModule
  ],
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss'
})
export class CategoryComponent implements OnInit {

  // Dependency Injection DP - wstrzyknięcie zależności do uzyskania instancji kategorii serwisu
  // dzięki temu komponent może korzystać z metod i danych CategoryService.
  categoryService = inject(CategoryService);

  // początkowo undefined, ale dzięki fetchCategories() i zainicjowaniu ngOnInit() kategorie zostają przypisane
  categories: Category[] | undefined;

  currentActivateCategory = this.categoryService.getCategoryByDefault();

  ngOnInit() {
    this.fetchCategories();
  }

  // pobiera listę kategorii i przypisuje ją do this.categories
  private fetchCategories() {
    this.categories = this.categoryService.getCategories();
  }
}
