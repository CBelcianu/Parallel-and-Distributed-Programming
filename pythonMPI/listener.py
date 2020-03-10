from mpi4py import MPI


def listenerThread(dsm):
    while True:
        print("Rank " + str(MPI.COMM_WORLD.Get_rank()) + ": waiting\n")
        msg = MPI.COMM_WORLD.recv(source=MPI.ANY_SOURCE, tag=MPI.ANY_TAG)

        if "closing" in msg:
            print("Rank " + str(MPI.COMM_WORLD.Get_rank()) + ": " + msg)
            return
        elif "updated" in msg:
            print("Rank " + str(MPI.COMM_WORLD.Get_rank()) + ": " + msg)
            lst = msg.split()
            dsm.setVariable(lst[3], int(lst[5]))
        elif "subscribed " in msg:
            print("Rank " + str(MPI.COMM_WORLD.Get_rank()) + ": " + msg)
            lst = msg.split()
            dsm.addSubscriber(lst[3], int(lst[0]))
        printInformation(dsm)


def printInformation(dsm):
    print("Rank " + str(MPI.COMM_WORLD.Get_rank()) + ": a=" + str(dsm.a) + ", b=" + str(
        dsm.b) + ", c=" + str(dsm.c) + ", subscribers" + str(dsm.subscribers))
