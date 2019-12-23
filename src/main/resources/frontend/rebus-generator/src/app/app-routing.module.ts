import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RebusComponent } from './rebus/rebus.component';
import { InfoComponent } from './info/info.component';

const routes: Routes = [
  { path: 'home', component: RebusComponent },
  { path: 'info', component: InfoComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
