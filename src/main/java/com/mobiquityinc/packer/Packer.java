package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.model.Item;
import com.mobiquityinc.packer.model.Package;
import com.mobiquityinc.packer.model.PackagingScenario;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Packer {

    public static String pack(String _filename) throws APIException {
        InputReader reader = new InputReader();

        StringBuffer results = new StringBuffer();
        Packer packer = new Packer();
        reader.readFile(_filename).forEach( scenario -> {
            try {
                Package bestPackage = packer.maximizeCostOfPackage(scenario);
                results.append(bestPackage.result2String()).append(System.getProperty("line.separator"));
            } catch (APIException e) {
                results.append("Error: " + e.getMessage()).append(System.getProperty("line.separator"));
            }
        });
        return results.toString();
    }


    public Package maximizeCostOfPackage(PackagingScenario _scenario) throws APIException {
        List<Package> previousIteration = new LinkedList<>();
        List<Package> currentIteration = new LinkedList<>();

        for (Item currentItem : _scenario.getItemsToPack()) {
            currentIteration.clear();
            for (int weight = 1; weight <= _scenario.getPackageMaxWeight(); weight++) {
                Package currentPackage = (previousIteration.size() < weight ?
                        new Package(weight) :
                        previousIteration.get(weight - 1).clone());
                // first we make sure the item fits in this package (considering it as being empty)
                if (currentItem.getWeight() <= currentPackage.getTotalWeight()) {
                    if (currentItem.getWeight() <= currentPackage.getFreeWeight()) {
                        // the item fits in the free weight of the package!
                        currentPackage.addItem(currentItem);
                    } else {
                        // check if removing the item's weight to fit it is better then ignoring the item
                        int minPackageWeight =  (int)Math.floor(currentPackage.getTotalWeight()-currentItem.getWeight());
                        boolean updateCurrentPackage = false;
                        Package previousPackageWithRoomForItem = null;
                        if (minPackageWeight == 0) {
                            // if element must be put alone, then its cost must be greater than the current package cost
                            updateCurrentPackage = currentItem.getCost() > currentPackage.getTotalCost();
                        } else {
                            // if element might be combined with another, we need to check previous packages. Since weights
                            // are floating point numbers, we must test the "floor" & "ceiling" of package.maxWeight - item.weight,
                            // because in the "ceiling" there might be room for the item.
                            for (int j = 0; j <= 1; j++) {
                                previousPackageWithRoomForItem = previousIteration.get(minPackageWeight - j);
//                                Package previousPackage = previousIteration.get(weight - 1);
                                // we can update the current package using the previous + this item if:
                                //  1) the previous package has room (=free weight) for this item AND
                                //  2) the previous package cost + this item cost is greater then the current package cost
                                updateCurrentPackage = (
                                        ((previousPackageWithRoomForItem.getUsedWeight() + currentItem.getWeight()) <= currentPackage.getTotalWeight()) &&
                                        ((previousPackageWithRoomForItem.getTotalCost() + currentItem.getCost()) > currentPackage.getTotalCost())
                                );
                                // stop if we found a combination where the current package can be updated
                                if (updateCurrentPackage) { break; }
                            }
                        }

                        if (updateCurrentPackage) {
                            currentPackage.resetPackage();
                            currentPackage.addItem(currentItem);
                            if (previousPackageWithRoomForItem != null) {
                                currentPackage.addAllItems(previousPackageWithRoomForItem.getItensInside());
                            }
                        }
                    }
                }
                currentIteration.add(currentPackage);
            }
            // last iteration becames the previous one, and we start over again
            previousIteration.clear();
            previousIteration.addAll(currentIteration);
        }

        // now we need to find the best option with the highest cost inside
        Package bestPackage = new Package(0);
        for (int index = currentIteration.size()-1; index > 0; index--) {
            if (currentIteration.get(index).getTotalCost() >= bestPackage.getTotalCost()) {
                bestPackage = currentIteration.get(index);
            }
        }
        return bestPackage;
    }
}
