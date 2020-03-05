import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CarpoolingTestModule } from '../../../test.module';
import { RideDeleteDialogComponent } from 'app/entities/ride/ride-delete-dialog.component';
import { RideService } from 'app/entities/ride/ride.service';

describe('Component Tests', () => {
  describe('Ride Management Delete Component', () => {
    let comp: RideDeleteDialogComponent;
    let fixture: ComponentFixture<RideDeleteDialogComponent>;
    let service: RideService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [RideDeleteDialogComponent]
      })
        .overrideTemplate(RideDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RideDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RideService);
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
