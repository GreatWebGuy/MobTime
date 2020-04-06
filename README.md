# MobTime
Mob timer written in JavaFX

For Timing Mob Sessions

### Build
#### Requirements
- Gradle 6.x
- OpenJDK 14
- JavaFX 14

#### Package generation
MacOSX
```./gradlew jpackage -PinstallerType=pkg```
Windows
```gradlew.bat jpackage -PinstallerType=msi```
Linux
```./gradlew jpackage -PinstallerType=deb```
### Use
- Download the MacOSX or Windows installer, install and run
- Default time is 7 minutes
- Click center of screen to start timer

#### Toolbar
 * Pause to pause current user
 * Skip to skip to the next user
 * Stop to stop rotate nag alarm or current users turn
 * Settings - Add/Remove users for named rotation and Skip/Reorders users 

![Info Graphic Mobtime](docs/images/mobtime-info-graphic.png "Info Graphic Mobtime")


[http://greatwebguy.github.io/MobTime/](http://greatwebguy.github.io/MobTime/)
