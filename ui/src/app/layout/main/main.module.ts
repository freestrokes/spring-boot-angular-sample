import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {MainComponent} from "./main.component";
import {SharedModule} from "../../common/shared.module";

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([
      {
        path: '',
        component: MainComponent
      }
    ])
  ],
  declarations: [
    MainComponent
  ],
  exports: [
    MainComponent
  ],
  providers: [
  ]
})
export class MainModule {
}
