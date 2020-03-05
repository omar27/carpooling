import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRide } from 'app/shared/model/ride.model';
import { RideService } from './ride.service';

@Component({
  selector: 'jhi-ride-delete-dialog',
  templateUrl: './ride-delete-dialog.component.html'
})
export class RideDeleteDialogComponent {
  ride: IRide;

  constructor(protected rideService: RideService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rideService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'rideListModification',
        content: 'Deleted an ride'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ride-delete-popup',
  template: ''
})
export class RideDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ride }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RideDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.ride = ride;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/ride', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/ride', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
