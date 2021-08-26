import {NgModule} from "@angular/core";
import {LayoutComponent} from "./layout.component";
import {RouterModule} from "@angular/router";
import {SharedModule} from "../common/shared.module";

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([
      {
        path: '',
        component: LayoutComponent,
        children: [
          {
            path: '',
            redirectTo: 'main',
            pathMatch: 'full'
          },
          {
            path: 'main',
            loadChildren: './main/main.module#MainModule'
          },
          {
            path: 'error',
            loadChildren: './error/error.module#ErrorModule'
          },
        ]
      }
    ])
  ],
  declarations: [
    LayoutComponent
  ],
  providers: [
  ]
})
export class LayoutModule {
}
