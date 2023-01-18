# Steps to follow to try executing a simple polyglot native image inside an SGX enclave.




## Environment setup
- Begin by creating a directory which will host all your work. We will call this directory `truffle-dev`. 
```
mkdir -p truffle-dev
cd truffle-dev
```
- Clone this repository and checkout the `master` branch, within the `truffle-dev` folder.
```
git clone https://gitlab.com/Yuhala/generic-tools.git
git checkout master
```

- Clone our modified `GraalVM` in the `truffle-dev` folder.
```
git clone https://github.com/Yuhala/graal.git
git checkout part
```
- To be exact, our modified GraalVM repo is based on `Graal version: 22.1.0 commit bf17700` of the original github repo.
- Further, we will build this custom GraalVM and use it for the purpose of our project.
- In the `truffle-dev` folder, download the `mx build tool` used to build GraalVM projects:
```
git clone https://github.com/graalvm/mx.git
```
- Our modified GraalVM above builds correctly with version: `5.317.7` of the `mx` tool downloaded above, checkout this version of mx.
```
cd mx
git checkout 5.317.7
cd ..
```

- GraalVM's JIT compiler works with the default JVM as a plugin with the help of the JVM compiler interface (JVMCI), and thus requires a JDK which supports a graal-compatible version of JVMCI. You can find a compatible version in `jdk-tools` folder of `generic-tools`: `labsjdk-ce-11.0.13+8-jvmci-22.0-b02-linux-amd64.tar.gz`. Other compatible versions can be found here (for Java 8): [jvmci releases java 8](https://github.com/graalvm/graal-jvmci-8/releases), and here (for Java 11): [jvmci releases java 11](https://github.com/graalvm/labs-openjdk-11/releases). We use Java 11.

- Extract the JDK in the `truffle-dev` working directory
```
cp generic-tools/jdk-tools/labsjdk-ce-11.0.13+8-jvmci-22.0-b02-linux-amd64.tar.gz .
tar -xvf labsjdk-ce-11.0.13+8-jvmci-22.0-b02-linux-amd64.tar.gz
```

## Installing Intel SGX tools (SGX SDK + driver)
- We created a script to install SGX for Ubuntu based systems: `16.04`, `18.04`, and `20.04`. It was tested on both `18.04` and `20.04`
- Copy the `sgx-install.sh` script from `graal` folder into `truffle-dev`: 
```
cp graal/sgx-install.sh .

```
- The script by default will install SGX tools with debug information included. To install the tools without debug information, change the value of `debug_info` in the script to `0`.
- Run the SGX install script:
```
./sgx-install.sh

```
- If your hardware does not support SGX, the necessary packages will still be installed; however you will have to use SGX in simulation mode in your makefiles (`SGX_MODE=SIM`). Our makefile builds the SGX applications in simulation mode so no need to do these modifications.
- To use the SGX SDK custom debugger `sgx-gdb`, install these packages: `libsgx-enclave-common-dbgsym libsgx-urts-dbgsym`. Run the `graal/sgx-gdb-install.sh` script to install these.


## Build GraalVM

- To build GraalVM, we need to add `mx` to the `PATH` and set `JAVA_HOME` to point to the JDK with JVMCI we downloaded. Copy the `generic-tools/set-env.sh` file into the `truffle-dev` directory and source it.
```
cp generic-tools/set-env.sh
source set-env.sh

```
- Run the command `mx --version` and make sure its output is `mx version 5.317.7`.

- To build GraalVM, move into the `graal` folder and run the build script as shown:
```
./build_graal

```
- This will take some time, depending on your computer's memory and CPU resources. My `4-core, 64GB` server completes the build in about 20 mins (todo: check exact). This should give you an idea of how resource heavy the full build process is.
- Once `GraalVM` is fully built, update your `JAVA_HOME` to point to this dev build:
```
./set-env-graal.sh

```
- At this point, we are ready to use our GraalVM build with the other tools: `polytaint` and `secureL`.

## Some build hints: this subsection is just FYI and you don't need to run these commands)
- To specify the python version used by `mx` build tool, use the following (example uses python3).
```
export MX_PYTHON="python3"

```
- This is done already in the build script above.

- To specify the exact commit id for a suite to be dynamically imported, modify the corresponding version (=commit id) in suite.py. For example the below entry will clone graalpython commit: `5e9c2c685654ddc784b0d5db365a40930a099e12`.
```
{
                "name": "graalpython",
                "version": "5e9c2c685654ddc784b0d5db365a40930a099e12",
                "dynamic": True,
                "urls": [
                    {"url": "https://github.com/graalvm/graalpython.git", "kind": "git"},
                    {"url": "https://curio.ssw.jku.at/nexus/content/repositories/snapshots", "kind": "binary"},
                ]
}
```


## GraalVM dev build (FYI)
- The env file used for the GraalVM dev build can be found in `graal/vm/mx.vm/sgx` file.
- The GraalVM dev build (vm suite to be precise) has some language components pre-installed after the build. Use `gu list` to see the list. Other language components like `secure language` can be manually installed locally using the corresponding language component JAR. See [secL readme](./secureL-mvn/README.md).
- 

## GraalVM language components
- Download installable components for GraalVM CE from the GraalVM github repos:
- For GraalVM 22.1.0: https://github.com/graalvm/graalvm-ce-builds/releases
- Install the components manually locally, use: `gu install -L /path/to/file`. Use the `-f` option to skip version checks. But use similar versions to your GraalVM dev build to avoid compatibility issues as much as possible. 


## Analyse and run applications with polytaint
- First build `secureL`. See [secL readme](./secureL-mvn/README.md). 
- To analyse applications with the taint tracking tool or build applications to run fully inside an SGX enclave, see [polytaint readme](./polytaint/README.md)



## Some debug hints
- To print out each call of a libc routine in our wrapper library, uncomment `#define GRAAL_SGX_DEBUG 1` in the `sgx/Include/user_types.h` file.


## Possible build errors
- In the event of linker errors with `liblibchelper.a`, remove the latter from `sgx/Common` folder and replace it with the one built during the build process above which can be found here: `substratevm/mxbuild/linux-amd64/SVM_HOSTED_NATIVE/linux-amd64/liblibchelper.a`

- Rebuild the SGX program.


## Problems with native image agent
- The tracing agent doesn't seem to pick up methods accessed reflectively via Truffle's API. The partitioning module will have to be extended to generate extra configuration file with all methods accessed reflectively. Example: the following JSON entry is used to add the `helloJava` method of the Main class to the native reflection configuration.

```
{
  "name":"iiun.smartc.Main",
  "methods":[{"name":"helloJava","parameterTypes":[] }]
}
```

## Author Info
- Peterson Yuhala <petersonyuhala@gmail.com>


## References
- GraalVM [SimpleLanguage](https://www.graalvm.org/22.0/graalvm-as-a-platform/implement-language/#run-simplelanguage-with-the-newest-developement-version-of-the-compiler).
- GraalVM [Instruments](https://www.graalvm.org/22.0/graalvm-as-a-platform/implement-instrument/)