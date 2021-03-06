# Albrecht

Albrecht utilizes a fast evolutionary algorithm to generate random magic squares of any order.



## Run

Albrecht is written in Java. To run it you'll need the [Java Runtime Environment](https://java.com/en/download/). This project uses [Maven](https://maven.apache.org/install.html) for its builds.

```bash
# execute inside Albrecht project directory
# generate default magic square of default order 30, saving results to the current directory
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



## Algorithm

The evolution process starts with a square matrix with values arranged at random. Mutations are applied to the square retaining incremental improvements. Values are continually swapped at random until the matrix represents a semi-magic square. Subsequent changes are random row and column exchanges until a magic square is formed.

 

## Benchmarks

The Albrecht algorithm is fundamentally stochastic, therefore individual runtimes will vary. Additionally, the application runs parallel processes dependent on the number of processors available to the environment. The following benchmarks were performed on a system with a 2.6 GHz Intel i7 and 6 cores.

| Order | Time to Complete |
| ----- | ---------------- |
| 10    | 1/50 secs        |
| 15    | 1/8 secs         |
| 20    | 0.7 secs         |
| 25    | 3 secs           |
| 30    | 7 secs           |
| 35    | 14 secs          |
| 40    | 0.8 mins         |
| 45    | 1.25 mins        |
| 50    | 2 mins           |
| 60    | 5.2 mins         |



## Results

Examples of discovered magic squares up to **order 125** may be found in the folder [examples](https://github.com/david-ta-ming/Albrecht/tree/main/examples).
