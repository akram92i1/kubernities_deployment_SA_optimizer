import pytest
from pydantic import ValidationError
from .models import Container

# app/test_models.py


def test_container_valid():
    c = Container(name="web", cpu_requirement=1.5, memory_requirement=2.0)
    assert c.name == "web"
    assert c.cpu_requirement == 1.5
    assert c.memory_requirement == 2.0

def test_container_invalid_cpu_type():
    with pytest.raises(ValidationError):
        Container(name="web", cpu_requirement="not_a_float", memory_requirement=2.0)

def test_container_invalid_memory_type():
    with pytest.raises(ValidationError):
        Container(name="web", cpu_requirement=1.0, memory_requirement="not_a_float")

def test_container_missing_name():
    with pytest.raises(ValidationError):
        Container(cpu_requirement=1.0, memory_requirement=2.0)

def test_container_missing_cpu_requirement():
    with pytest.raises(ValidationError):
        Container(name="web", memory_requirement=2.0)

def test_container_missing_memory_requirement():
    with pytest.raises(ValidationError):
        Container(name="web", cpu_requirement=1.0)