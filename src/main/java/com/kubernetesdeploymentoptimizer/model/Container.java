package com.kubernetesdeploymentoptimizer.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Container {

    @JsonProperty("name")
    private String name;

    @JsonProperty("cpuRequirement")
    private double cpuRequirement;

    @JsonProperty("memoryRequirement")
    private double memoryRequirement;

    public Container(
        @JsonProperty("name") String name, 
        @JsonProperty("cpuRequirement") double cpuRequirement, 
        @JsonProperty("memoryRequirement") double memoryRequirement
    ) {
        this.name = name;
        this.cpuRequirement = cpuRequirement;
        this.memoryRequirement = memoryRequirement;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("cpuRequirement")
    public double getCpuRequirement() {
        return cpuRequirement;
    }

    @JsonProperty("memoryRequirement")
    public double getMemoryRequirement() {
        return memoryRequirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Container container = (Container) o;
        return Double.compare(container.cpuRequirement, cpuRequirement) == 0 &&
               Double.compare(container.memoryRequirement, memoryRequirement) == 0 &&
               Objects.equals(name, container.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cpuRequirement, memoryRequirement);
    }
}
