# rebus-generator-app
Application on generating rebus on input word

Deployed for a while on AWS EC2<br>
http://rebusgenerator.online/

# How it works: 
- The word is read from the input
- By any two sequential symbols from this word words from database with the same symbols are searched, so any two sequential symbols of input word are connected with some word from database
- The word is splitted into parts from two to whole word length symbols, these symbols are compared with existing image words and the most fit are chosen as a parts of rebus image
- The rebus sequence is build - the sequence of all commands needed to change chosen words to express the input word
- Images that needed for rebus are downloaded from remote storage and merged to the final rebus image, image can be downloaded

# Some pictures
- The main page

![alt text](https://github.com/Daply/rebus-generator-app/blob/master/demo%20images/main_page.png)
- Rebus generation loading

![alt text](https://github.com/Daply/rebus-generator-app/blob/master/demo%20images/loading.png)
- Rebus generation result with rebus picture

![alt text](https://github.com/Daply/rebus-generator-app/blob/master/demo%20images/rebus_result.png)
