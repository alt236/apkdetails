# APK Details

Analyzes an APK and prints information helpful to a developer

- API levels as declared in the manifest.
- Number of permissions/activities/services/receivers. If `-v` is set, they will also be listed
- Information about the signing certificate. If the APK is signed it will also be validated.
- The largest files in the APK, along with the largest resources.
- Information about the DEX files, such as how many there are and the number of classes, methods and strings.
- List of the supported JNI architectures based on the contents of the `lib` directory.

## Usage

```
usage: apkdetails.jar [-h] -i <arg> [-v]
version: 1.0
Analyzes an APK and prints helpful info.

 -h,--human         If set, file sizes will be human readable
 -i,--input <arg>   Input. This can be either a single APK, or a
                    directory. If it's the latter, all files with ".apk"
                    extension will be analysed
 -v,--verbose       If set, more detailed information will be printed.
                    This includes a full list of Manifest
                    activities/permissions/receivers and services.

Sample usage:

 * apkdetails-1.0-SNAPSHOT.jar --input ~/tmp/sample.apk
 * apkdetails-1.0-SNAPSHOT.jar -v -i ~/tmp/path/to/apks/

Source code: https://github.com/alt236/apkdetails
Please report issues at https://github.com/alt236/apkdetails/issues
```
## Example Usage

```
apkdetails.jar --input ~/tmp/sample.apk
apkdetails.jar -v -i ~/tmp/path/to/apks/
```

## Sample output

* [Google Play Store 8.4.40](sample_output/google_play_store_8.4.40.md)
* [Google Play Services 11.9.49](sample_output/google_play_services_11.9.49.md)

## Build Instructions

Linux/Mac: `mvn clean package && chmod +x target/apkdetails-X.X.jar`

## Links

* Github: [https://github.com/alt236/apkdetails]()

## Credits

Author: [Alexandros Schillings](https://github.com/alt236).

The XML parser a slightly modified version of the one in [apk-extractor](https://code.google.com/archive/p/apk-extractor) by Prasanta Paul.

The code in this project is licensed under the [Apache Software License 2.0](LICENSE-2.0.html).

Copyright (c) 2017 Alexandros Schillings.
