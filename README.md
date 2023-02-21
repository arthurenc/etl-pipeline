# etl-pipeline

Extract, transform, load (ETL) pipeline with Origin and Destination traits

## Origin

The Origin trait has three methods:
- openFile(path) takes a String path and returns the file as a BufferedSource or throws a FileNotFoundException
- extract(path) takes a String path and returns it as the given type
- clean(input) takes a file (input) of a given type, cleans the file and returns it

There are three concrete implementations of the Origin trait:
- `StringOrigin` returns and cleans a String type
- `IntListOrigin` returns and cleans a List[Int]
- `JsonOrigin` returns and cleans a circe Json type

## Destination

The Destination trait has two methods:
- transform(input) takes a file (input) of a given type, transforms the file and returns it
- save(filePath, input) takes a file (input) and saves it in the location of the path

There are three concrete implementations of the Destination trait:
- `StringDestination` transforms and saves a String type
- `IntListDestination` transforms and saves a List[Int]
- `JsonDestination` transforms and saves a circe Json type