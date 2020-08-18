
<table align="center"><tr><td align="center" width="9999">
<img src="./rabbitmq.svg" alt="rabbitmq logo" width="200" height="200" style="">

# rabbitmq-tutorials
Series of simple examples using [rabbitmq] and java inspired by [rabbitmq tutorials website] (not a total rip-off, but almost)
</td></tr></table>

### "Echo"
One of the simplest use cases for messaging is when a Producer sends a message to a queue that threfore are 
consumed by a Consumer. 

````
Producer ---> [][][][][] ---> Consumer
                queue
````

In this example, we start a command line application that allows the user
to type whatever message it wants and send it to a queue running the EchoProducer class. The EchoConsumer
consumes the message and prints its content.
This implementation uses [the 'Hello World' example](https://www.rabbitmq.com/tutorials/tutorial-one-java.html) as 
basis. 

**To execute this example, execute the EchoMessageTutorial class**. 


[rabbitmq]:https://www.rabbitmq.com/
[rabbitmq tutorials website]:https://www.rabbitmq.com/getstarted.html
