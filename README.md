# Albrecht

Albrecht utilizes a fast evolutionary algorithm to generate random magic squares of any order.



## Requirements

- Java Development Kit (JDK) 8 or higher

- Apache Maven 3.2+

  

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/david-ta-ming/Albrecht.git
   cd lbrecht
   ```

2. Build the project using Maven:

   ```bash
   mvn clean package
   ```

This will create an executable JAR file in the `target` directory.



## Usage

Run Albrecht using the following command:

```bash
java -jar target/albrecht.jar [options]
```

### Options

- `-o, --order <n>`: Set the order of the magic square (default: 30)

- `-d, --dir <path>`: Specify the output directory (default: current directory)

- `-r, --report <seconds>`: Set the frequency of progress reports in seconds

- `-h, --help`: Display help information

  

### Examples

1. Generate a 30×30 magic square (default):

   ```bash
   java -jar target/albrecht.jar
   ```

2. Generate a 50×50 magic square, saving results to `/tmp`, with progress reports every 30 seconds:

   ```bash
   java -jar target/albrecht.jar --order 50 --dir /tmp --report 30
   ```



## Algorithm

Albrecht employs an evolutionary algorithm to generate magic squares of any order. The process begins with the creation of a random square matrix of the specified order, filled with distinct integers from 1 to n² (where n is the order of the square). This initial square serves as the starting point for the evolutionary process.

The core of the algorithm lies in its mutation and selection strategy. In each iteration, the algorithm generates a "child" square by applying a mutation to the current square. For non-semi-magic squares (where not all rows and columns sum to the magic constant), the mutation involves swapping two random values. This swap targets rows or columns that don't yet sum to the magic constant, aiming to improve the overall structure of the square. For semi-magic squares (where all rows and columns sum correctly, but diagonals may not), the mutation strategy shifts to exchanging entire rows or columns. This approach preserves the correct row and column sums while attempting to optimize the diagonal sums.

After each mutation, the algorithm calculates a score for the new square. This score is based on the number of rows, columns, and diagonals that sum to the magic constant. If the new square's score is equal to or better than the current square's score, it becomes the new current square. This process of mutation and selection continues until a perfect magic square is formed—when all rows, columns, and both main diagonals sum to the magic constant. To enhance performance, especially for larger orders, Albrecht implements multi-threading. Multiple threads can work on evolving different squares simultaneously, with the first thread to find a solution signaling the others to stop.

The algorithm's efficiency stems from its adaptive approach. As the square evolves and becomes semi-magic, the mutation strategy becomes more focused, targeting the specific aspects that need improvement (i.e., the diagonals). This targeted evolution, combined with the algorithm's ability to accept lateral moves (mutations that don't immediately improve the score but maintain it), allows it to navigate the solution space effectively, avoiding local optima and eventually converging on a perfect magic square.

 

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
