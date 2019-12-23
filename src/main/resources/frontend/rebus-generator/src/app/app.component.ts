import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'rebus-generator';

  // for integrating with spring app:
  // 1. put whole the project to spring app
  // under the folder src/main/resources/frontend
  // 2. change in angular.json "outputPath": "dist/rebus-generator",
  // to path in spring app -> src/main/resources/public
  // 3. add plugin to pom.xml
  // 4. mvn package
  // 5. java -jar target/name-of-jar.jar
}
