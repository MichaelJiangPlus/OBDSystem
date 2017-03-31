using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO.Ports;
using System.Windows.Forms;
namespace OBDSimulator
{
    public delegate void SerialPortLinkFailEvent(object sender, string str);
    class MySerialPort
    {
        public bool StopCom = false;
        public SerialPort com = new SerialPort();
        public event SerialPortLinkFailEvent SerialPortLinkPortFail;
        public string Serialport_Config(string COMx, int Baud)
        {

            try
            {
                com.BaudRate = Baud;
                com.PortName = COMx;
                com.DataBits = 8;
                com.Open();
            }
            catch (System.InvalidOperationException)//指定的端口已经打开
            {
                return "err0";
            }
            catch (System.ArgumentOutOfRangeException)
            {
                return "err1";
            }
            catch (System.ArgumentException)//端口名称不是以“COM”开始的。- 或 -端口的文件类型不受支持。
            {
                return "err2";
            }
            catch (System.IO.IOException)//此端口处于无效状态。- 或 -尝试设置基础端口状态失败。例如，从此 System.IO.Ports.SerialPort 对象传递的参数无效。
            {
                return "err3";
            }
            catch (System.UnauthorizedAccessException)//对端口的访问被拒绝。
            {
                return "err4";
            }
            return "true";
        }
        private byte[] readbyte = new byte[800];
        public  string ReceiveStr = null;
        public void MySerialPortReceive()
        {
            com.BaseStream.BeginRead(readbyte, 0, readbyte.Length, new AsyncCallback(ReadSerialPortData), com);
 
        }
        private void ReadSerialPortData(IAsyncResult ar)
        {
            try
            {
                SerialPort com1 = (SerialPort)ar.AsyncState;
                int len = com1.BaseStream.EndRead(ar);
                ReceiveStr += Encoding.Default.GetString(readbyte).Remove(len);
            }
            catch (System.Exception ex)
            {
                if (ex.ToString().Contains("由于线程退出"))
                {
                    SerialPortLinkPortFail(this, "err0");

                }
            }
            finally
            {
                try
                {
                    if (StopCom != true)
                    {
                        com.BaseStream.BeginRead(readbyte, 0, readbyte.Length, new AsyncCallback(ReadSerialPortData), com);
                    }
                    else
                    {
                        com.Close();
                        System.Threading.Thread.CurrentThread.Abort();
                    }
                    

                }
                catch (System.Exception)
                {
 
                }
                
            }
 
        }
        public bool MySerialPortSend(string str)
        {
            try
            {
                byte[] byteArray = System.Text.Encoding.Default.GetBytes(str);
                com.BaseStream.BeginWrite(byteArray, 0, byteArray.Length, new AsyncCallback(WriteSerialPortData), com);
            }
            catch (System.Exception)
            {
                return false;
            }
            return true;
        }
        private void WriteSerialPortData(IAsyncResult ar)
        {
            try
            {
                SerialPort com1 = (SerialPort)ar.AsyncState;
                com1.BaseStream.EndWrite(ar);
            }
            catch (System.Exception)
            {
            }
        }
    }
}
