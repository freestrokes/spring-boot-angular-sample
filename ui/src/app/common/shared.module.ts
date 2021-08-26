import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

export const MODULES = [
	CommonModule,
	FormsModule,
	HttpClientModule,
	RouterModule,
];

export const COMPONENTS = [
];

/**
 * Common Modules and Components
 */
@NgModule({
	imports: [
		MODULES
	],
	declarations: [
		COMPONENTS
	],
	exports: [
		MODULES,
		COMPONENTS
	]
})
export class SharedModule {
}
