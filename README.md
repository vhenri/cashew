# Project Cachew

## Problem: 
Using Kotlin, write a single page Android app that solves the following problem: 
 
You have a bucket of i lightbulbs of j different colours, k of each colour. 
 
i = j * k
 
The program should randomly pick m number of lightbulbs from the bucket and determine the number of unique colours you receive. The inputs i, j, k and m is determined by input fields.  Extend the simulation to determine the expected number of unique colours in after running this scenario n number of times (in other words, the average output of the simulation).

## Solution Notes
- User is restricted to inputs of 5 digits or less
- Error alert will show if user tries to run a simulation causing an Out Of Memory Error
- Any negative numbers will be converted into positives when running the simulation
