# Discrete-Event-Simulation-in-Java
This is a Java-based simulation project that models a queueing system with random inter-arrival and service times. The simulation uses discrete event simulation techniques to simulate the system over time and compute various performance measures such as average waiting time, system time, and server utilization.

# Getting Started
To run the simulation, you need to have Java installed on your machine. Once you have Java installed, you can compile and run the DiscreteEventSimulation class. The simulation requires the following inputs:

  n: The number of customers to simulate
  lambda: The average arrival rate of customers
  mu: The average service rate of the server

# Running the Simulation
Once you have compiled and run the DiscreteEventSimulation class, you will be prompted to enter the simulation parameters. After you enter the parameters, the simulation will run and display the simulation results in a tabular format. The simulation results include the following performance measures:

  Inter-arrival time: The time between successive customer arrivals
  Arrival time: The time when each customer arrives
  Service time: The time it takes for the server to serve each customer
  Time service begins: The time when the server begins to serve each customer
  Waiting time: The time each customer spends waiting in the queue
  Customers in queue: The number of customers waiting in the queue at each time step
  Time service ends: The time when the server finishes serving each customer
  Time customer spends in system: The total time each customer spends in the system
  Server idle time: The time the server spends idle between customer service

# Simulation Results
The simulation results are displayed in a tabular format that shows the performance measures at each time step. The table includes the following columns:

  #: The customer number
  Inter-arrival: The time between successive customer arrivals
  Arrival: The time when each customer arrives
  ServTime: The time it takes for the server to serve each customer
  BeginServ: The time when the server begins to serve each customer
  Wait: The time each customer spends waiting in the queue
  Queue: The number of customers waiting in the queue at each time step
  EndServ: The time when the server finishes serving each customer
  SysTime: The total time each customer spends in the system
  Idle: The time the server spends idle between customer service

# Modifying the Simulation
You can modify the simulation by changing the values of the input parameters such as n, lambda, and mu. You can also modify the random inter-arrival and service time distributions by changing the interArrivalTimes and serviceTimes arrays.
