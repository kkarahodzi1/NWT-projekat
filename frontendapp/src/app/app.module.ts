import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { HeaderComponentComponent } from './header-component/header-component.component';
import { FooterComponentComponent } from './footer-component/footer-component.component';
import { LoginComponentComponent } from './login-component/login-component.component';
import { AboutComponentComponent } from './about-component/about-component.component';
import { ContactComponentComponent } from './contact-component/contact-component.component';
import { UserService } from './services/user.service';
import { UserviewComponent } from './userview/userview.component';
import { RegisterComponentComponent } from './register-component/register-component.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TokenInterceptor } from './services/auth/token.interceptor';
import { AdminviewComponent } from './adminview/adminview.component';
import { FormsModule } from '@angular/forms';

// PAZNJA !!!!!
// U OVAJ SMRDLJIVI FAJL SE NECE AUTOMATSKI DODATI IMPORTI KADA NAPRAVITE NESTO NOVO - MORATE IH DODATI RUCNO !!!!

@NgModule({
   declarations: [
      AppComponent,
      HeaderComponentComponent,
      FooterComponentComponent,
      LoginComponentComponent,
      AboutComponentComponent,
      ContactComponentComponent,
      UserviewComponent,
      RegisterComponentComponent,
      AdminviewComponent
   ],
   imports: [
      BrowserModule,
      BrowserAnimationsModule,
      FormsModule,
      ToastrModule.forRoot(),
      HttpClientModule,
      RouterModule,
      ReactiveFormsModule,
      RouterModule.forRoot([
         { path: '', component: LoginComponentComponent },
         { path: 'about', component: AboutComponentComponent },
         { path: 'contact', component: ContactComponentComponent },
         { path: 'userview', component: UserviewComponent },
         { path: 'register', component: RegisterComponentComponent },
         { path: 'adminview', component: AdminviewComponent }
       ])
   ],
   providers: [
      UserService,
      {
         provide: HTTP_INTERCEPTORS,
         useClass: TokenInterceptor,
         multi: true
       }
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
