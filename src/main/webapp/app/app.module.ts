import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CarpoolingSharedModule } from 'app/shared/shared.module';
import { CarpoolingCoreModule } from 'app/core/core.module';
import { CarpoolingAppRoutingModule } from './app-routing.module';
import { CarpoolingHomeModule } from './home/home.module';
import { CarpoolingEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    CarpoolingSharedModule,
    CarpoolingCoreModule,
    CarpoolingHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CarpoolingEntityModule,
    CarpoolingAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class CarpoolingAppModule {}
