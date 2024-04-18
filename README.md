# StatefulLayout
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![license](https://img.shields.io/github/license/balazsgerlei/StatefulLayout)](https://opensource.org/license/MIT)
[![last commit](https://img.shields.io/github/last-commit/balazsgerlei/StatefulLayout?color=018786)](https://github.com/balazsgerlei/StatefulLayout/commits/main)
[![](https://jitpack.io/v/balazsgerlei/StatefulLayout.svg)](https://jitpack.io/#balazsgerlei/StatefulLayout)

The Android layout for displaying different states (loading, empty, error, content) on UI.

__WARNING! The library is in its early stage with active development, so expect breaking changes!__

## Setup

The library is currently hosted on `jitpack.io`. You can add it as a depenency to your project:

Step 1. Add the JitPack repository to your build file

Add it in your root `build.gradle` at the end of repositories:

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.balazsgerlei:StatefulLayout:1.0.0-alpha06'
	}
