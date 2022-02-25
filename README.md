# Albrecht

Albrecht utilizes an evoluionary algorithm to generate magic squares.



## Build and Run

```bash
# execute inside Albrecht project directory
mvn clean install
# generate default magic square of order 15
java -jar target/albrecht.jar 
```



## Arguments and Usage

```
java -jar target/albrecht.jar --help

usage: net.noisynarwhal.albrecht.Main
 -d,--dir <arg>      Output directory
 -h,--help           Display help
 -o,--order <arg>    Magic square order
 -p,--pop <arg>      Size of population
 -r,--report <arg>   Report frequency in seconds
```

