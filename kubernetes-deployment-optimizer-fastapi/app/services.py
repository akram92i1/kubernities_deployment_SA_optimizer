from typing import List, Dict
import random
import math

class Container:
    def __init__(self, name: str, cpu_requirement: float, memory_requirement: float):
        self.name = name
        self.cpu_requirement = cpu_requirement
        self.memory_requirement = memory_requirement

class Node:
    def __init__(self, name: str, cpu_capacity: float, memory_capacity: float):
        self.name = name
        self.cpu_capacity = cpu_capacity
        self.memory_capacity = memory_capacity

def optimize_deployment(nodes: List[Node], containers: List[Container]) -> Dict[str, object]:
    if not nodes or not containers:
        raise ValueError("Nodes and containers cannot be empty")

    initial_placement = random_placement(containers, nodes)
    initial_score = evaluate_placement(initial_placement)
    optimized_placement = simulated_annealing(containers, nodes, initial_placement)
    optimized_score = evaluate_placement(optimized_placement)

    response = {
        "initialScore": initial_score,
        "optimizedScore": optimized_score,
        "initialPlacement": format_placement(initial_placement),
        "optimizedPlacement": format_placement(optimized_placement)
    }
    return response

def simulated_annealing(containers: List[Container], nodes: List[Node], initial_solution: Dict[Container, Node]) -> Dict[Container, Node]:
    temperature = 1000.0
    cooling_rate = 0.995

    current_solution = initial_solution.copy()
    current_energy = evaluate_placement(current_solution)

    best_solution = current_solution.copy()
    best_energy = current_energy

    iterations = 0
    while temperature > 1.0 and iterations < 10000:
        new_solution = current_solution.copy()
        container_to_move = random.choice(containers)

        available_nodes = [node for node in nodes if new_solution[container_to_move] != node]

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

def evaluate_placement(placement: Dict[Container, Node]) -> float:
    cpu_utilization = {node: 0.0 for node in set(placement.values())}
    memory_utilization = {node: 0.0 for node in set(placement.values())}

    for container, node in placement.items():
        cpu_utilization[node] += container.cpu_requirement
        memory_utilization[node] += container.memory_requirement

    cpu_std_dev = calculate_standard_deviation(cpu_utilization.values())
    memory_std_dev = calculate_standard_deviation(memory_utilization.values())
    return cpu_std_dev + memory_std_dev

def calculate_standard_deviation(values: List[float]) -> float:
    mean = sum(values) / len(values) if values else 0.0
    variance = sum((value - mean) ** 2 for value in values) / len(values) if values else 0.0
    return math.sqrt(variance)

def random_placement(containers: List[Container], nodes: List[Node]) -> Dict[Container, Node]:
    placement = {}
    for container in containers:
        random_node = random.choice(nodes)
        placement[container] = random_node
    return placement

def format_placement(placement: Dict[Container, Node]) -> Dict[str, str]:
    return {container.name: node.name for container, node in placement.items()}