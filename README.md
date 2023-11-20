# MobTime
Mob timer written in JavaFX

![MobTime Build](https://github.com/GreatWebGuy/MobTime/workflows/MobTime%20Build/badge.svg)

For Timing Mob Sessions

## Run
- Download the MacOSX, Windows, or Linux (Ubuntu/Debian) installer, install and run
- Default time is 7 minutes
- Click center of screen to start timer

#### Toolbar
 * Pause to pause current user
 * Skip to skip to the next user
 * Stop to stop rotate nag alarm or current users turn
 * Settings - Add/Remove users for named rotation and Skip/Reorders users 
 
#### Secret Menu
 * If a user is called `break` it will function as a break
 * If you add an environment variable such as `export MOBTIME_SCRIPT=/Users/jason-crow/{name}.py` it will run that file if it exists for current person

![Info Graphic Mobtime](docs/images/mobtime-info-graphic.png "Info Graphic Mobtime")


[http://greatwebguy.github.io/MobTime/](http://greatwebguy.github.io/MobTime/)

## Build

### CI
- Create a branch from master
- Write code
- Push to github and actions will build windows/macos/linux executables
- Update version by incrementing semver
- Update release.md with release notes
- If all is successful open a PR

### Local
#### Requirements
- OpenJDK 21.0.1

#### Windows
- enable .net framework 3.5.1 on Windows 10
- Install WIX build tools

### Build and run locally
```./gradlew run```

### Package generation
> MacOSX
> ```./gradlew jpackage -PinstallerType=pkg```

> Windows
> ```gradlew.bat jpackage -PinstallerType=msi```

> Linux (Ubuntu)
> ```./gradlew jpackage -PinstallerType=deb```

 