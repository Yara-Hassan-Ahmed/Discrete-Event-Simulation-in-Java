package discreteeventsimulation;

import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class DiscreteEventSimulation {

// declare variables
// wait for size input to instantiate arrays
	static Random random = new Random();
	static int[] customer;
	// static int[] interArrivalTimes = {0, 4, 2, 3, 2, 3, 3, 4, 2, 1};
	static int[] interArrivalTimes;
	static int[] interArrivalTimesInput;
	static double[] interArrivalTimesProb;
	static int[] arrivalTimes;
	// static int[] serviceTimes = {3, 4, 2, 3, 4, 5, 2, 2, 3, 4};
	static int[] serviceTimes;
	static int[] serviceTimesInput;
	static double[] serviceTimesProb;
	static int[] serviceBeginTimes;
	static int[] waitingTimes;
	static int[] customersInQueue;
	static int[] serviceEndTimes;
	static int[] customerSpentTimes;
	static int[] serverIdleTimes;

	static int intrSUM = 0;
	static int arrSUM = 0;
	static int servTimeSUM = 0;
	static int servBeginTimeSUM = 0;
	static int waitSUM = 0;
	static int queueSUM = 0;
	static int servEndTimeSUM = 0;
	static int sysTimeSUM = 0;
	static int idleSUM = 0;

	public static int[] generateRandomNumbers(int size, double[] cdf, int[] values) {
		int arr[] = new int[size];

		for (int i = 0; i < size; i++) {
			double r = Math.random();
			for (int j = 0; j < values.length; j++) {
				if (j == 0) {
					if (r < cdf[j] && r > 0.0) {
						arr[i] = values[j];
						break;
					}
				} else {
					if (r < cdf[j] && r > cdf[j - 1]) {
						arr[i] = values[j];
						break;
					}
				}
			}

		}
		return arr;
	}

	private static void printTable(int[][] table) {
		// print sim table
		for (int[] row : table) {
			System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %n", row[0], row[1], row[2],
					row[3], row[4], row[5], row[6], row[7], row[8], row[9]);
		}
	}

	private static int[][] createTable(int rows) {
/////////////////////////print table header  columns///////////////////////
		int table[][] = new int[rows][];
		System.out.println("\t\t\t\t\t\t\t\t\t---Simulation Table---\t\t\t\t\t\t\t");
		System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %n", "#", "Inter-arrival",
				"Arrival", "ServTime", "BeginServ", "Wait", "Queue", "EndServ", "SysTime", "Idle");

		for (int i = 0; i < rows; i++) {
			if (i == 0) {
				customer[i] = i + 1;
				arrivalTimes[i] = 0 + interArrivalTimes[i];
				serviceBeginTimes[i] = 0;
				waitingTimes[i] = 0;
				customersInQueue[i] = 0;
				serviceEndTimes[i] = serviceTimes[i];
				customerSpentTimes[i] = serviceTimes[i] + waitingTimes[i];
				serverIdleTimes[i] = 0;
			} else {
				boolean calcIdle = false;
				customer[i] = i + 1;
				arrivalTimes[i] = arrivalTimes[i - 1] + interArrivalTimes[i];
				if (arrivalTimes[i] > serviceEndTimes[i - 1]) {// means there was idling
					serverIdleTimes[i] = arrivalTimes[i] - serviceTimes[i - 1];
					calcIdle = true;
				}
				if (calcIdle) {
					serviceBeginTimes[i] = serviceEndTimes[i - 1] + serverIdleTimes[i];
				} else {
					serviceBeginTimes[i] = serviceEndTimes[i - 1];
				}
				waitingTimes[i] = serviceBeginTimes[i] - arrivalTimes[i];
//getting the queue length
				int count = 0;
				for (int j = rows; j > 0; j--) {
					if (arrivalTimes[i] < serviceEndTimes[j - 1]) {
						count++;
					}
					customersInQueue[i] = count;
				}
				serviceEndTimes[i] = serviceBeginTimes[i] + serviceTimes[i];
				customerSpentTimes[i] = serviceTimes[i] + waitingTimes[i];
				if (!calcIdle) {
					serverIdleTimes[i] = serviceBeginTimes[i] - serviceEndTimes[i - 1];
				}

			}
// fill sim table
			table[i] = new int[] { customer[i], interArrivalTimes[i], arrivalTimes[i], serviceTimes[i],
					serviceBeginTimes[i], waitingTimes[i], customersInQueue[i], serviceEndTimes[i],
					customerSpentTimes[i], serverIdleTimes[i] };
		}
		return table;
	}

	private static void statistics(int rows) {
		// sum columns
		intrSUM = IntStream.of(interArrivalTimes).sum();
		arrSUM = IntStream.of(arrivalTimes).sum();
		servTimeSUM = IntStream.of(serviceTimes).sum();
		servBeginTimeSUM = IntStream.of(serviceBeginTimes).sum();
		waitSUM = IntStream.of(waitingTimes).sum();
		queueSUM = IntStream.of(customersInQueue).sum();
		servEndTimeSUM = IntStream.of(serviceEndTimes).sum();
		sysTimeSUM = IntStream.of(customerSpentTimes).sum();
		idleSUM = IntStream.of(serverIdleTimes).sum();

		// print avg
		System.out.format("%n%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %n", "AVERAGE",
				intrSUM / (float) rows, arrSUM / (float) rows, servTimeSUM / (float) rows,
				servBeginTimeSUM / (float) rows, waitSUM / (float) rows, queueSUM / (float) rows,
				servEndTimeSUM / (float) rows, sysTimeSUM / (float) rows, idleSUM / (float) rows);

		// print sum
		System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %n", "SUM", intrSUM, arrSUM,
				servTimeSUM, servBeginTimeSUM, waitSUM, queueSUM, servEndTimeSUM, sysTimeSUM, idleSUM);

		// statistics
		int customersWhoWait = 0;
		for (int i = 0; i < rows; i++) {
			if (waitingTimes[i] != 0) {
				customersWhoWait++;
			}
		}

		// Averages and probabilities
		System.out.format("%n%nAverage Waiting Time in Queue: %.2f%n", (float) waitSUM / rows);
		System.out.format("Probability of Waiting: %.2f%n", (float) customersWhoWait / rows);
		System.out.format("Probability Idle Server: %.2f%n", (float) idleSUM / serviceEndTimes[rows - 1]);
		System.out.format("Average Service Time: %.2f%n", (float) servTimeSUM / rows);
		System.out.format("Average Interrarrival Time: %.2f%n", (float) intrSUM / rows - 1);
		System.out.format("Average Waiting of Those Who Wait Time: %.2f%n", (float) waitSUM / customersWhoWait); ///////
		System.out.format("Average Time a Customer Spends in System: %.2f%n", (float) sysTimeSUM / rows);
		System.out.format("Average Queue Length: %.2f%n", (float) queueSUM / rows);
		System.out.format("Service Utilization: %.2f%%%n", (1 - idleSUM / (float) serviceEndTimes[rows - 1]) * 100);
	}

	private static void option1(int rows) {
		System.out.print("Start entering interarrival times: ");
		Scanner scanner1 = new Scanner(System.in);
		for (int i = 0; i < rows; i++) {
			interArrivalTimes[i] = scanner1.nextInt();
		}
		System.out.print("Start entering service times: ");
		Scanner scanner2 = new Scanner(System.in);
		for (int i = 0; i < rows; i++) {
			serviceTimes[i] = scanner2.nextInt();
		}
	}

	private static void option2(int rows) {
////******************** RANDOMNESS **********************************
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter the size of input of interarrival times: ");
		int sizeInter = scanner.nextInt();
		// Take in inputs
		System.out.println("Start entering interarrival times:");
		for (int i = 0; i < sizeInter; i++) {
			interArrivalTimesInput[i] = scanner.nextInt();
			interArrivalTimesProb[i] = scanner.nextDouble();
		}

		System.out.println("Enter the size of input of service times: ");
		int sizeService = scanner.nextInt();
		System.out.println("Start entering service times:");
		for (int i = 0; i < sizeService; i++) {
			serviceTimesInput[i] = scanner.nextInt();
			serviceTimesProb[i] = scanner.nextDouble();
		}

		// Generate CDF table Inter
		System.out.println("\t\tCDF Table for Interarival Times\n");
		System.out.printf("%-15s %-15s %-15s%n", "Interarrival Times", "Probability", "CDF");
		double[] interArrivalCDF = new double[rows];
		for (int i = 0; i < sizeInter; i++) {
			if (i == 0) {
				interArrivalCDF[i] = interArrivalTimesProb[i];
			} else {
				interArrivalCDF[i] = interArrivalCDF[i - 1] + interArrivalTimesProb[i];
			}

			System.out.printf("%-15d %-15.2f %-15.2f%n", interArrivalTimesInput[i], interArrivalTimesProb[i],
					interArrivalCDF[i]);
		}

		// Generate CDF table Serv
		System.out.println("\t\t\n\nCDF Table for Service Times\n");
		System.out.printf("%-15s %-15s %-15s%n", "Service Times", "Probability", "CDF");
		double[] serviceCDF = new double[rows];
		for (int i = 0; i < sizeService; i++) {
			if (i == 0) {
				serviceCDF[i] = serviceTimesProb[i];
			} else {
				serviceCDF[i] = serviceCDF[i - 1] + serviceTimesProb[i];
			}
			System.out.printf("%-15d %-15.2f %-15.2f%n", serviceTimesInput[i], serviceTimesProb[i], serviceCDF[i]);
		}

		// Gen rand no from inputs&CDF
		interArrivalTimes = generateRandomNumbers(rows, interArrivalCDF, interArrivalTimesInput);
		serviceTimes = generateRandomNumbers(rows, interArrivalCDF, serviceTimesInput);
////******************** RANDOMNESS **********************************        

	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		System.out.print("Enter number of rows/customers: ");
		int rows = input.nextInt();
		interArrivalTimes = new int[rows];
		// instantiate arrays with correct size
		customer = new int[rows];
		interArrivalTimesInput = new int[rows];
		interArrivalTimesProb = new double[rows];
		arrivalTimes = new int[rows];
		serviceTimes = new int[rows];
		serviceTimesInput = new int[rows];
		serviceTimesProb = new double[rows];
		serviceBeginTimes = new int[rows];
		waitingTimes = new int[rows];
		customersInQueue = new int[rows];
		serviceEndTimes = new int[rows];
		customerSpentTimes = new int[rows];
		serverIdleTimes = new int[rows];

		System.out.println("1)option 1: Discrete Event Simulation\n2)option 2: Mont Carlo Simulation");
		int op = input.nextInt();
		if (op == 1)
			option1(rows);
		else
			option2(rows);
		
		printTable(createTable(rows));
		statistics(rows);
	}
}