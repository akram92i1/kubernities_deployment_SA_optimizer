package com.kubernetesdeploymentoptimizer.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OptimizationRequest {
  @JsonProperty("nodes")
    private List<Node> nodes;

    @JsonProperty("containers")
    private List<Container> containers;

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Container> getContainers() {
        return containers;
    }
    
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
    
    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }
}
