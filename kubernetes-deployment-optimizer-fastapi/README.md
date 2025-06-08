# Kubernetes Deployment Optimizer FastAPI

This project is a FastAPI application that optimizes Kubernetes deployments using simulated annealing. It provides an API to evaluate and optimize the placement of containers across nodes in a Kubernetes cluster.

## Project Structure

```
kubernetes-deployment-optimizer-fastapi
├── app
│   ├── main.py          # Entry point of the FastAPI application
│   ├── models.py        # Data models for Container and Node
│   ├── services.py      # Core logic for deployment optimization
│   └── __init__.py      # Package initialization
├── requirements.txt      # Python dependencies
├── README.md             # Project documentation
└── vercel.json           # Vercel deployment configuration
```

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd kubernetes-deployment-optimizer-fastapi
   ```

2. **Create a virtual environment:**
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows use `venv\Scripts\activate`
   ```

3. **Install dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

4. **Run the application:**
   ```bash
   uvicorn app.main:app --reload
   ```

5. **Access the API:**
   Open your browser and go to `http://127.0.0.1:8000/docs` to view the interactive API documentation.

## Usage

The API provides endpoints to optimize Kubernetes deployments. You can send requests to the endpoints defined in `app/main.py` to evaluate and optimize container placements.

## Deployment

This application can be deployed on Vercel. The configuration for Vercel is specified in the `vercel.json` file. Follow the Vercel documentation for instructions on deploying FastAPI applications.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.