
## Build and Publish
Developer's notes on how to publish this artifact on bintray via PowerShell:
* ensure to increment version in `gradle.build`
* `$env:BINTRAY_USER = "debuglevel"; $env:BINTRAY_API_KEY = "SUPER_SECRET"; ./gradlew bintrayUpload`