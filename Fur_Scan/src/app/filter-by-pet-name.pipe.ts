import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterByPetName'
})
export class FilterByPetNamePipe implements PipeTransform {
  transform(pets: any[], searchTerm: string): any[] {
    if (!searchTerm) {
      return pets;
    }

    return pets.filter(pet => pet.pet_name.toLowerCase().includes(searchTerm.toLowerCase()));
  }
}
