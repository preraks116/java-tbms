# java-tbms

## Introduction

TBMS is a Java-based application that provides functionalities for managing banks, currencies, and exchange rates, with a focus on finding the best exchange rates across all banks in the system.

## Features

- Bank Management
- Currency Management
- Exchange Rate Management
- Command-Line Interface
- **Exchange Service:**

  * Efficient graph-based implementation for managing currency relationships and exchange rates
  * Support for adding, deleting, and querying exchange rates

## Data Structures

- Graphs: The core of the Exchange Service uses a weighted directed graph implemented using JGraphT library
- Maps: Used to store information about banks and currencies

## Setup and Installation

1. Ensure you have Java JDK 11 or higher installed.
2. Clone the repository

   ```
   git clone https://github.com/yourusername/currency-exchange-system.git
   ```
3. Build Project

   ```
   mvn clean install
   ```
4. Run the application

   ```
   java -jar target/currency-exchange-system-1.0-SNAPSHOT.jar
   ```

## Commands

Type `help` to see the list of commands and their usages.
