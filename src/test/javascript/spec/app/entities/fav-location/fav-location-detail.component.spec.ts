import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarpoolingTestModule } from '../../../test.module';
import { FavLocationDetailComponent } from 'app/entities/fav-location/fav-location-detail.component';
import { FavLocation } from 'app/shared/model/fav-location.model';

describe('Component Tests', () => {
  describe('FavLocation Management Detail Component', () => {
    let comp: FavLocationDetailComponent;
    let fixture: ComponentFixture<FavLocationDetailComponent>;
    const route = ({ data: of({ favLocation: new FavLocation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CarpoolingTestModule],
        declarations: [FavLocationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FavLocationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FavLocationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.favLocation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
