# Build

## local

`gradle publishToMavenLocal` for local development

## Publish (Bintray)

Developer's notes on how to publish this artifact on bintray via PowerShell:

* do `gradle release` to do version tagging stuff
* git checkout the new version tag
* `$env:BINTRAY_USER = "debuglevel"; $env:BINTRAY_API_KEY = "SUPER_SECRET"; ./gradlew bintrayUpload`

## Publish (MavenCentral)

Developer's notes on how to publish this artifact on bintray via PowerShell:

* do `gradle release` to do version tagging stuff
* git checkout the new version tag
* `$env:SONATYPE_NEXUS_USERNAME = "__YOUR_USERNAME__"; $env:SONATYPE_NEXUS_PASSWORD = "__SUPERSECRET__"; ./gradlew uploadArchives --no-daemon --no-parallel`