import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CarpoolingTestModule } from '../../../test.module';
import { FavLocationDeleteDialogComponent } from 'app/entities/fav-location/fav-location-delete-dialog.component';
import { FavLocationService } from 'app/entities/fav-location/fav-location.service';

describe('Component Tests', () => {
  describe('FavLocation Management Delete Component', () => {
    let comp: FavLocationDeleteDialogComponent;
    let fixture: ComponentFixture<FavLocationDeleteDialogComponent>;
    let service: FavLocationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [FavLocationDeleteDialogComponent]
      })
        .overrideTemplate(FavLocationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FavLocationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FavLocationService);
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
