using System;
using System.Text;
using System.Net.Sockets;

namespace PDPlab5
{
    public class StateObject
    {
        public Socket workSocket = null;
        public const int BufferSize = 256;
        public byte[] buffer = new byte[BufferSize];
        public StringBuilder sb = new StringBuilder();
    }
    class Program
    {
        static void Main(string[] args)
        {
            //DirectCallbacksClient.StartClient();
            //TaskClient.StartClient();
            AsynchronousTaskClient.StartClient();
            Console.Read();
        }
    }
}
