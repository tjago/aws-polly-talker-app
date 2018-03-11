# Polly Talker App
![Build Status](https://travis-ci.org/tjago/aws-polly-talker-app.svg?branch=master)

This app uses the AWS Polly service api to generate voice from text, similar to Siri.

Based on: [Polly Documentation](https://aws.amazon.com/polly/developers/)

note: previously this service was known as Ivona API, before acquisition by Amazon.


Features:

* Uses JavaFX GUI
* Grabs all voices for every language code
* plays voice in separete Thread
* Enables saving grabbed voice to .mp3 file

## Building

```
# gradlew clean build run
```

## Screenshots

![screen1][screen-main]

[screen-main]: https://github.com/tjago/javafx-ivona-speech-api/blob/master/media/screenshots/IvonaApp.JPG "JavaFX Ivon App GUI"