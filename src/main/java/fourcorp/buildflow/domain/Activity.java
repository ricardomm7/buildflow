package fourcorp.buildflow.domain;

import java.util.List;

public class Activity {
    private String id;
    private String name;
    private int duration;
    private String durationUnit;
    private double cost;
    private String costUnit;
    private List<String> dependencies;
    private int earlyStart, earlyFinish;
    private int lateStart, lateFinish;

    /**
     * Constructs an Activity with the specified attributes.
     *
     * @param id           the unique identifier for the activity.
     * @param name         the name of the activity.
     * @param duration     the duration of the activity.
     * @param durationUnit the unit of the activity's duration (e.g., days, hours).
     * @param cost         the cost associated with the activity.
     * @param costUnit     the unit of the cost (e.g., dollars, euros).
     * @param dependencies a list of activity IDs this activity depends on.
     */
    public Activity(String id, String name, int duration, String durationUnit, double cost, String costUnit, List<String> dependencies) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.cost = cost;
        this.costUnit = costUnit;
        this.dependencies = dependencies;
        this.earlyStart = 0;
        this.earlyFinish = 0;
        this.lateStart = Integer.MAX_VALUE;
        this.lateFinish = Integer.MAX_VALUE;
    }

    /**
     * Gets the ID of the activity.
     *
     * @return the activity ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the activity.
     *
     * @param id the new ID of the activity.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the activity.
     *
     * @return the activity name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the activity.
     *
     * @param name the new name of the activity.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the duration of the activity.
     *
     * @return the activity duration.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the activity.
     *
     * @param duration the new duration of the activity.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the duration unit of the activity.
     *
     * @return the duration unit.
     */
    public String getDurationUnit() {
        return durationUnit;
    }

    /**
     * Sets the duration unit of the activity.
     *
     * @param durationUnit the new duration unit.
     */
    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    /**
     * Gets the cost of the activity.
     *
     * @return the activity cost.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost of the activity.
     *
     * @param cost the new cost of the activity.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Gets the cost unit of the activity.
     *
     * @return the cost unit.
     */
    public String getCostUnit() {
        return costUnit;
    }

    /**
     * Sets the cost unit of the activity.
     *
     * @param costUnit the new cost unit.
     */
    public void setCostUnit(String costUnit) {
        this.costUnit = costUnit;
    }

    /**
     * Gets the list of dependencies for the activity.
     *
     * @return the list of dependencies.
     */
    public List<String> getDependencies() {
        return dependencies;
    }

    /**
     * Sets the dependencies for the activity.
     *
     * @param dependencies the new list of dependencies.
     */
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Gets the early start time of the activity.
     *
     * @return the early start time.
     */
    public int getEarlyStart() {
        return earlyStart;
    }

    /**
     * Sets the early start time of the activity.
     *
     * @param earlyStart the new early start time.
     */
    public void setEarlyStart(int earlyStart) {
        this.earlyStart = earlyStart;
    }

    /**
     * Gets the early finish time of the activity.
     *
     * @return the early finish time.
     */
    public int getEarlyFinish() {
        return earlyFinish;
    }

    /**
     * Sets the early finish time of the activity.
     *
     * @param earlyFinish the new early finish time.
     */
    public void setEarlyFinish(int earlyFinish) {
        this.earlyFinish = earlyFinish;
    }

    /**
     * Gets the late start time of the activity.
     *
     * @return the late start time.
     */
    public int getLateStart() {
        return lateStart;
    }

    /**
     * Sets the late start time of the activity.
     *
     * @param lateStart the new late start time.
     */
    public void setLateStart(int lateStart) {
        this.lateStart = lateStart;
    }

    /**
     * Gets the late finish time of the activity.
     *
     * @return the late finish time.
     */
    public int getLateFinish() {
        return lateFinish;
    }

    /**
     * Sets the late finish time of the activity.
     *
     * @param lateFinish the new late finish time.
     */
    public void setLateFinish(int lateFinish) {
        this.lateFinish = lateFinish;
    }

    /**
     * Calculates the slack time for the activity (difference between late start and early start).
     *
     * @return the slack time.
     */
    public int getSlack() {
        return lateStart - earlyStart;
    }
}