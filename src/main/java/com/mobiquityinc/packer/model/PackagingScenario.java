package com.mobiquityinc.packer.model;

import java.util.Collection;
import java.util.LinkedList;

public class PackagingScenario {

    private int packageMaxWeight;
    private Collection<Item> itemsToPack;
    private Collection<Item> overweightedItems;


    public PackagingScenario(int _maxWeight) {
        this.packageMaxWeight = _maxWeight;
        this.itemsToPack = new LinkedList<>();
        this.overweightedItems = new LinkedList<>();
    }

    public int getPackageMaxWeight() {
        return packageMaxWeight;
    }

    public void setPackageMaxWeight(int _packageMaxWeight) {
        this.packageMaxWeight = _packageMaxWeight;
    }

    public Collection<Item> getItemsToPack() {
        return itemsToPack;
    }

    public void setItemsToPack(Collection<Item> _itemsToPack) {
        this.itemsToPack = _itemsToPack;
    }

    public int getOverweightItemsCount() {
        return this.overweightedItems.size();
    }

    /**
     * If the item weights more then the package max allowed weight, then save it to a separate list. This way we don't
     * need to wast calculations on an item that will never fit the package.
     */
    public void addItem(Item _item) {
        if (this.packageMaxWeight < _item.getWeight()) {
            this.overweightedItems.add(_item);
        } else {
            this.itemsToPack.add(_item);
        }
    }
}
