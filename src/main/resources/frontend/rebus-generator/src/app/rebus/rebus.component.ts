import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { RebusService } from './rebus.service';
import { DomSanitizer } from '@angular/platform-browser';
import { Rebus } from './rebus.model';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-rebus',
  templateUrl: './rebus.component.html',
  styleUrls: ['./rebus.component.css']
})
export class RebusComponent implements OnInit {

  @ViewChild('inputword') inputword: ElementRef;
  @ViewChild('inputwordlang') inputwordlang: ElementRef;
  @ViewChild('newword') newword: ElementRef;
  @ViewChild('newwordlang') newwordlang: ElementRef;

  languages: string[] = ["en"];

  // status of uploading
  // 0 - no generating process
  // 1 - generating process
  // 2 - generating process successfully
  // 3 - generating process error
  generatingProcessStatus: number;
  private image : any;
  private imageBase64: string;
  private imageName : string;
  private readonly imageType : string = 'data:image/(png|jpeg|jpg);base64,';
  rebus: Rebus;

  file: File;
  filename: string;
  // status of uploading
  // 0 - no uploading process
  // 1 - uploaded
  uploadingImageStatus: number;
  uploadMessage: boolean;

  constructor(private rebusService: RebusService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.filename = "Choose file";
    this.rebusService.getLanguages().subscribe(langs => {
      this.languages = langs;
    });
    this.generatingProcessStatus = 0;
    this.uploadingImageStatus = 0;
  }

  generateRebus() {
    this.image = null;
    this.generatingProcessStatus = 1;
    this.rebusService.getRebusSequence(this.inputword.nativeElement.value, 
                                       this.inputwordlang.nativeElement.value).subscribe(rebus => {
        this.rebus = rebus;
        this.image = this.sanitizer.bypassSecurityTrustUrl(this.imageType + rebus.content);
        this.imageBase64 = rebus.content;
        this.imageName = rebus.image_name;
        if (this.imageName == "error" || this.image == null){
          this.generatingProcessStatus = 3;
          setTimeout(function() {
            this.generatingProcessStatus = 0;
          }.bind(this), 5000);
        }
        else {
          this.generatingProcessStatus = 2;
        }
    });
  }

  saveRebusImage() {
    const byteCharacters = this.dataURItoBlob(this.imageBase64);
    var href = URL.createObjectURL(new Blob([byteCharacters]));
    saveAs(href, this.imageName);
  }

  dataURItoBlob(dataURI) {
    const byteString = window.atob(dataURI);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteString.length; i++) {
      int8Array[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([int8Array], { type: 'image/png' });    
    return blob;
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
      this.rebusService.uploadNewImageForReview(formData).subscribe(
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
