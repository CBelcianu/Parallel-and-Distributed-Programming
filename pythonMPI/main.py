from mpi4py import MPI
from dsm import DSM
from listener import listenerThread, printInformation
import threading

comm = MPI.COMM_WORLD
rank = comm.Get_rank()

dsm = DSM()

if rank == 0:
    thr = threading.Thread(target=listenerThread, args=(dsm,))
    thr.start()
    dsm.subscribe("a")
    dsm.subscribe("b")
    dsm.subscribe("c")

    while True:
        print("1. Update variable")
        print("2. Compare and exchange variable")
        print("0. exit")

        asw = int(input(">> "))

        if asw == 0:
            DSM.close()
            thr.join()
            break
        elif asw == 1:
            print("Which variable would you like to set?")
            vr = input(">> variable=")
            print("Give a value")
            vl = int(input(">> " + vr + "="))
            dsm.updateVariable(vr, vl)
            printInformation(dsm)
        elif asw == 2:
            print("Which variable would you like to update?")
            vr = input(">> variable=")
            print("Give the current value")
            vl = int(input(">> " + vr + "="))
            print("Give the new value")
            nvl = int(input(">> " + vr + "="))
            dsm.compareAndExchange(vr, vl, nvl)
            printInformation(dsm)
elif rank == 1:
    thr = threading.Thread(target=listenerThread, args=(dsm,))
    thr.start()
    dsm.subscribe("a")
    dsm.subscribe("c")
    thr.join()
elif rank == 2:
    thr = threading.Thread(target=listenerThread, args=(dsm,))
    thr.start()
    dsm.subscribe("b")
    dsm.subscribe("c")
    thr.join()
