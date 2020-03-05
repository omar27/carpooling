import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CarpoolingTestModule } from '../../../test.module';
import { CarPoolingUserDeleteDialogComponent } from 'app/entities/car-pooling-user/car-pooling-user-delete-dialog.component';
import { CarPoolingUserService } from 'app/entities/car-pooling-user/car-pooling-user.service';

describe('Component Tests', () => {
  describe('CarPoolingUser Management Delete Component', () => {
    let comp: CarPoolingUserDeleteDialogComponent;
    let fixture: ComponentFixture<CarPoolingUserDeleteDialogComponent>;
    let service: CarPoolingUserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [CarPoolingUserDeleteDialogComponent]
      })
        .overrideTemplate(CarPoolingUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CarPoolingUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CarPoolingUserService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
