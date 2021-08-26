import {Component, OnInit, ElementRef, Injector} from '@angular/core';
import {AbstractComponent} from "../common/component/abstract.component";

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html'
})
export class LayoutComponent extends AbstractComponent implements OnInit {

  constructor(protected elementRef: ElementRef,
              protected injector: Injector) {

    super(elementRef, injector);
  }

  ngOnInit() {
  }

}
