from mpi4py import MPI


class DSM:
    def __init__(self):
        self.a = 0
        self.b = 0
        self.c = 0
        self.subscribers = {"a": [], "b": [], "c": []}

    def setVariable(self, variable, value):
        if variable == "a":
            self.a = value
        elif variable == "b":
            self.b = value
        elif variable == "c":
            self.c = value

    def updateVariable(self, variable, value):
        self.setVariable(variable, value)
        self.sendToSubscribers(variable, str(MPI.COMM_WORLD.Get_rank()) + " updated variable "+variable+" to "+str(value))

    def subscribe(self, variable):
        self.subscribers[variable].append(MPI.COMM_WORLD.Get_rank())
        DSM.sendAll(str(MPI.COMM_WORLD.Get_rank()) + " subscribed to " + variable)

    def addSubscriber(self, variable, rank):
        self.subscribers[variable].append(rank)

    def sendToSubscribers(self, variable, msg):
        for i in range(0, MPI.COMM_WORLD.Get_size()):
            if MPI.COMM_WORLD.Get_rank() != i and self.isSubscribed(variable, i):
                MPI.COMM_WORLD.send(msg, dest=i, tag=0)

    def isSubscribed(self, variable, rank):
        if rank in self.subscribers[variable]:
            return True
        return False

    def compareAndExchange(self, variable, value, newValue):
        if variable == "a":
            if self.a == value:
                self.updateVariable(variable, newValue)
        elif variable == "b":
            if self.b == value:
                self.updateVariable(variable, newValue)
        elif variable == "c":
            if self.c == value:
                self.updateVariable(variable, newValue)

    @staticmethod
    def close():
        DSM.sendAll(str(MPI.COMM_WORLD.Get_rank()) + " closing")

    @staticmethod
    def sendAll(msg):
        for i in range(0, MPI.COMM_WORLD.Get_size()):
            if MPI.COMM_WORLD.Get_rank() != i:
                MPI.COMM_WORLD.send(msg, dest=i, tag=0)

