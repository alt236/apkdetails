# APK Details

Analyzes an APK and prints information helpful to a developer

- API levels as declared in the manifest.
- Number of permissions/activities/services/receivers. If `-v` is set, they will also be listed
- Information about the signing certificate. If the APK is signed it will also be validated.
- The largest files in the APK, along with the largest resources.
- Information about the DEX files, such as how many there are and the number of classes, methods and strings.
- List of the supported JNI architectures based on the contents of the `lib` directory.

## Usage
Get the latest release here: [https://github.com/alt236/apkdetails/releases]()

```
usage: apkdetails.jar [-h] -i <arg> [-o <arg>] [--print-class-graph |
                             --print-class-list | --print-class-tree | --print-manifest]    [-s
                             <arg>] [-v]
version: 1.2.1
Analyzes an APK and prints helpful info.

 -h,--human               If set, file sizes will be human readable
 -i,--input <arg>         Input. This can be either a single APK, or a
                          directory. If it's the latter, all files with
                          ".apk" extension will be analysed
 -o,--output <arg>        Output Directory (Optional)
    --print-class-graph   Print the APK classes as a GraphML graph to
                          terminal. It ignores all options except -i and
                          -o.
    --print-class-list    Print the APK classes as a list to the terminal.
                          It ignores all options except -i and -o.
    --print-class-tree    Print the APK classes as an ASCII tree to
                          terminal. It ignores all options except -i and
                          -o.
    --print-manifest      Print the APK manifest to the terminal. It
                          ignores all options except -i and -o.
 -s,--show-only <arg>     Show only the following, comma separated
                          outputs. Valid options are: [architectures,
                          build_config, certificates, content_size,
                          dex_info, file_info, manifest, resources]
 -v,--verbose             If set, more detailed information will be
                          printed. This includes a full list of Manifest
                          activities/permissions/receivers and services.

Sample usage:

 * apkdetails.jar --input ~/tmp/sample.apk
 * apkdetails.jar -v -i ~/tmp/path/to/apks/

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

## Versions

* 1.0.0: First release
* 1.1.0: Added selection of outputs
* 1.2.0: Added options to print list/tree/graph of classes and dump of the Manifest to the terminal.
* 1.2.1: Fixed NPE when EncodedValue is null while parsing BuildConfigs
* 1.2.2: Added option to recursively search a directory for APKs

## Links

* Github: [https://github.com/alt236/apkdetails]()
* Bug reports: [https://github.com/alt236/apkdetails/issues]()
* Releases: [https://github.com/alt236/apkdetails/releases]()

## Credits

Author: [Alexandros Schillings](https://github.com/alt236).

The XML parser a slightly modified version of the one in [apk-extractor](https://code.google.com/archive/p/apk-extractor) by Prasanta Paul.

The code in this project is licensed under the [Apache Software License 2.0](LICENSE).

## License
Copyright (c) 2017-present, Alexandros Schillings.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.