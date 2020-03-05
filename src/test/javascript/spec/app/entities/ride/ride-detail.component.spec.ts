import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarpoolingTestModule } from '../../../test.module';
import { RideDetailComponent } from 'app/entities/ride/ride-detail.component';
import { Ride } from 'app/shared/model/ride.model';

describe('Component Tests', () => {
  describe('Ride Management Detail Component', () => {
    let comp: RideDetailComponent;
    let fixture: ComponentFixture<RideDetailComponent>;
    const route = ({ data: of({ ride: new Ride(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [RideDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RideDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RideDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ride).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
