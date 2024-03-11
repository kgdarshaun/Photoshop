package org.sfu.dka101.utils;

import java.util.List;

public class Entropy {
    public static double getEntropy(List<Integer> frequencyList) {
        long totalCount = frequencyList.stream().mapToInt(Integer::intValue).sum();
        return frequencyList.stream()
                .map(frequency -> {
                    double probability = (double) frequency / totalCount;
                    return (-probability) * (Math.log(probability)) / Math.log(2);
                })
                .reduce(0.0, Double::sum);
    }
}
