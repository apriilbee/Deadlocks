# Deadlocks

How to download project:
1. Download zipped file and uncompress.
2. Open the project in any IDE (i.e. Netbeans)
3. The project wil encounter a problem due to importation of a third-party java library, Jama.
Resolve the problem by adding "jama-1.0.2.jar" (included in the zipped folder) to the project's library

How to run:
A. Deadlock Prevention
1. 


B. Deadlock Avoidance
1. Enter number of processes and resources.
2. The program will prompt the user for the details of the process. Enter such info.
3. Enter available resources for each Resource.
4. Given the above input, the program will assume that it is in a safe state and 
will display the initial allocation, maximum needed, available resources, and needed matrix. 
5. The program will prompt the user if a process has a new request. Press 1 if yes,  any characters
to terminate the program otherwise.
6. If the user pressed 1, the program will ask for the process id and request value for each process.
7. If the system state is safe, the program will display the status, the matrices and the safe sequence. It will deny the request if unsafe, otherwise. 
8. The program will prompt user again for a new process request until the user decides to terminate the program.
