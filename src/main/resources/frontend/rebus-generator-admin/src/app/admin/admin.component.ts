import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { AdminService } from './admin.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  home: string;

  @ViewChild('newword') newword: ElementRef;
  @ViewChild('newwordlang') newwordlang: ElementRef;

  file: File;
  filename: string;
  uploadMessage: string;
  // status of uploading
  // 0 - no uploading process
  // 1 - uploaded
  uploadingImageStatus: number;

  languages: string[] = ["en"];

  constructor(private adminService: AdminService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.home = 'Back to rebus generator';
    this.filename = "Choose file";
    this.adminService.getLanguages().subscribe(langs => {
      this.languages = langs;
    });
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      this.file = event.target.files[0];
      this.filename = this.file.name.split("\\").pop();
    }
  }

  uploadImage() {
    if (this.file != null) {
      const formData = new FormData();
      formData.append('word', this.newword.nativeElement.value);
      formData.append('lang', this.newwordlang.nativeElement.value);
      formData.append('file', this.file);
      this.adminService.uploadNewImage(formData).subscribe(
        (res) => {
          this.uploadMessage = res;
          this.uploadingImageStatus = 1;
          this.clearValues();
          setTimeout(function() {
            this.uploadMessage = false;
            this.uploadingImageStatus = 0;
          }.bind(this), 5000);
        }
      );
    }
  }

  clearValues() {
    this.newword.nativeElement.value = "";
    this.file = null;
    this.filename = "";
  }

}
