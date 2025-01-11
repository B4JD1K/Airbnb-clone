import {IconName} from "@fortawesome/free-brands-svg-icons";

// nazwy/typy kategorii dla serwisu
export type CategoryName = "ALL" | "AMAZING_VIEWS" | "OMG" | "TREEHOUSES"
  | "BEACH" | "FARMS" | "TINY_HOMES" | "LAKE" | "CONTAINERS" | "CAMPING" | "CASTLE" | "SKIING"
  | "CAMPERS" | "ARTIC" | "BOAT" | "BED_AND_BREAKFASTS" | "ROOMS" | "EARTH_HOMES" | "TOWER" | "CAVES"
  | "LUXES" | "CHEFS_KITCHEN"

export interface Category {
  icon: IconName, // symbol/ikonka reprezentująca kategorię
  displayName: string,  // nazwa, która będzie wyświetlana użytkownikowi
  technicalName: CategoryName,  // unikalna nazwa, będzie aplikowana w logice (w serwisie)
  activated: boolean  // flaga - czy kategoria jest aktywna
}
