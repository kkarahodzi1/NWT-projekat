import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';


import { AppComponent } from './app.component';
import { HeaderComponentComponent } from './header-component/header-component.component';
import { FooterComponentComponent } from './footer-component/footer-component.component';
import { LoginComponentComponent } from './login-component/login-component.component';
import { AboutComponentComponent } from './about-component/about-component.component';
import { ContactComponentComponent } from './contact-component/contact-component.component';

@NgModule({
   declarations: [
      AppComponent,
      HeaderComponentComponent,
      FooterComponentComponent,
      LoginComponentComponent,
      AboutComponentComponent,
      ContactComponentComponent
   ],
   imports: [
      BrowserModule,
      RouterModule,
      ReactiveFormsModule,
      RouterModule.forRoot([
        { path: '', component: LoginComponentComponent },
        { path: 'about', component: AboutComponentComponent },
        { path: 'contact', component: ContactComponentComponent },
      ])
   ],
   providers: [],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
