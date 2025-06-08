from pydantic import BaseModel , field_validator
from typing import List, Dict

from pydantic import BaseModel
from typing import Tuple, Mapping

class Node(BaseModel):
    name: str
    cpu_capacity: float
    memory_capacity: float
    labels: Tuple[str, ...] = ()                # e.g., ("ssd", "gpu", "zone-a")
    taints: Tuple[str, ...] = ()                # e.g., ("NoSchedule", "NoExecute")
    available: bool = True                      # Node availability status
    disk_capacity: float = 0.0                  # Disk size in GB
    network_bandwidth: float = 0.0              # Network bandwidth in Gbps
    zone: str = ""                              # For topology/affinity
    custom_attributes: Tuple[Tuple[str, str], ...] = ()   # Any other custom attributes
    @field_validator("custom_attributes", mode="before")
    @classmethod
    def convert_custom_attributes(cls, v: any) -> Tuple[Tuple[str, str], ...]:
        if isinstance(v, dict):
            return tuple(v.items())
        return v

    class Config:
        frozen = True

from typing import Tuple, Mapping

class Container(BaseModel):
    name: str
    cpu_request: float
    cpu_limit: float
    memory_request: float
    memory_limit: float
    memory_requirement: float
    disk_request: float = 0.0 
    affinity: Tuple[str, ...] = ()
    anti_affinity: Tuple[str, ...] = ()
    required_labels: Tuple[str, ...] = ()
    priority: int = 0
    class Config:
        frozen = True

class DeploymentRequest(BaseModel):
    nodes: List[Node]
    containers: List[Container]