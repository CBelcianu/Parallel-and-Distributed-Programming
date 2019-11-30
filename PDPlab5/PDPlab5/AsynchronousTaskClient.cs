using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;

namespace PDPlab5
{
    class AsynchronousTaskClient
    {
        private const int port = 80;

        private static ManualResetEvent connectDone = new ManualResetEvent(false);
        private static ManualResetEvent sendDone = new ManualResetEvent(false);
        private static ManualResetEvent receiveDone = new ManualResetEvent(false);

        private static String response = String.Empty;

        public static async void StartClient()
        {
            try
            {
                IPHostEntry ipHostInfo = Dns.GetHostEntry("www.cs.ubbcluj.ro");
                IPAddress ipAddress = ipHostInfo.AddressList[0];
                IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);

                Socket client = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

                // Connect to the remote endpoint.  
                await Connect(client, remoteEP);

                // Send test data to the remote device.  
                await Send(client, "GET /~rlupsa/edu/pdp/lab-5-futures-continuations.html HTTP/1.1\r\n" +
                    "Host: " + ipAddress + "\r\n" +
                    "Content-Length: 0\r\n" +
                    "\r\n");

                // Receive the response from the remote device.  
                await Receive(client);
                
                // Write the response to the console.  
                Console.WriteLine("Response received : {0}", response);
                Console.WriteLine(parseResponse(response));

                // Release the socket.  
                client.Shutdown(SocketShutdown.Both);
                client.Close();

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

        public static async Task Connect(Socket client, IPEndPoint remoteEP)
        {
            client.BeginConnect(remoteEP, new AsyncCallback(ConnectCallback), client);
            await Task.FromResult(connectDone.WaitOne());
        }

        private static void ConnectCallback(IAsyncResult ar)
        {
            try
            {
                Socket client = (Socket)ar.AsyncState;

                client.EndConnect(ar);

                Console.WriteLine("Socket connected to {0}",
                    client.RemoteEndPoint.ToString());

                // Signal that the connection has been made.  
                connectDone.Set();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private static async Task Receive(Socket client)
        {
            try
            {
                StateObject state = new StateObject();
                state.workSocket = client;
                client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, new AsyncCallback(ReceiveCallback), state);
                await Task.FromResult(receiveDone.WaitOne());
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private static void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                StateObject state = (StateObject)ar.AsyncState;
                Socket client = state.workSocket;

                // Read data from the remote device.  
                int bytesRead = client.EndReceive(ar);

                if (bytesRead > 0)
                {
                    // There might be more data, so store the data received so far.  
                    state.sb.Append(Encoding.ASCII.GetString(state.buffer, 0, bytesRead));
                    client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, new AsyncCallback(ReceiveCallback), state);
                }
                else
                {
                    // All the data has arrived; put it in response.  
                    if (state.sb.Length > 1)
                    {
                        response = state.sb.ToString();
                    }
                    // Signal that all bytes have been received.  
                    receiveDone.Set();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private static async Task Send(Socket client, String data)
        {
            byte[] byteData = Encoding.ASCII.GetBytes(data);
            client.BeginSend(byteData, 0, byteData.Length, 0, new AsyncCallback(SendCallback), client);
            await Task.FromResult(sendDone.WaitOne());
        }

        private static void SendCallback(IAsyncResult ar)
        {
            try
            {
                Socket client = (Socket)ar.AsyncState;

                // Complete sending the data to the remote device.  
                int bytesSent = client.EndSend(ar);
                Console.WriteLine("Sent {0} bytes to server.", bytesSent);

                // Signal that all bytes have been sent.  
                sendDone.Set();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }
    }
}
