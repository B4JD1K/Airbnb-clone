import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {Message} from "primeng/api";

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  INIT_STATE = "INIT";

  // dzięki BehaviorSubject<> przechowywana jest ostatnia wiadomości toast
  private send$ = new BehaviorSubject<Message>({summary: this.INIT_STATE});
  // dzięki .asObservable() można podpiąć się subskrybcją do obserwowania strumienia (listenToastService)
  sendSub=this.send$.asObservable();

  public send(message:Message):void{
    // aktualizuje strumień z nową wiadomością
    this.send$.next(message);
  }
}
