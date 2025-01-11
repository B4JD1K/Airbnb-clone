import {Component, input} from '@angular/core';
import {NgClass} from "@angular/common";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";

@Component({
  selector: 'app-avatar',
  standalone: true,
  imports: [
    NgClass,
    FontAwesomeModule
  ],
  templateUrl: './avatar.component.html',
  styleUrl: './avatar.component.scss'
})
export class AvatarComponent {
  // url avatara użytkownika - jeśli pusty lub null to ustawiona zostanie domyślna ikona
  imageUrl = input<string>();
  // rozmiar avatara, przykazywany jako klasa do CSS
  avatarSize = input<"avatar-sm" | "avatar-xl">();
}
