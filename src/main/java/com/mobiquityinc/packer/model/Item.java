package com.mobiquityinc.packer.model;

public class Item {

    private int index;
    private double weight;
    private int cost;

    public Item(int index, double weight, int cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int _index) {
        this.index = _index;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double _weight) {
        this.weight = _weight;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int _cost) {
        this.cost = _cost;
    }

    @Override
    public String toString() {
        return "Item{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }
}
