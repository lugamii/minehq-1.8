// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import java.beans.ConstructorProperties;
import java.util.Iterator;
import org.bukkit.ChatColor;
import java.util.List;

public final class GraphUtil
{
    public static GraphResult getGraph(final List<Double> values) {
        final StringBuilder graph = new StringBuilder();
        double largest = 0.0;
        for (final double value : values) {
            if (value > largest) {
                largest = value;
            }
        }
        final int GRAPH_HEIGHT = 2;
        int positives = 0;
        int negatives = 0;
        for (int i = GRAPH_HEIGHT - 1; i > 0; --i) {
            final StringBuilder sb = new StringBuilder();
            for (final double index : values) {
                final double value2 = GRAPH_HEIGHT * index / largest;
                if (value2 > i && value2 < i + 1) {
                    ++positives;
                    sb.append(String.format("%s+", ChatColor.GREEN));
                }
                else {
                    ++negatives;
                    sb.append(String.format("%s-", ChatColor.RED));
                }
            }
            graph.append(sb.toString());
        }
        return new GraphResult(graph.toString(), positives, negatives);
    }
    
    private GraphUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static class GraphResult
    {
        private final String graph;
        private final int positives;
        private final int negatives;
        
        public String getGraph() {
            return this.graph;
        }
        
        public int getPositives() {
            return this.positives;
        }
        
        public int getNegatives() {
            return this.negatives;
        }
        
        @ConstructorProperties({ "graph", "positives", "negatives" })
        public GraphResult(final String graph, final int positives, final int negatives) {
            this.graph = graph;
            this.positives = positives;
            this.negatives = negatives;
        }
    }
}
