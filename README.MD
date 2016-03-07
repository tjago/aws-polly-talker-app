# Ivona Talker App

This app uses the Amazon Ivona service api to generate voice from text, similar to Siri.

Based on: [ivona-speechcloud-sdk-java](https://github.com/IvonaSoftware/ivona-speechcloud-sdk-java)


Features:

* Uses JavaFX GUI
* Grabs all possible voices for every language code
* plays voice in separete Thread
* Enables saving grabbed voice to .mp3 file

## Building

```
# gradlew clean build run
```

## Screenshots

![screen1][screen-main]

[screen-main]: https://github.com/tjago/javafx-ivona-speech-api/blob/master/media/screenshots/IvonaApp.JPG "JavaFX Ivon App GUI"