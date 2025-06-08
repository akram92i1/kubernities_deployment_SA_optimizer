from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict
from .models import Node, Container, DeploymentRequest
import random
import math
import time 
from colorama import  Fore, Style
app = FastAPI()

class Container(BaseModel):
    name: str
    cpu_requirement: float
    memory_requirement: float

class Node(BaseModel):
    name: str
    cpu_capacity: float
    memory_capacity: float

def random_placement(containers: List[Container], nodes: List[Node]) -> Dict[Container, Node]:
    placement = {}
    for container in containers:
        random_node = random.choice(nodes)
        print(f"Placing container {container.name} on node {random_node.name}")
        containerIndex = containers.index(container)
        placement[container] = random_node
        print("Placement so far:", {c.name: n.name for c, n in placement.items()})
        time.sleep(0.1)  # Simulate some processing delay
    return placement

def evaluate_placement(placement: Dict[Container, Node]) -> float:
    cpu_utilization = {node: 0.0 for node in set(placement.values())}
    print(Fore.BLUE,f"Initial CPU utilization: {cpu_utilization}")
    memory_utilization = {node: 0.0 for node in set(placement.values())}
    disk_utilization = {node: 0.0 for node in set(placement.values())}
    print( Fore.GREEN, f"Initial disk_utilization {disk_utilization}",Style.RESET_ALL)
    affinity_penalty = 0
    anti_affinity_penalty = 0
    label_penalty = 0
    unavailable_penalty = 0

    # Resource usage and constraint checks
    for container, node in placement.items():
        print(f"Evaluating container {container.name} on node {node.name}")
        cpu_utilization[node] += container.cpu_request
        memory_utilization[node] += container.memory_request
        disk_utilization[node] += getattr(container, "disk_request", 0.0)

        # Node availability
        if not node.available:
            unavailable_penalty += 100

        # Required labels
        if hasattr(container, "required_labels"):
            if not all(label in node.labels for label in container.required_labels):
                label_penalty += 50

    # Affinity: containers with the same affinity label should be together
    affinity_groups = {}
    for container, node in placement.items():
        for label in getattr(container, "affinity", []):
            affinity_groups.setdefault(label, set()).add(node)
    for label, nodeset in affinity_groups.items():
        if len(nodeset) > 1:
            affinity_penalty += (len(nodeset) - 1) * 10

    # Anti-affinity: containers with the same anti-affinity label should not be together
    anti_affinity_groups = {}
    for container, node in placement.items():
        for label in getattr(container, "anti_affinity", []):
            anti_affinity_groups.setdefault(label, []).append(node)
    for label, nodelist in anti_affinity_groups.items():
        if len(nodelist) != len(set(nodelist)):
            anti_affinity_penalty += 20

    cpu_std_dev = calculate_standard_deviation(list(cpu_utilization.values()))
    memory_std_dev = calculate_standard_deviation(list(memory_utilization.values()))
    disk_std_dev = calculate_standard_deviation(list(disk_utilization.values()))

    # The total score includes penalties for constraint violations
    return (
        cpu_std_dev
        + memory_std_dev
        + disk_std_dev
        + affinity_penalty
        + anti_affinity_penalty
        + label_penalty
        + unavailable_penalty
    )

def calculate_standard_deviation(values: List[float]) -> float:
    mean = sum(values) / len(values) if values else 0.0
    variance = sum((x - mean) ** 2 for x in values) / len(values) if values else 0.0
    return math.sqrt(variance)

def simulated_annealing(containers: List[Container], nodes: List[Node], initial_solution: Dict[Container, Node]) -> Dict[Container, Node]:
    temperature = 5000.0
    cooling_rate = 0.995
    current_solution = initial_solution.copy()
    current_energy = evaluate_placement(current_solution)
    best_solution = current_solution.copy()
    best_energy = current_energy

    iterations = 0
    while temperature > 1.0 and iterations < 500000:
        new_solution = current_solution.copy()
        container_to_move = random.choice(containers)
        available_nodes = [node for node in nodes if node != current_solution[container_to_move]]

        if available_nodes:
            new_node = random.choice(available_nodes)
            new_solution[container_to_move] = new_node
            new_energy = evaluate_placement(new_solution)
            delta_e = new_energy - current_energy

            if delta_e < 0 or random.random() < math.exp(-delta_e / temperature):
                current_solution = new_solution
                current_energy = new_energy
                if current_energy < best_energy:
                    best_solution = current_solution.copy()
                    best_energy = current_energy

        temperature *= cooling_rate
        iterations += 1

    return best_solution


@app.get("/")
def read_root():
    return "Hello World! This is the Deployment Optimizer API."

@app.post("/optimize-deployment")
def optimize_deployment(request: DeploymentRequest):
    containers = request.containers
    nodes = request.nodes
    print(f"Received {len(containers)} containers and {len(nodes)} nodes for optimization.")
    time.sleep(3)  
    initial_placement = random_placement(containers, nodes)
    initial_score = evaluate_placement(initial_placement)
    optimized_placement = simulated_annealing(containers, nodes, initial_placement)
    optimized_score = evaluate_placement(optimized_placement)

    return {
        "initialScore": initial_score,
        "optimizedScore": optimized_score,
        "initialPlacement": {container.name: node.name for container, node in initial_placement.items()},
        "optimizedPlacement": {container.name: node.name for container, node in optimized_placement.items()}
    }