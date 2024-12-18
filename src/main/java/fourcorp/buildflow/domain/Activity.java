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

    // Construtor atualizado
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


    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCostUnit() {
        return costUnit;
    }

    public void setCostUnit(String costUnit) {
        this.costUnit = costUnit;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public int getEarlyStart() {
        return earlyStart;
    }

    public void setEarlyStart(int earlyStart) {
        this.earlyStart = earlyStart;
    }

    public int getEarlyFinish() {
        return earlyFinish;
    }

    public void setEarlyFinish(int earlyFinish) {
        this.earlyFinish = earlyFinish;
    }

    public int getLateStart() {
        return lateStart;
    }

    public void setLateStart(int lateStart) {
        this.lateStart = lateStart;
    }

    public int getLateFinish() {
        return lateFinish;
    }

    public void setLateFinish(int lateFinish) {
        this.lateFinish = lateFinish;
    }

    public int getSlack() {
        return lateStart - earlyStart;
    }
}