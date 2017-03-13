using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.IO.Ports;

namespace OBDSimulator
{
    public partial class Form1 : Form
    {
        MySerialPort mycom = new MySerialPort();
        public Form1()
        {
            InitializeComponent();
        }
        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            string str = mycom.Serialport_Config(this.comboBox1.SelectedItem.ToString(), 256000);
            if (str.Contains("true"))
            {
                mycom.StopCom = false;
                mycom.MySerialPortReceive();   
                if (SendATCMD("1").Contains("指令格式错误"))
                {
                    this.ovalShape1.FillColor = System.Drawing.Color.Blue;
                             
                }
                else
                {
                    this.ovalShape1.FillColor = System.Drawing.Color.Black;
                    mycom.com.Close();
                }
            }
            else
            {
                this.ovalShape1.FillColor = System.Drawing.Color.Black;
                mycom.com.Close();
            }
        }
        
        TextClass dss = new TextClass();
        private void Form1_Load(object sender, EventArgs e)
        {
            int i;
            this.MaximizeBox = false;                          
            this.FormBorderStyle = FormBorderStyle.FixedSingle;
            this.ovalShape1.FillColor = System.Drawing.Color.Black;
            this.ovalShape2.FillColor = System.Drawing.Color.Black;
            label6.Text = "无故障码";
            label8.Text = "无故障码";
            label10.Text = "无故障码";
            dataGridView1.Columns[0].Width = 500;
            dataGridView1.Columns[1].Width = 300;
            dataGridView1.Columns[0].HeaderText = "项目";
            dataGridView1.Columns[1].HeaderText = "值";
            dataGridView1.Rows.Add(200);
            dataGridView1rowIndex = 0;
            comboBox2.Enabled = false;
            button4.Enabled = true;
            button5.Enabled = true;
            trackBar1.Enabled = true;
            for (i = 0; i < 151; i++)
            {
                dataGridView1.Rows[i].Cells[0].Value = dss.DSString[i,0];
                dataGridView1.Rows[i].Cells[1].Value = dss.DSString[i,1];
            }
            label23.Text = trackBarValue.ToString();
            string[] ports = SerialPort.GetPortNames();
            i = 0;
            foreach (string port in ports)
            {
                this.comboBox1.Items[i++] = port;
            }
            mycom.SerialPortLinkPortFail += new SerialPortLinkFailEvent(LinkFail);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string str = null,str1=null;
            str = "AT+DTC";
            try
            {
                str += string.Format("{0:D2}", Convert.ToInt32(textBox5.Text));
            }
            catch (System.Exception)
            {
                str += "00";
            }
            str += ":" + textBox1.Text.ToUpper();
            str1=SendATCMD(str);

            if (str1.Contains("模拟当前故障码"))
            {
                string[] sArray = str1.Split(':');
                string[] sArray1 = sArray[1].Split('\r');
                if (sArray1[0] == "")
                {
                    label6.Text = "无故障码";

                }
                else
                {
                    label6.Text = sArray1[0];
                }
                    
            }
            else if (str1.Contains("您输入的故障码号不符合定义规则，请查证后输入！"))
            {
                MessageBox.Show("输入故障码格式错误，请输入需要模拟故障码的个数并且故障码之间需要用\",\"隔开，(比如P1012,P1013)");

            }
            else if (str1.Contains("err0"))
            {
                MessageBox.Show("端口未打开，或者USB连接已经断开！");

            }
            else if (str.Contains("err1"))
            {
                MessageBox.Show("模拟器未响应");
            }

        }

        private void comboBox3_SelectedIndexChanged(object sender, EventArgs e)
        {
            string str = null;
            if (String.Compare(this.comboBox3.SelectedItem.ToString(),"ISO14230-4(5baud init)")==0)
            {
                str = SendATCMD("AT+ISO14230-4ADDR");
                if (str.Contains("ISO14230-4 地址激活 协议模拟启动"))
                {
                    this.label18.Text = "ISO14230-4(5baud init)";
                    this.ovalShape2.FillColor = System.Drawing.Color.Blue;
                }
                else if(str.Contains("err0"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");
                }
                else if(str.Contains("err1"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("模拟器未响应");

                } 
     
            }
            else if (String.Compare(this.comboBox3.SelectedItem.ToString(), "ISO14230-4(fast init)") == 0)
            {
                str = SendATCMD("AT+ISO14230-4HL");
                if (str.Contains("ISO14230-4 电平激活 协议模拟启动"))
                {
                    this.label18.Text = "ISO14230-4(fast init)";
                    this.ovalShape2.FillColor = System.Drawing.Color.Blue;
                }
                else if (str.Contains("err0"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");
                }
                else if (str.Contains("err1"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("模拟器未响应");

                } 
            }
            else if (String.Compare(this.comboBox3.SelectedItem.ToString(), "ISO9141-2(5baud init)") == 0)
            {
                str = SendATCMD("AT+ISO9141-2ADDR");
                if (str.Contains("ISO9141-2  地址激活 协议模拟启动"))
                {
                    this.label18.Text = "ISO9141-2(5baud init)";
                    this.ovalShape2.FillColor = System.Drawing.Color.Blue;
                }
                else if (str.Contains("err0"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");
                }
                else if (str.Contains("err1"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("模拟器未响应");

                } 
            }
            else if (String.Compare(this.comboBox3.SelectedItem.ToString(), "ISO15765-4(11BIT ID 500K)") == 0)
            {
                str = SendATCMD("AT+ISO15765-4STD_500K");
                if (str.Contains("ISO15765-4 11bit 500K 协议模拟启动"))
                {
                    this.label18.Text = "ISO15765-4(11BIT ID 500K)";
                    this.ovalShape2.FillColor = System.Drawing.Color.Blue;
                }
                else if (str.Contains("err0"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");
                }
                else if (str.Contains("err1"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("模拟器未响应");

                } 
            }
            else if (String.Compare(this.comboBox3.SelectedItem.ToString(), "ISO15765-4(29BIT ID 500K)") == 0)
            {
                str = SendATCMD("AT+ISO15765-4EXT_500K");
                if (str.Contains("ISO15765-4 29bit 500K 协议模拟启动"))
                {
                    this.label18.Text = "ISO15765-4(29BIT ID 500K)";
                    this.ovalShape2.FillColor = System.Drawing.Color.Blue;
                }
                else if (str.Contains("err0"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");
                }
                else if (str.Contains("err1"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("模拟器未响应");

                } 
            }
            else if (String.Compare(this.comboBox3.SelectedItem.ToString(), "ISO15765-4(11BIT ID 250K)") == 0)
            {
                str = SendATCMD("AT+ISO15765-4STD_250K");
                if (str.Contains("ISO15765-4 11bit 250K 协议模拟启动"))
                {
                    this.label18.Text = "ISO15765-4(11BIT ID 250K)";
                    this.ovalShape2.FillColor = System.Drawing.Color.Blue;
                }
                else if (str.Contains("err0"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");
                }
                else if (str.Contains("err1"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("模拟器未响应");

                } 
            }
            else if (String.Compare(this.comboBox3.SelectedItem.ToString(), "ISO15765-4(29BIT ID 250K)") == 0)
            {
                str = SendATCMD("AT+ISO15765-4EXT_250K");
                if (str.Contains("ISO15765-4 29bit 250K 协议模拟启动"))
                {
                    this.label18.Text = "ISO15765-4(29BIT ID 250K)";
                    this.ovalShape2.FillColor = System.Drawing.Color.Blue;
                }
                else if (str.Contains("err0"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");
                }
                else if (str.Contains("err1"))
                {
                    this.ovalShape2.FillColor = System.Drawing.Color.Black;
                    MessageBox.Show("模拟器未响应");

                } 
            }
            else
            {
                this.ovalShape2.FillColor = System.Drawing.Color.Black;
            }
            
        }
        private string SendATCMD(string str)
        {
            string str1 = null;
            if (mycom.MySerialPortSend(str))
            {
                if (WaitReceiveStr())
                {
                    str1 = mycom.ReceiveStr;
                    mycom.ReceiveStr = null;
                    return str1;
                }
                else
                {
                    mycom.ReceiveStr = null;
                    return "err1";
                }
            }
            else
            {
                mycom.ReceiveStr = null;
                return "err0";
            } 
        }
        private bool WaitReceiveStr()
        {
            Thread.Sleep(500);
            if (mycom.ReceiveStr != null)
            {
                return true;
            }
            return false;
           
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string str = null, str1 = null;
            str = "AT+07DTC";
            try
            {
                str += string.Format("{0:D2}", Convert.ToInt32(textBox6.Text));
            }
            catch (System.Exception)
            {
                str += "00";
            }
            str += ":" + textBox2.Text.ToUpper();
            str1 = SendATCMD(str);

            if (str1.Contains("模拟未决故障码"))
            {
                string[] sArray = str1.Split(':');
                string[] sArray1 = sArray[1].Split('\r');
                if (sArray1[0] == "")
                {
                    label8.Text = "无故障码";

                }
                else
                {
                    label8.Text = sArray1[0];
                }

            }
            else if (str1.Contains("您输入的故障码号不符合定义规则，请查证后输入！"))
            {
                MessageBox.Show("输入故障码格式错误，请输入需要模拟故障码的个数并且故障码之间需要用\",\"隔开，(比如P1012,P1013)");

            }
            else if (str1.Contains("err0"))
            {
                MessageBox.Show("端口未打开，或者USB连接已经断开！");

            }
            else if (str.Contains("err1"))
            {
                MessageBox.Show("模拟器未响应");
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            string str = null, str1 = null;
            str = "AT+0ADTC";
            try
            {
                str += string.Format("{0:D2}", Convert.ToInt32(textBox7.Text));
            }
            catch (System.Exception)
            {
                str += "00";
            }
            str += ":" + textBox3.Text.ToUpper();
            str1 = SendATCMD(str);

            if (str1.Contains("模拟带永久状态的当前故障码"))
            {
                string[] sArray = str1.Split(':');
                string[] sArray1 = sArray[1].Split('\r');
                if (sArray1[0] == "")
                {
                    label10.Text = "无故障码";

                }
                else
                {
                    label10.Text = sArray1[0];
                }

            }
            else if (str1.Contains("您输入的故障码号不符合定义规则，请查证后输入！"))
            {
                MessageBox.Show("输入故障码格式错误，请输入需要模拟故障码的个数并且故障码之间需要用\",\"隔开，(比如P1012,P1013)");

            }
            else if (str1.Contains("err0"))
            {
                MessageBox.Show("端口未打开，或者USB连接已经断开！");

            }
            else if (str1.Contains("err1"))
            {
                MessageBox.Show("该协议没有永久状态故障码，不能模拟！");
            }
        }

        private void button6_Click(object sender, EventArgs e)
        {
            string str = null, str1 = null;
            str = "AT+VIN";
            str += ":" + textBox4.Text.ToUpper();
            if (str.Length >= 24)
            {
                str1 = SendATCMD(str);
                if (str1.Contains("模拟VIN码为"))
                {
                    string[] sArray = str1.Split(':');
                    string[] sArray1 = sArray[1].Split('\r');
                    label14.Text = sArray1[0];

                }
                else if (str1.Contains("输入VIN码格式错误,VIN码由17为数字和大写字母组成，请重新输入！"))
                {
                    MessageBox.Show("输入VIN码必须为17位字母和数字组成，请重新输入！");

                }
                else if (str1.Contains("err0"))
                {
                    MessageBox.Show("端口未打开，或者USB连接已经断开！");

                }
                else if (str.Contains("err1"))
                {
                    MessageBox.Show("模拟器未响应");
                }
            }
            else
            {
                MessageBox.Show("输入VIN码必须为17位，请重新输入！");
 
            }
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            string str = null, str1 = null;
            str = "AT+SDS";
            str += string.Format("{0:D3}", Convert.ToInt32(dataGridView1rowIndex)) + ":"+ this.comboBox2.SelectedItem.ToString();
            str1 = SendATCMD(str);
            if (str1.Contains("模拟("))
            {
                string[] sArray = str1.Split(':');
                string[] sArray1 = sArray[1].Split(',');
                dataGridView1.Rows[dataGridView1rowIndex].Cells[1].Value = sArray1[0];

            }
            else if (str1.Contains("请设置需要模拟的ISO协议类型"))
            {
                timer1.Enabled = false;
                MessageBox.Show("未激活指定协议，不能操作！");
            }
            else if (str1.Contains("err0"))
            {

                timer1.Enabled = false;
                MessageBox.Show("端口未打开，或者USB连接已经断开！");


            }
            else if (str.Contains("err1"))
            {
                timer1.Enabled = false;
                MessageBox.Show("模拟器未响应");
            }

            else
            {
                timer1.Enabled = false;
                MessageBox.Show("模拟器未响应");
            }           
        }
        private bool GetAutodsType(int index)
        {
            int[] strindex = new int[45] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 54, 55, 72, 73, 74, 136 };
            foreach (int i in strindex)
            {
                if (i == index)
                {
                    return true;
                }
            }
            return false;
        }
        private void button4_MouseDown(object sender, MouseEventArgs e)  
        {
            timer1.Enabled = true;
            btbool = true;
        }

        private void button4_MouseUp(object sender, MouseEventArgs e)
        {
            timer1.Enabled = false;

        }
        private int dataGridView1rowIndex = 0;
        private void dataGridView1_Click(object sender, EventArgs e)
        {
            int i;
            dataGridView1rowIndex = this.dataGridView1.CurrentCell.RowIndex;
            if (GetAutodsType(dataGridView1rowIndex))
            {
                comboBox2.Enabled = true;
                button4.Enabled = false;
                button5.Enabled = false;
                trackBar1.Enabled = false;
                dss.dsvalueTo(dataGridView1rowIndex);
                comboBox2.Items.Clear();
                for(i = 0;i<dss.dsvalue.total;i++)
                {
                    comboBox2.Items.Add(dss.dsvalue.dsv[i]);
                }
                
            }
            else
            {
                comboBox2.Enabled = false;
                button4.Enabled = true;
                button5.Enabled = true;
                trackBar1.Enabled = true;
            }
        }
        
        private void timer1_Tick(object sender, EventArgs e)
        {
            int i;
            string str = null, str1 = null;
            str = "AT+SDS";
            if (btbool == true)
            {
                str += string.Format("{0:D3}", Convert.ToInt32(dataGridView1rowIndex)) + ":UP";
            }
            else
            {
                str += string.Format("{0:D3}", Convert.ToInt32(dataGridView1rowIndex)) + ":DOWN";
            }
            
            for (i = 0; i < (trackBarValue - 1);i++)
            {
                mycom.MySerialPortSend(str);
                Thread.Sleep(5);
            }
            if (mycom.ReceiveStr != null)
            {
                do
                {
                    mycom.ReceiveStr = null;
                    Thread.Sleep(500);
                }
                while (mycom.ReceiveStr != null); 
            }
            str1 = SendATCMD(str);
            if (str1.Contains("模拟("))
            {
                string[] sArray = str1.Split(':');
                string[] sArray1 = sArray[1].Split(',');
                dataGridView1.Rows[dataGridView1rowIndex].Cells[1].Value = sArray1[0];

            }
            else if (str1.Contains("请设置需要模拟的ISO协议类型"))
            {
                timer1.Enabled = false;
                MessageBox.Show("未激活指定协议，不能操作！");
            }
            else if (str1.Contains("err0"))
            {

                timer1.Enabled = false;
                MessageBox.Show("端口未打开，或者USB连接已经断开！");
                

            }
            else if (str.Contains("err1"))
            {
                timer1.Enabled = false;
                MessageBox.Show("模拟器未响应");
            }

            else
            {
                timer1.Enabled = false;
                MessageBox.Show("模拟器未响应");
            }           
        }
        int trackBarValue = 1;
        private void trackBar1_Scroll(object sender, EventArgs e)
        {
            trackBarValue = trackBar1.Value + 1;
            label23.Text = trackBarValue.ToString();
        }
        private bool btbool = true;
        private void button5_MouseDown(object sender, MouseEventArgs e)
        {
            timer1.Enabled = true;
            btbool = false;
        }

        private void button5_MouseUp(object sender, MouseEventArgs e)
        {
            timer1.Enabled = false;
        }
        private void LinkFail(object sender, string str)
        {
            if (str.Contains("err0"))
            {
                MessageBox.Show("连接断开，请检查USB连接后再打开软件！");
                this.Close();
            }
        }

      }
    }

     

