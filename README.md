# Build

## local
`gradle publishToMavenLocal` for local development

## Publish
Developer's notes on how to publish this artifact on bintray via PowerShell:
* do `gradle release` to do version tagging stuff
* git checkout the new version tag
* `$env:BINTRAY_USER = "debuglevel"; $env:BINTRAY_API_KEY = "SUPER_SECRET"; ./gradlew bintrayUpload`