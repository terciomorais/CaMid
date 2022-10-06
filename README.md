# M-CaMid
Multi-Cloud-aware Middleware
M-CaMid (Multi-Cloud aware Middleware) is an object-oriented middleware specially designed for supporting the development and management of distributed applications in multi-cloud environments. The proposed middleware implements cloud elasticity at the middleware level and abstracts the underlying distribution complexities inherent to multi-cloud infrastructures. In practice, M-CaMid implements essential mechanisms to support: (i) communication between the application’s components distributed over multiple clouds; and (ii) management of distributed applications and their infrastructure resources with minimal human intervention by leveraging the cloud elasticity benefit automatically and transparently.

## Reproducible Experiment
This section discusses experiments to evaluate M-CaMid. The main goal is to assess the M-CaMid efficiency on the management of multi-cloud distributed applications by exploring elasticity at different levels of granularity and scopes. Considering the multi-cloud elasticity as the key element for cross-management, experiments plan to characterise its differences from common approaches. Common approaches selection depends on the type of experiment. For example, the exclusive use of infrastructure elasticity, referred here to coarse elasticity, is a reference approach to assess the M-CaMid cross management. Experiments’ sections describe methods in detail. A systematic approach for performance evaluation was adopted (Jain, 1991) to accomplish the experiments. First, some steps are defined to conduct the experiments: goals and system boundaries, scenarios, services and outcomes, metrics, parameters and factors, experiment design. Finally, results are presented.

### Goals and System Boundaries
The experiments’ main goal is to estimate the impact of using M-CaMid over the performance of multi-cloud distributed applications. The experiment’s system consists of a multi-cloud environment. Its components are a multi-cloud distributed application, a multi-cloud infrastructure composed of a set of private clouds and M-CaMid. The multi-cloud application follows a client-server architecture, where the client-side can invoke a service implemented by remote objects on the server-side. The service executes the calculation of the Fibonacci sequence. Remote objects can be hosted in a single node, in many nodes in a single cloud, or many clouds.
Since M-CaMid is a client-centric solution, some environment components do not belong to the system under evaluation: tools for IaaS management, low-level communication channel and the Internet. Public clouds are not used in the experiments because their IaaS infrastructure resources are out of the system boundaries.

### Scenarios’ Description
M-CaMid is submitted to scenarios where a multi-cloud application running in a limited-growth infrastructure is submitted to unpredictable peaks of workloads. Facing this challenge, M-CaMid supports the high availability and scalability of the multi-cloud applications. The distributed application is client/server, in which the client invokes remote objects’ methods (server-side). M-CaMid can host the client in the same cloud provider or a different one.
