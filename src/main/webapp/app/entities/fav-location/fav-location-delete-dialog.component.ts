import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFavLocation } from 'app/shared/model/fav-location.model';
import { FavLocationService } from './fav-location.service';

@Component({
  selector: 'jhi-fav-location-delete-dialog',
  templateUrl: './fav-location-delete-dialog.component.html'
})
export class FavLocationDeleteDialogComponent {
  favLocation: IFavLocation;

  constructor(
    protected favLocationService: FavLocationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.favLocationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'favLocationListModification',
        content: 'Deleted an favLocation'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-fav-location-delete-popup',
  template: ''
})
export class FavLocationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ favLocation }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FavLocationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.favLocation = favLocation;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/fav-location', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/fav-location', { outlets: { popup: null } }]);
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
