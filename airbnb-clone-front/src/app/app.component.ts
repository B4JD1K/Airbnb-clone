import {Component, inject, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NavbarComponent} from "./layout/navbar/navbar.component";
import {FooterComponent} from "./layout/footer/footer.component";
import {ToastModule} from "primeng/toast";
import {FaIconLibrary, FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {fontAwesomeIcons} from "./shared/font-awesome-icons";
import {ToastService} from "./layout/toast.service";
import {MessageService} from "primeng/api";
import {ButtonModule} from "primeng/button";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ButtonModule, FontAwesomeModule, NavbarComponent, FooterComponent, ToastModule],
  providers:[MessageService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  // wstrzykiwanie zależności serwisów
  faIconLibrary = inject(FaIconLibrary);
  toastService = inject(ToastService);
  messageService = inject(MessageService);

  isListingView = true;

  // z inicjalizacją komponentu wywoływane są te metody
  ngOnInit(): void {
    this.initFontAwesome();
    this.listenToastService();
  }

  private initFontAwesome() {
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }

  // metoda 'nasłuchująca' komunikaty/popupy "Toast"
  private listenToastService() {
    // subskrybuje strumień 'sendSub' z serwisu 'ToastService'
    this.toastService.sendSub.subscribe({
      // odbiera wiadomości
      next: newMessage => {
        // i jeżeli wiadomość różni się od początkowego 'INIT_STATE' to przekazuje je do 'MessageService'
        if (newMessage && newMessage.summary !== this.toastService.INIT_STATE) {
          this.messageService.add(newMessage);
        }
      }
    })
  }
}
