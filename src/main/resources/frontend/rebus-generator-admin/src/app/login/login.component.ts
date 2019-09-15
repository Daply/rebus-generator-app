import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin/admin.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  model: any = {};

  constructor(private adminService: AdminService) { }

  ngOnInit() {
    sessionStorage.setItem('auth', '');
  }

  login() {
     this.adminService.loginAdmin(this.model);
  }
}
