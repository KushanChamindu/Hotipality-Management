# Service Registry

## Setup Eureka Cluster on own local computer
#### 1-  What is Eureka Server Cluster?
A Eureka Server Cluster, also known as a Eureka Peer Awareness system, is a group of Eureka Servers that communicate with each other. This is not your average group of servers, though. They have a knack for talking to each other, sharing information about the registered services. This means even if one of the servers goes down, the others can still provide the necessary service information, ensuring high availability and fault tolerance. This is a key feature, making Eureka Server Clusters an excellent choice for large-scale systems where downtime can be costly.



    eureka:
      instance:
        prefer-ip-address: true
      client:
        fetch-registry: true
        register-with-eureka: true
        service-url:
          defaultZone: ${EUREKA_SERVER_ADDRESS:http://localhost:8761/eureka}

#### 2-  Define Eureka Server Peers on MacOS
If you’re using MacOS, you can define each Eureka Discovery Server peer by editing your hosts file. This file is essentially a map telling your computer where to find the servers identified as ‘peer1’, ‘peer2’, and ‘peer3’. Here’s how you can do it:

First, open a terminal window. You can do this by going to Applications > Utilities > Terminal.

Next, you’ll need to edit the hosts file, which is found at /etc/hosts. We’ll use the built-in text editor “nano” for this purpose. Type the following command into your terminal and press enter:

sudo nano /etc/hosts
You’ll be asked for your password. Go ahead and enter it.

This will open the hosts file in the nano editor. At the end of the file, you’re going to define each Eureka Discovery Server peer with its own line. You’ll be associating each peer name with the local IP address (127.0.0.1), like so:

   127.0.0.1 peer1

   127.0.0.1 peer2

   127.0.0.1 peer3

Each line tells your computer to direct any requests for ‘peer1’, ‘peer2’, or ‘peer3’ to your local machine (127.0.0.1).

#### 2-  Define Eureka Server Peers on Windows
To define Eureka Server peers on Windows OS you’ll need to open the Notepad application as an administrator. This is necessary because the hosts file is a system file and needs administrative privileges to edit. To do this, click on the Start button, type ‘Notepad’ in the search box, right-click on the Notepad app in the search results, and select ‘Run as administrator’.

Once the Notepad application is open, you’ll need to open your hosts file. Go to File > Open, and in the File name box, type in C:\Windows\System32\Drivers\etc\hosts, then click ‘Open’. This will open up your hosts file in Notepad.

At the end of the file, you’ll define each Eureka Discovery Server peer, each on its own line. You’ll associate each peer name with the local IP address (127.0.0.1), like so:

127.0.0.1 peer1

127.0.0.1 peer2

127.0.0.1 peer3

Each line is instructing your computer to direct any requests for ‘peer1’, ‘peer2’, or ‘peer3’ to your local machine (127.0.0.1).


###  3-  Run Eureka Server Cluster with Peer Awareness

Open a terminal window. Navigate to the directory where your project is located using the cd command. For example, if your project is in a folder named “myproject” on your desktop, you would type: cd Desktop/myproject.
Run the first instance of the Eureka Server for “peer1” using the following command: 

$env:SPRING_PROFILES_ACTIVE = "peer1"; ./mvnw spring-boot:run


Open a second terminal window. 

$env:SPRING_PROFILES_ACTIVE = "peer2"; ./mvnw spring-boot:run


Run your application

### 4 - In Production Mode :
you want to set up a Docker image to accommodate both scenarios:

When starting your Docker container, you can specify the environment variables for peer1 and peer2 accordingly:

docker run -e EUREKA_SERVER_ADDRESS_PEER1="http://custom-peer1:8761/eureka" your-image-name

docker run -e EUREKA_SERVER_ADDRESS_PEER2="http://custom-peer2:8761/eureka" your-image-nam
e
