package com.kubernetesdeploymentoptimizer.apicontroller;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kubernetesdeploymentoptimizer.model.Container;
import com.kubernetesdeploymentoptimizer.model.Node;
import com.kubernetesdeploymentoptimizer.model.OptimizationRequest;
import com.kubernetesdeploymentoptimizer.service.deploymentservice;

@RestController
@RequestMapping("/api")


public class ApiController {

    private final deploymentservice dplmtService;

    // Constructor-based Dependency Injection
    public ApiController(deploymentservice dplmtService) {
        this.dplmtService = dplmtService;
    }


    @GetMapping("/")
    public String home() {
        return "Welcome to Kubernetes Deployment Optimizer API!";
    }

    // Accept JSON request from React frontend
    // Endpoint to trigger microservice deployment optimization
    @PostMapping(value="/deploy" ,consumes = { MediaType.APPLICATION_JSON_VALUE, "application/json;charset=UTF-8" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deployMicroservices(@RequestBody OptimizationRequest  request) {
        List<Node> nodes = request.getNodes();
        List<Container> containers = request.getContainers();
         // Validate input data
         if (nodes.isEmpty() || containers.isEmpty()) {
            throw new IllegalArgumentException("#### Nodes and Containers cannot be empty ##### ");
        }
        return this.dplmtService.optimizeDeployment(request.getNodes() , request.getContainers());
    }
}