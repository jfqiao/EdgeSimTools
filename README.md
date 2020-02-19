# MobileEdgeSim: A set of simulator for edge computing

This tools contains three simulator for edge computing in different scenarios.

## Three edge computing scenarios

### Computing Power Allocation and Traffic Scheduling for Edge Service Provisioning

In the mobile edge computing scenario, services are usually deployed on multiple edge servers on the edge platform, and the 
edge server and cloud server jointly provide external services. When the edge device initiates a service request, after 
receiving the request, the access edge server forwards the service to the appropriate server for execution according to the 
forwarding policy of the edge platform, and obtains the final service result. This forwarding strategy minimizes request delays
while balancing the costs associated with the resources needed to deploy the service. Therefore, in this system, the cost of 
the resources required for deployment and the average request delay are considered to obtain a service forwarding strategy 
and a deployment strategy.

### Burst Load Evacuation Based on Dispatching and Scheduling In Edge Network

In this problem, N edge servers are connected to each other according to a certain topology. Each edge server can receive the 
task request from the mobile terminal, forward the task request as a route, and process the task request. For mobile edge 
scenarios, when there are a large number of task requests in a certain area, the local edge server cannot effectively handle 
the problem of large number of task requests. Design and use algorithms to find the optimal evacuation strategy. In order to 
achieve the purpose of minimizing the time to complete all tasks.

### Distributed Redundancy Scheduling for Microservice-based Applications at the Edge

Multi-access Edge Computing (MEC) is booming as a promising paradigm to push the computation and communication resources from 
cloud to the network edge to provide services and perform computations. With container technologies, MEC enables mobile devices
with limited battery capacity and small memory footprint to run composite microservice-based applications without 
time-consuming backbone transmission. Service deployment at the edge is of importance to put MEC from theory into practice. 
However, the state-of-the-art researches do not take the composite property of services into consideration. Besides, although
Kubernetes has certain abilities to heal container failures, high availability of deployed microservices can not be ensured 
due to the heterogeneity and variability of edge sites.