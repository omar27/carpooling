import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CarpoolingTestModule } from '../../../test.module';
import { FavLocationUpdateComponent } from 'app/entities/fav-location/fav-location-update.component';
import { FavLocationService } from 'app/entities/fav-location/fav-location.service';
import { FavLocation } from 'app/shared/model/fav-location.model';

describe('Component Tests', () => {
  describe('FavLocation Management Update Component', () => {
    let comp: FavLocationUpdateComponent;
    let fixture: ComponentFixture<FavLocationUpdateComponent>;
    let service: FavLocationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [FavLocationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FavLocationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FavLocationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FavLocationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FavLocation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FavLocation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
