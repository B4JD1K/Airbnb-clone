import {HttpErrorResponse} from '@angular/common/http';

export type StatusNotification = 'OK' | 'ERROR' | 'INIT';

export class State<T, V = HttpErrorResponse> {
  value?: T;
  error?: V | HttpErrorResponse; // pozwala aby błąd był V ("własny" typ generyczny) lub HttpErrorResponse
  status: StatusNotification;

  // utworzy obiekt stanu
  constructor(status: StatusNotification, value?: T, error?: V | HttpErrorResponse) {
    this.value = value;
    this.error = error;
    this.status = status;
  }

  // zwraca nową instancję klasy pomocniczej 'StateBuilder'
  // dzięki niej w wygodniejszy sposób będzie się budowało obiekty 'State'
  static Builder<T = any, V = HttpErrorResponse>() {
    return new StateBuilder<T, V>();
  }
}

// tworzenie obieków stanu 'State' odpowiedzi API (czy sukces, czy błąd, czy dopiero inicjuje)
class StateBuilder<T, V = HttpErrorResponse> {
  private status: StatusNotification = 'INIT';
  private value?: T;
  private error?: V | HttpErrorResponse; // jw. - pozwala aby błąd był V lub HttpErrorResponse

  public forSuccess(value: T): State<T, V> {
    this.value = value;
    return new State<T, V>('OK', this.value, this.error);
  }

  public forError(error: V | HttpErrorResponse = new HttpErrorResponse({ error: 'Unknown Error' }), value?: T): State<T, V> {
    this.value = value;
    this.error = error;
    return new State<T, V>('ERROR', this.value, this.error);
  }

  public forInit(): State<T, V> {
    return new State<T, V>('INIT', this.value, this.error);
  }
}
