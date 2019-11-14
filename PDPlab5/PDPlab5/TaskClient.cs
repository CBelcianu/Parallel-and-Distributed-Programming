using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;

namespace PDPlab5
{
    class TaskClient
    {
        private const int port = 80;

        private static ManualResetEvent connectDone = new ManualResetEvent(false);
        private static ManualResetEvent sendDone = new ManualResetEvent(false);
        private static ManualResetEvent receiveDone = new ManualResetEvent(false);

        private static String response = String.Empty;

        public static void StartClient()
        {
            IPHostEntry ipHostInfo = Dns.GetHostEntry("www.cs.ubbcluj.ro");
            IPAddress ipAddress = ipHostInfo.AddressList[0];
            IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);

            Socket client = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            Connect(client, remoteEP).Wait();
            Send(client, "GET /~rlupsa/edu/pdp/lab-5-futures-continuations.html HTTP/1.1\r\n" +
                    "Host: " + ipAddress + "\r\n" +
                     "Content-Length: 0\r\n" +
                     "\r\n").Wait();
            Receive(client).Wait();

            Console.WriteLine("Response received : {0}", response);
            Console.WriteLine(parseResponse(response));

            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }

        public static Task Connect(Socket client, IPEndPoint remoteEP)
        {
            client.BeginConnect(remoteEP, ConnectCallback, client);
            return Task.FromResult(connectDone.WaitOne());
        }

        private static void ConnectCallback(IAsyncResult ar)
        {
            var clientSocket = (Socket)ar.AsyncState;
            var hostname = "www.cs.ubbcluj.ro";

            clientSocket.EndConnect(ar);

            Console.WriteLine("Socket connected to {0} ({1})", hostname, clientSocket.RemoteEndPoint);

            connectDone.Set();
        }

        private static Task Send(Socket client, string data)
        {
            var byteData = Encoding.ASCII.GetBytes(data);

            client.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, client);

            return Task.FromResult(sendDone.WaitOne());
        }

        private static void SendCallback(IAsyncResult ar)
        {
            var clientSocket = (Socket)ar.AsyncState;

            var bytesSent = clientSocket.EndSend(ar);
            Console.WriteLine("Sent {0} bytes to server.", bytesSent);

            sendDone.Set();
        }

        private static Task Receive(Socket client)
        {
            StateObject state = new StateObject();
            state.workSocket = client;

            client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, ReceiveCallback, state);
            return Task.FromResult(receiveDone.WaitOne());

        }

        private static void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                StateObject state = (StateObject)ar.AsyncState;
                Socket client = state.workSocket;

                int bytesRead = client.EndReceive(ar);

                if (bytesRead > 0)
                {
                    state.sb.Append(Encoding.ASCII.GetString(state.buffer, 0, bytesRead));

                    client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, ReceiveCallback, state);
                }
                else
                {
                    if (state.sb.Length > 1)
                    {
                        response = state.sb.ToString();
                    }
                    receiveDone.Set();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
        private static int parseResponse(String response)
        {
            int contentLength = 0;
            if (response.Contains("\r\n\r\n"))
            {
                Regex reg = new Regex("\\\r\nContent-Length: (.*?)\\\r\n");
                Match m = reg.Match(response);
                contentLength = int.Parse(m.Groups[1].ToString());
            }
            return contentLength;
        }
    }
}
