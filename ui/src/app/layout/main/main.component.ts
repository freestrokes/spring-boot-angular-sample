import {Component, OnInit, ElementRef, Injector, ViewChild} from "@angular/core";
import {PageComponent} from "../../common/component/page.component";

@Component({
  selector: 'main',
  templateUrl: './main.component.html'
})
export class MainComponent extends PageComponent implements OnInit {

  constructor(protected elementRef: ElementRef,
              protected injector: Injector) {

    super(elementRef, injector);

  }

  ngOnInit() {

  }

}
