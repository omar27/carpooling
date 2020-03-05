import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CarpoolingTestModule } from '../../../test.module';
import { RideUpdateComponent } from 'app/entities/ride/ride-update.component';
import { RideService } from 'app/entities/ride/ride.service';
import { Ride } from 'app/shared/model/ride.model';

describe('Component Tests', () => {
  describe('Ride Management Update Component', () => {
    let comp: RideUpdateComponent;
    let fixture: ComponentFixture<RideUpdateComponent>;
    let service: RideService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [RideUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RideUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RideUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RideService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ride(123);
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
        const entity = new Ride();
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
