import {computed, inject, Injectable, signal, WritableSignal} from '@angular/core';
import {HttpClient, HttpParams, HttpStatusCode} from "@angular/common/http";
import {Location} from "@angular/common";
import {Observable} from "rxjs";
import {State} from "../model/state.model";
import {User} from "../model/user.model";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  http = inject(HttpClient);
  location = inject(Location);
  notConnected = "NOT_CONNECTED";

  // reactive signal przechowujący stan użytkownika
  private fetchUser$: WritableSignal<State<User>> =
    // domyślny stan to użytkownik niezalogowany (email: nocConnected)
    signal(State.Builder<User>().forSuccess({email: this.notConnected}));
  fetchUser =
    computed(() => this.fetchUser$());


  // pobiera dane uwierzytelnionego użytkownika z backendu za pomocą fetchHttpUser
  fetch(forceResync: boolean): void {
    this.fetchHttpUser(forceResync)
      .subscribe({
        // jeśli żądanie się powiedzie ustawia stan użytkownika jako OK, po czym zapisuje jego dane
        next: user => this.fetchUser$.set(State.Builder<User>().forSuccess(user)),
        // jeśli 401, a użytkownik był zalogowany
        error: err => {
          if (err.status === HttpStatusCode.Unauthorized && this.isAuthenticated()) {
            // zmienia stan na niezalogowany
            this.fetchUser$.set(State.Builder<User>().forSuccess({email: this.notConnected}));
          } else {
            // w przypadku "innych" błędów - zapisuje błąd w stanie użytkownika
            this.fetchUser$.set(State.Builder<User>().forError(err));
          }
        }
      })
  }

  // wysłanie żądania GET do backendu, aby pobrać dane uwierzytelnionego użtkownika
  // forceResync - wymusza odświeżenie danych użytkownika
  fetchHttpUser(forceResync: boolean): Observable<User> {
    const params = new HttpParams().set('forceResync', forceResync);
    return this.http.get<User>(`${environment.API_URL}/auth/get-authenticated-user`, {params})
  }

  // przekierowuje na stronę logowania oauth2 (okta)
  login(): void {
    location.href = `${location.origin}${this.location.prepareExternalUrl("oauth2/authorization/okta")}`;
  }

  // wysyła żądanie w celu wylogowania użytkownika
  logout(): void {
    this.http.post(`${environment.API_URL}/auth/logout`, {})
      .subscribe({
        next: (response: any) => {
          this.fetchUser$.set(State.Builder<User>()
            // jeśli się powiedzie - stan użytkownika jako niezalogowany
            .forSuccess({email: this.notConnected}))
          // przekierowuje na podany w odpowiedzi adres
          location.href = response.logoutUrl
        }
      })
  }

  // sprawdzenie czy zalogowany
  isAuthenticated(): boolean {
    if (this.fetchUser$().value) {
      return this.fetchUser$().value!.email !== this.notConnected;
    } else {
      return false;
    }
  }

  // sprawdzenie czy posiada jedno z uprawnień 'authority'
  hasAnyAuthority(authorities: string[] | string): boolean {
    if (this.fetchUser$().value!.email === this.notConnected) {
      return false;
    }
    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }
    return this.fetchUser$().value!.authorities!
      .some((authority: string) => authorities.includes(authority));
  }
}
