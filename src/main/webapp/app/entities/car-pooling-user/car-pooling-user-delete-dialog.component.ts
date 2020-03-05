import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICarPoolingUser } from 'app/shared/model/car-pooling-user.model';
import { CarPoolingUserService } from './car-pooling-user.service';

@Component({
  selector: 'jhi-car-pooling-user-delete-dialog',
  templateUrl: './car-pooling-user-delete-dialog.component.html'
})
export class CarPoolingUserDeleteDialogComponent {
  carPoolingUser: ICarPoolingUser;

  constructor(
    protected carPoolingUserService: CarPoolingUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.carPoolingUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'carPoolingUserListModification',
        content: 'Deleted an carPoolingUser'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-car-pooling-user-delete-popup',
  template: ''
})
export class CarPoolingUserDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ carPoolingUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CarPoolingUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.carPoolingUser = carPoolingUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/car-pooling-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/car-pooling-user', { outlets: { popup: null } }]);
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
