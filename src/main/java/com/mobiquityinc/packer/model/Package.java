package com.mobiquityinc.packer.model;

import com.mobiquityinc.exception.APIException;

import java.util.Collection;
import java.util.LinkedList;

public class Package implements Cloneable {

    private int totalWeight;
    private double freeWeight;
    private int totalCost;
    private Collection<Item> itensInside;

    public Package() {
        this(0);
    }

    public Package(int _totalWeight) {
        this.totalWeight = _totalWeight;
        this.freeWeight = _totalWeight;
        this.totalCost = 0;
        this.itensInside = new LinkedList<>();
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int _totalWeight) {
        this.totalWeight = _totalWeight;
    }

    public double getFreeWeight() {
        return freeWeight;
    }

    public void setFreeWeight(double _freeWeight) {
        this.freeWeight = _freeWeight;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int _totalCost) {
        this.totalCost = _totalCost;
    }

    public Collection<Item> getItensInside() {
        return itensInside;
    }

    public double getUsedWeight() {
        return this.totalWeight - this.freeWeight;
    }

    public void resetPackage() {
        this.totalCost = 0;
        this.freeWeight = this.totalWeight;
        this.itensInside.clear();
    }

    public void addAllItems(Collection<Item> _multipleItems) throws APIException {
        for (Item eachItem : _multipleItems) {
            this.addItem(eachItem);
        }
    }

    public void addItem(Item _newItem) throws APIException {
        // sanity check!
        if (this.freeWeight < _newItem.getWeight()) {
            throw new APIException("Adding an item that is heavier than the current free weight of this package!");
        }
        this.itensInside.add(_newItem);
        this.freeWeight -= _newItem.getWeight();
        this.totalCost += _newItem.getCost();
    }

    public String result2String() {
        if (this.itensInside.isEmpty()) {
            return "-";
        } else {
            StringBuffer buf = new StringBuffer();
            for (Item eachItem: this.itensInside) {
                buf.append(eachItem.getIndex()).append(",");
            }
            return buf.deleteCharAt(buf.length()-1).toString();
        }
    }


    @Override
    public Package clone() {
        Package anotherPackage = new Package(this.totalWeight);
        anotherPackage.setFreeWeight(this.freeWeight);
        anotherPackage.setTotalCost(this.totalCost);
        this.itensInside.forEach(eachItem -> {
            anotherPackage.getItensInside().add(eachItem);
        });
        return anotherPackage;
    }
}
