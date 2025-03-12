package com.kubernetesdeploymentoptimizer.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Node {
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("cpuCapacity")
    private double cpuCapacity;    // In CPU cores

    @JsonProperty("memoryCapacity")
    private double memoryCapacity; // In GB
    
    public Node(String name, double cpuCapacity, double memoryCapacity) {
        this.name = name;
        this.cpuCapacity = cpuCapacity;
        this.memoryCapacity = memoryCapacity;
    }
    
    public String getName() {
        return name;
    }
    
    public double getCpuCapacity() {
        return cpuCapacity;
    }
    
    public double getMemoryCapacity() {
        return memoryCapacity;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
