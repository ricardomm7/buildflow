package fourcorp.buildflow.domain;

import fourcorp.buildflow.application.Clock;

/**
 * Represents a workstation that processes products with a specific execution time.
 * It tracks its availability, operation count, and total operation time.
 * Implements the Identifiable interface for unique identification.
 */
public class Workstation implements Identifiable<String> {
    private final String idMachine;
    private int time;
    private boolean isAvailable;
    private int oprCounter;
    private double totalOper;
    private Clock clock = new Clock();

    /**
     * Constructs a Workstation with the specified machine ID and processing time.
     *
     * @param idMachine the unique identifier for the workstation
     * @param time the estimated time to process a product in seconds
     */
    public Workstation(String idMachine, int time) {
        this.idMachine = idMachine;
        this.time = time;
        this.isAvailable = true;
        this.oprCounter = 0;
        this.totalOper = 0;
    }


    /**
     * Starts the clock for the workstation and sets its availability status.
     * Increments the operation counter and starts a countdown for the specified time.
     *
     * @param hasMoreOperation indicates if there are more operations to process
     */
    public void startClock(boolean hasMoreOperation) {
        this.isAvailable = false;
        clock.countDownClock(this.time, () -> {
            this.isAvailable = true;
            increaseOpCounter();
            if (hasMoreOperation) {
                clock.countUpClock(true); // Começa a contagem ascendente se ainda houver operações
            }
        });
    }

    /**
     * Calculates the average execution time per operation.
     *
     * @return a string representation of the average execution time
     */
    public String getAverageExecutionTimePerOperation() {
        double tempo = totalOper / oprCounter;
        return " ---Workstation " + idMachine + "---Average Execution Time = " + tempo + "\n";
    }

    /**
     * Processes a product by increasing the operation counter,
     * displaying processing information, and starting the clock.
     *
     * @param product the product to be processed
     */
    public void processProduct(Product product) {
        increaseOpCounter();
        System.out.println("Processing product " + product.getId() + " in machine " + idMachine + " - Estimated time: " + time + " sec");
        increaseOperationTime();
        startClock(product.hasMoreOperations());
    }

    /**
     * Increases the total operation time by the workstation's time.
     */
    public void increaseOperationTime() {
        totalOper = totalOper + time;
    }

    /**
     * Increases the operation counter by one.
     */
    public void increaseOpCounter() {
        this.oprCounter = oprCounter + 1;
    }

    /**
     * Gets the operation counter.
     *
     * @return the number of operations processed
     */
    public int getOprCounter() {
        return oprCounter;
    }

    /**
     * Gets the estimated time for processing a product.
     *
     * @return the estimated processing time in seconds
     */
    public double getTime() {
        return time;
    }

    /**
     * Sets the estimated time for processing a product.
     *
     * @param time the estimated processing time in seconds to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Checks if the workstation is available for processing.
     *
     * @return true if the workstation is available, false otherwise
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the workstation.
     *
     * @param available the availability status to set
     */
    public synchronized void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Gets the total operation time of the workstation.
     *
     * @return the total time spent on operations
     */
    public double getTotalOperationTime() {
        return totalOper;
    }

    /**
     * Sets the total operation time for the workstation.
     *
     * @param a the total operation time to set
     */
    public void setTotalOperationTime(double a) {
        this.totalOper = a;
    }

    /**
     * Gets the unique identifier for the workstation, implementing the Identifiable interface.
     *
     * @return the workstation ID
     */
    @Override
    public String getId() {
        return idMachine;
    }

    /**
     * Compares this workstation with another object for equality.
     *
     * @param o the object to compare
     * @return true if the workstations are considered equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workstation that = (Workstation) o;
        return idMachine.equalsIgnoreCase(that.idMachine);
    }

    /**
     * Returns a hash code value for the workstation.
     *
     * @return the hash code for the workstation
     */
    @Override
    public int hashCode() {
        return idMachine.hashCode();
    }
}
