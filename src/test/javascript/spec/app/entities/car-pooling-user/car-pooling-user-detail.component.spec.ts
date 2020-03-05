import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarpoolingTestModule } from '../../../test.module';
import { CarPoolingUserDetailComponent } from 'app/entities/car-pooling-user/car-pooling-user-detail.component';
import { CarPoolingUser } from 'app/shared/model/car-pooling-user.model';

describe('Component Tests', () => {
  describe('CarPoolingUser Management Detail Component', () => {
    let comp: CarPoolingUserDetailComponent;
    let fixture: ComponentFixture<CarPoolingUserDetailComponent>;
    const route = ({ data: of({ carPoolingUser: new CarPoolingUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [CarPoolingUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CarPoolingUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CarPoolingUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.carPoolingUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
