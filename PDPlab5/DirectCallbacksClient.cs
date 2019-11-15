using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Net;
using System.Net.Sockets;

namespace PDPlab5
{
    class DirectCallbacksClient
    {
        private const int port = 80;
        public static void StartClient()
        {
            IPHostEntry ipHostInfo = Dns.GetHostEntry("www.cs.ubbcluj.ro");
            IPAddress ipAddress = ipHostInfo.AddressList[0];
            IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);

            Socket client = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            client.BeginConnect(remoteEP, Connected, client);
        }

        public static void Connected(IAsyncResult ar)
        {
            var clientSocket = (Socket)ar.AsyncState;
            var hostname = "www.cs.ubbcluj.ro";

            clientSocket.EndConnect(ar);
            Console.WriteLine("Socket connected to {0} ({1})", hostname, clientSocket.RemoteEndPoint);

            var byteData = Encoding.ASCII.GetBytes("GET /~rlupsa/edu/pdp/lab-5-futures-continuations.html HTTP/1.1\r\n" +
                    "Host: " + hostname + "\r\n" +
                     "Content-Length: 0\r\n" +
                     "\r\n");

            clientSocket.BeginSend(byteData, 0, byteData.Length, 0, Sent, clientSocket);
        }

        private static void Sent(IAsyncResult ar)
        {
            var clientSocket = (Socket)ar.AsyncState;
            StateObject state = new StateObject();
            state.workSocket = clientSocket;

            var bytesSent = clientSocket.EndSend(ar);
            Console.WriteLine("--> Sent {0} bytes to server.", bytesSent);

            clientSocket.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, Receiving, state);
        }

        private static void Receiving(IAsyncResult ar)
        {
            var state = (StateObject)ar.AsyncState;
            var clientSocket = state.workSocket;

            StringBuilder response = new StringBuilder();

            try
            {
                var bytesRead = clientSocket.EndReceive(ar);

                response.Append(Encoding.ASCII.GetString(state.buffer, 0, bytesRead));

                if (response.ToString().Contains("\r\n\r\n"))
                {
                    clientSocket.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, Receiving, state);
                }
                else
                {
                    var responseBody = response.ToString();

                    var contentLengthHeaderValue = parseResponse(response.ToString());
                    if (responseBody.Length < contentLengthHeaderValue)
                    {
                        clientSocket.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, Receiving, state);
                    }
                    else
                    {
                        foreach (var i in response.ToString().Split('\r', '\n'))
                            Console.WriteLine(i);
                        Console.WriteLine(
                            "Response received : expected {0} chars in body, got {1} chars (headers + body)",
                            contentLengthHeaderValue, response.Length);

                        clientSocket.Shutdown(SocketShutdown.Both);
                        clientSocket.Close();
                    }
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
