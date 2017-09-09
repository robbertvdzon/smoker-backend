import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { SettingsComponent } from './settings/settings.component';
import { GrafiekComponent } from './grafiek/grafiek.component';
import { MainComponent } from './main/main.component';
import { NotfoundComponent } from './notfound/notfound.component';
import { RouterModule, Routes } from '@angular/router';
import {SmokerserviceService} from "./smokerservice.service";
import {HttpClientModule} from "@angular/common/http";
import { FormsModule } from '@angular/forms';

const appRoutes: Routes = [
  { path: 'settings', component: SettingsComponent },
  { path: 'grafiek/:id', component: GrafiekComponent },
  { path: 'main', component: MainComponent }
  ,
  { path: '',
    redirectTo: '/main',
    pathMatch: 'full'
  }
  ,
  { path: '**', component: NotfoundComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    SettingsComponent,
    GrafiekComponent,
    MainComponent,
    NotfoundComponent
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { useHash:true, enableTracing: true } // <-- debugging purposes only
    ),
    HttpClientModule,
    BrowserModule,
    FormsModule
  ],
  providers: [SmokerserviceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
