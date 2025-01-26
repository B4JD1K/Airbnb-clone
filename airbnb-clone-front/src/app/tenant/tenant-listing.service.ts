import {computed, inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CardListing, Listing} from "../landlord/model/listing.model";
import {State} from "../core/model/state.model";
import {createPaginationOption, Page, Pagination} from "../core/model/request.model";
import {CategoryName} from "../layout/navbar/category/category.model";
import {environment} from "../../environments/environment";
import {Subject} from "rxjs";
import {Search} from "./search/search.model";

@Injectable({
  providedIn: 'root'
})
export class TenantListingService {

  http = inject(HttpClient);

  private getAllByCategory$: WritableSignal<State<Page<CardListing>>> =
    signal(State.Builder<Page<CardListing>>().forInit());
  getAllByCategorySig = computed(() => this.getAllByCategory$());

  private getOneByPublicId$: WritableSignal<State<Listing>> =
    signal(State.Builder<Listing>().forInit());
  getOneByPublicIdSig = computed(() => this.getOneByPublicId$());

  /**
   * Powyższy Sygnał jest używany do przechowywania i zarządzania stanem reaktywnie.
   * Sygnały mogą być odczytywane, zapisywane, a także automatycznie powiadamiają subskrybentów o zmianachh stanu.
   * Przykładowo:
   * WritableSignal<State<Listing>> jest używany do zarządzania stanem pojedynczego ogłoszenia,
   * pozwalając komponentom na reaktywne aktualizowanie się, gdy stan się zmienia
   *
   * Natomiast poniższy Subject jest typem obserwowalnym (z RxJS), ale nie jest używany do przechowywania stanu.
   * Jest używany do emitowania wartości do wielu subskrybentów.
   * Działają zarówno jako obserwator, jak i obserwowany, co oznacza, że mogą emitowaćnowe wartości i być subsrybowane.
   * Przykładowo:
   * Subject<State<Page<CardListing>>> jest używany do emitowania stanu operacji wyszukiwania,
   * pozwalając wielu subskrybentom reagować na wyemitowane wartości.
   *
   * Podsumowanie:
   * WritableSignal jest używany do przechowywania i zarządzania stanem reaktywnie.
   * Subject jest używany do emitowania wartości do wielu subskrybentów.
   */

  private search$: Subject<State<Page<CardListing>>> =
    new Subject<State<Page<CardListing>>>();
  search = this.search$.asObservable();

  constructor() {
  }

  getAllByCategory(pageRequest: Pagination, category: CategoryName): void {
    let params = createPaginationOption(pageRequest);
    params = params.set("category", category);
    this.http.get<Page<CardListing>>(`${environment.API_URL}/tenant-listing/get-all-by-category`, {params})
      .subscribe({
        next: displayListingCart => this.getAllByCategory$.set(State.Builder<Page<CardListing>>().forSuccess(displayListingCart)),
        error: err => this.getAllByCategory$.set(State.Builder<Page<CardListing>>().forError(err))
      })
  }

  resetGetAllCategory(): void {
    this.getAllByCategory$.set(State.Builder<Page<CardListing>>().forInit());
  }

  getOneByPublicId(publicId: string): void {
    const params = new HttpParams().set("publicId", publicId);
    this.http.get<Listing>(`${environment.API_URL}/tenant-listing/get-one`, {params})
      .subscribe({
        next: listing => this.getOneByPublicId$.set(State.Builder<Listing>().forSuccess(listing)),
        error: err => this.getOneByPublicId$.set(State.Builder<Listing>().forError(err)),
      })
  }

  resetGetOneByPublicId(): void {
    this.getOneByPublicId$.set(State.Builder<Listing>().forInit());
  }

  searchListing(newSearch: Search, pageRequest: Pagination): void {
    const params = createPaginationOption(pageRequest);
    this.http.post<Page<CardListing>>(`${environment.API_URL}/tenant-listing/search`, newSearch, {params})
      .subscribe({
        next: displayListingCards => this.search$.next(State.Builder<Page<CardListing>>().forSuccess(displayListingCards)),
        error: err => this.search$.next(State.Builder<Page<CardListing>>().forError(err))
      })
  }
}
