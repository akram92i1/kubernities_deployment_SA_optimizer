    package com.kubernetesdeploymentoptimizer.service ;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collection;
    import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.kubernetesdeploymentoptimizer.model.Container;
import com.kubernetesdeploymentoptimizer.model.Node;


    @Service

    public class deploymentservice {
    
        public Map<String,Object> optimizeDeployment(List<Node> nodes, List<Container> containers){
            if (nodes.isEmpty() || containers.isEmpty()) {
                throw new IllegalArgumentException("Nodes and containers cannot be empty");
            }

            System.out.println("The data was well transfered");

            // // Create a cluster with nodes
            // List<Node> nodes = createSampleCluster();
            // // Create sample microservices to deploy
            // List<Container> containers = createSampleWorkload();
            // Initial random deployment
            Map<Container, Node> initialPlacement = randomPlacement(containers, nodes);
            double initialScore = evaluatePlacement(initialPlacement);
            // Optimize using simulated annealing
            Map<Container, Node> optimizedPlacement = simulatedAnnealing(containers, nodes, initialPlacement);
            double optimizedScore = evaluatePlacement(optimizedPlacement);
            // Prepare response
            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("initialScore", initialScore);
            response.put("optimizedScore", optimizedScore);
            response.put("initialPlacement", formatPlacement(initialPlacement));
            response.put("optimizedPlacement", formatPlacement(optimizedPlacement));
            return response;
        }

        // Simulated Annealing optimization
    private Map<Container, Node> simulatedAnnealing(List<Container> containers, List<Node> nodes, Map<Container, Node> initialSolution) {
        Random random = new Random();
        double temperature = 1000.0;
        double coolingRate = 0.995;

        Map<Container, Node> currentSolution = new HashMap<>(initialSolution);
        double currentEnergy = evaluatePlacement(currentSolution);

        Map<Container, Node> bestSolution = new HashMap<>(currentSolution);
        double bestEnergy = currentEnergy;

        int iterations = 0;
        while (temperature > 1.0 && iterations < 10000) {
            // Create a new candidate solution
            Map<Container, Node> newSolution = new HashMap<>(currentSolution);
            Container containerToMove = containers.get(random.nextInt(containers.size()));

            // Move to a different node
            List<Node> availableNodes = new ArrayList<>(nodes);
            availableNodes.remove(currentSolution.get(containerToMove));

            if (!availableNodes.isEmpty()) {
                Node newNode = availableNodes.get(random.nextInt(availableNodes.size()));
                newSolution.put(containerToMove, newNode);

                // Calculate energy (lower is better)
                double newEnergy = evaluatePlacement(newSolution);
                double deltaE = newEnergy - currentEnergy;

                if (deltaE < 0 || random.nextDouble() < Math.exp(-deltaE / temperature)) {
                    currentSolution = newSolution;
                    currentEnergy = newEnergy;
                    if (currentEnergy < bestEnergy) {
                        bestSolution = new HashMap<>(currentSolution);
                        bestEnergy = currentEnergy;
                    }
                }
            }
            temperature *= coolingRate;
            iterations++;
        }
        return bestSolution;
    }

    private double evaluatePlacement(Map<Container, Node> placement) {
        double totalCost = 0.0;
        Map<Node, Double> cpuUtilization = new HashMap<>();
        Map<Node, Double> memoryUtilization = new HashMap<>();

        for (Node node : placement.values()) {
            cpuUtilization.put(node, 0.0);
            memoryUtilization.put(node, 0.0);
        }

        for (Map.Entry<Container, Node> entry : placement.entrySet()) {
            Container container = entry.getKey();
            Node node = entry.getValue();

            cpuUtilization.put(node, cpuUtilization.get(node) + container.getCpuRequirement());
            memoryUtilization.put(node, memoryUtilization.get(node) + container.getMemoryRequirement());
        }

        double cpuStdDev = calculateStandardDeviation(cpuUtilization.values());
        double memoryStdDev = calculateStandardDeviation(memoryUtilization.values());
        return cpuStdDev + memoryStdDev;
    }

    private double calculateStandardDeviation(Collection<Double> values) {
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = values.stream().mapToDouble(value -> Math.pow(value - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

    // Generate initial random placement
    private static Map<Container, Node> randomPlacement(List<Container> containers, List<Node> nodes) {
        Map<Container, Node> placement = new HashMap<>();
        Random random = new Random();
        
        for (Container container : containers) {
            Node randomNode = nodes.get(random.nextInt(nodes.size()));
            placement.put(container, randomNode);
        }
        return placement;
    }

    private Map<String, String> formatPlacement(Map<Container, Node> placement) {
        Map<String, String> formatted = new HashMap<>();
        for (Map.Entry<Container, Node> entry : placement.entrySet()) {
            formatted.put(entry.getKey().getName(), entry.getValue().getName());
        }
        return formatted;
    }

    private List<Node> createSampleCluster() {
        return Arrays.asList(
                new Node("node-1", 8.0, 32.0),
                new Node("node-2", 4.0, 16.0),
                new Node("node-3", 16.0, 64.0),
                new Node("node-4", 8.0, 32.0)
        );
    }

    // Create sample microservices to deploy
    private static List<Container> createSampleWorkload() {
        List<Container> containers = new ArrayList<>();
        
        containers.add(new Container("frontend", 1.0, 2.0));
        containers.add(new Container("backend-api", 2.0, 4.0));
        containers.add(new Container("database-master", 4.0, 8.0));
        containers.add(new Container("database-replica-1", 2.0, 4.0));
        containers.add(new Container("database-replica-2", 2.0, 4.0));
        containers.add(new Container("cache", 2.0, 16.0));
        containers.add(new Container("auth-service", 1.0, 2.0));
        containers.add(new Container("logging", 0.5, 4.0));
        containers.add(new Container("monitoring", 1.0, 2.0));
        containers.add(new Container("batch-processor", 3.0, 8.0));
        return containers;
    }

    }
    