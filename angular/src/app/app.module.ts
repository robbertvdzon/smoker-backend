import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { SettingsComponent } from './settings/settings.component';
import { GrafiekComponent } from './grafiek/grafiek.component';
import { MainComponent } from './main/main.component';
import { NotfoundComponent } from './notfound/notfound.component';
import { RouterModule, Routes } from '@angular/router';

const appRoutes: Routes = [
  { path: 'settings', component: SettingsComponent },
  { path: 'grafiek', component: GrafiekComponent },
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
      { enableTracing: true } // <-- debugging purposes only
    ),
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
