import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CarpoolingTestModule } from '../../../test.module';
import { CarPoolingUserUpdateComponent } from 'app/entities/car-pooling-user/car-pooling-user-update.component';
import { CarPoolingUserService } from 'app/entities/car-pooling-user/car-pooling-user.service';
import { CarPoolingUser } from 'app/shared/model/car-pooling-user.model';

describe('Component Tests', () => {
  describe('CarPoolingUser Management Update Component', () => {
    let comp: CarPoolingUserUpdateComponent;
    let fixture: ComponentFixture<CarPoolingUserUpdateComponent>;
    let service: CarPoolingUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [CarPoolingUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CarPoolingUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CarPoolingUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CarPoolingUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CarPoolingUser(123);
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
        const entity = new CarPoolingUser();
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
