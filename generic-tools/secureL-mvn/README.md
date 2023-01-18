
## Build secureL implementation (mvn)
- To build `secureL` with maven, run:
```
mvn package -DskipTests

```
- To obtain the installable component, run the `make_component` script in the `component` directory.
```
cd component
./make_component.sh

```
- Install the component into your local GraalVM build with the following command:
```
gu install -L secL-component.jar

```
- Check the secureL component is installed with `gu list`.
