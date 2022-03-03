# Albrecht

Albrecht utilizes an evolutionary algorithm to generate magic squares of any order.



## Build and Run

Albrecht is written in Java. To run it you'll need the [Java Runtime Environment](https://java.com/en/download/). This project uses [Maven](https://maven.apache.org/install.html) for its builds.

```bash
# execute inside Albrecht project directory
# generate default magic square of order 15
java -jar target/albrecht.jar 
# generate a magic square of order 35, saving results to directory tmp, with a status report every 15 secs
java -jar target/albrecht.jar --order 35 --dir /tmp --report 15
```



## Arguments and Usage

```
java -jar target/albrecht.jar --help

usage: net.noisynarwhal.albrecht.Main
 -d,--dir <arg>      Output directory
 -h,--help           Display help
 -o,--order <arg>    Magic square order
 -r,--report <arg>   Report frequency in seconds
```



## About

The evolution process starts with a square matrix with values arranged at random.  Values are continually swapped at random until the matrix represents a semi-magic square. Subsequent changes are random row and column exchanges until a magic square is formed.

 

## Completion Times

The Albrecht algorithm is fundamentally stochastic, therefore individual runtimes will vary. Additionally, the application runs parallel processes dependent on the number of processors available to the environment. The following benchmarks were performed on a system with a 2.6 GHz Intel i7 with 6 cores.

| Order | Time to Complete |
| ----- | ---------------- |
| 10    | 1/50 secs        |
| 15    | 1/4 secs         |
| 20    | 1.7 secs         |
| 25    | 7 secs           |
| 30    | 30 secs          |
| 35    | 90 secs          |
| 50    | 8 mins           |



## Results

Examples of discovered magic squares may be found in the folder [examples](https://github.com/david-ta-ming/Albrecht/tree/main/examples).
