# MobileEdgeSim: A set of simulators for edge computing

The architecture of EdgeSimTools:

![image](https://github.com/jfqiao/EdgeSimTools/tree/master/docs/img/Arch.png)


- The **Core** layer is the basement of EdgeSimTools. It provides an event driven mechanism that guides how to listen and response to the events produced by all possible entities of the system with a global clock.

- The **Server** layer is an abstract of entities related to servers. It provides interfaces for upper layers so that their resources can be allocated for different purposes.

- The **Strategy** layer is a collection of related strategies (resource allocation and traffic scheduling). By implementing the interfaces declared in this layer, users can manage the behaviors of servers.

- The **Network** layer describes the properties of servers and how these servers can communicate with each other.

- The **Device** layer describes the properties of the devices and how will they produce service requests.

- The **Application** layer describes the structure of applications (with service composition techniques) and the properties of them.

The UI of EdgeSimTools (currently):

![image](https://github.com/jfqiao/EdgeSimTools/tree/master/docs/img/UI.png)

This tool now contains three simulators for edge computing in different scenarios.


## Computing Power Allocation and Traffic Scheduling for Edge Service Provisioning

In the mobile edge computing or multi-access edge computing (MEC) environment, services are usually deployed on edge servers of the edge platform, and the edge server and cloud server will jointly provide resources for these services. The nearest edge server (access server) receives the service requests produced by edge devices, it will forwards the service to the appropriate server for execution (called executor server) according to the traffic scheduling strategy. For different services, an executor server will allocate appropriate resources for it according to the resource allocation strategy. With these resources, the service requests will be processed, and the final service results will be obtained. Currently, the cost of using such a service provisioning system is determined by used resources, namely, the on-demand billing mode is supported in this SimTool. 

## Burst Load Evacuation Based on Dispatching and Scheduling In Edge Network

In this problem, N edge servers are connected to each other according to a certain topology. Each edge server can receive the 
task request from the mobile terminal, forward the task request as a route, and process the task request. For mobile edge 
scenarios, when there are a large number of task requests in a certain area, the local edge server cannot effectively handle 
the problem of a large number of task requests. Design and use algorithms to find the optimal evacuation strategy. In order to 
achieve the purpose of minimizing the time to complete all tasks.

## Distributed Redundancy Scheduling for Microservice-based Applications at the Edge

Multi-access Edge Computing (MEC) is booming as a promising paradigm to push the computation and communication resources from 
cloud to the network edge to provide services and perform computations. With container technologies, MEC enables mobile devices
with limited battery capacity and small memory footprint to run composite microservice-based applications without 
time-consuming backbone transmission. Service deployment at the edge is of importance to put MEC from theory into practice. 
However, state-of-the-art researches do not take the composite property of services into consideration. Besides, although
Kubernetes has certain abilities to heal container failures, high availability of deployed microservices can not be ensured 
due to the heterogeneity and variability of edge sites.