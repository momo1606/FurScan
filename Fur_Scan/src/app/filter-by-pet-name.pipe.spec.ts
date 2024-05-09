import { FilterByPetNamePipe } from './filter-by-pet-name.pipe';

describe('FilterByPetNamePipe', () => {
  it('create an instance', () => {
    const pipe = new FilterByPetNamePipe();
    expect(pipe).toBeTruthy();
  });
});
