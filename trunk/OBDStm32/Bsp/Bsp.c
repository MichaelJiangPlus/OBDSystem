
/************************************************************************
*@文件: Bsp.h
*@作者:
*@版本:
*@日期:
*@描述:
*************************************************************************/
#include "includes.h"


#if 1
#pragma import(__use_no_semihosting)             
                 
struct __FILE 
{ 
	int handle; 
	/* Whatever you require here. If the only file you are using is */ 
	/* standard output using printf() for debugging, no file handling */ 
	/* is required. */ 
}; 
/* FILE is typedef’ d in stdio.h. */ 
FILE __stdout;         
_sys_exit(int x) 
{ 
	x = x; 
} 

int fputc(int ch, FILE *f)
{   

  while((PC_USART->SR&0x40) == 0);
  PC_USART->DR = (u8)ch;      
  return ch;
}
#endif
/************************************************************************
  * @描述:  硬件初始化
  * @参数:  None
  * @返回值: None
  **********************************************************************/
void Bsp_Init(void)
{ 
  PC_USART_Config(256000);
  delay_init(72);
  NVIC_Config();
  LED_Config(&LEDControlA);
}
/************************************************************************
  * @描述:  串口配置
  * @参数:  Baud:串口波特率
  * @返回值: None
  **********************************************************************/
void PC_USART_Config(u32 Baud)
{
  u8 data;
  GPIO_InitTypeDef GPIO_InitStructure;
  USART_InitTypeDef USART_InitStructure;
  if (PC_USARTAPB == APB1)
  {
    RCC_APB2PeriphClockCmd(RCC_APBxPeriph_PC_USART_IO | RCC_APB2Periph_AFIO,ENABLE);
    RCC_APB1PeriphClockCmd(RCC_APBxPeriph_PC_USART,ENABLE);
  }
  else
  {
	RCC_APB2PeriphClockCmd(RCC_APBxPeriph_PC_USART_IO | RCC_APBxPeriph_PC_USART | RCC_APB2Periph_AFIO,ENABLE);
  }
  if (PC_PinRemap == ENABLE)
  {  				 
    GPIO_PinRemapConfig(GPIO_Remap_USART2,ENABLE);
  }
  GPIO_InitStructure.GPIO_Pin = PC_USART_TXD;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF_PP;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(PC_USART_IO,&GPIO_InitStructure);
    
  GPIO_InitStructure.GPIO_Pin = PC_USART_RXD;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(PC_USART_IO,&GPIO_InitStructure);

  USART_InitStructure.USART_BaudRate = Baud;
  USART_InitStructure.USART_WordLength = USART_WordLength_8b;
  USART_InitStructure.USART_StopBits = USART_StopBits_1;
  USART_InitStructure.USART_Parity = USART_Parity_No;
  USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None ;
  USART_InitStructure.USART_Mode = USART_Mode_Rx|USART_Mode_Tx;
  USART_Init(PC_USART,&USART_InitStructure);
  data = data;
  data = PC_USART->DR;
  data = PC_USART->SR;
  USART_ITConfig(PC_USART,USART_IT_RXNE,ENABLE);
  USART_Cmd(PC_USART,ENABLE);
}
/************************************************************************
  * @描述:  中断配置
  * @参数:  None
  * @返回值: None
  **********************************************************************/
void NVIC_Config(void)
{
  NVIC_InitTypeDef  NVIC_InitStructure;

  NVIC_PriorityGroupConfig(NVIC_PriorityGroup_1);
   
  NVIC_InitStructure.NVIC_IRQChannel = USART2_IRQn;
  NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;
  NVIC_InitStructure.NVIC_IRQChannelSubPriority = 0;
  NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;
  NVIC_Init(&NVIC_InitStructure);

  NVIC_InitStructure.NVIC_IRQChannel = TIM2_IRQn;
  NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 2;
  NVIC_InitStructure.NVIC_IRQChannelSubPriority = 0;
  NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;
  NVIC_Init(&NVIC_InitStructure);

  NVIC_InitStructure.NVIC_IRQChannel = USB_LP_CAN1_RX0_IRQn;
  NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 1;
  NVIC_InitStructure.NVIC_IRQChannelSubPriority = 0;
  NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;
  NVIC_Init(&NVIC_InitStructure);

  NVIC_InitStructure.NVIC_IRQChannel = USART1_IRQn;
  NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 1;
  NVIC_InitStructure.NVIC_IRQChannelSubPriority = 0;
  NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE;
  NVIC_Init(&NVIC_InitStructure);
}
/************************************************************************
  * @描述:  定时器2配置
  * @参数:   NONE
  * @返回值: NONE
  **********************************************************************/
void TIM2_Config(u32 T2Delayms)
{
	TIM_TimeBaseInitTypeDef  TIM_TimeBaseStructure;

	RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM2, ENABLE);

	TIM_TimeBaseStructure.TIM_Period = 35999;
	TIM_TimeBaseStructure.TIM_Prescaler = T2Delayms;
	TIM_TimeBaseStructure.TIM_ClockDivision = 0;
	TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up;
	TIM_TimeBaseInit(TIM2, &TIM_TimeBaseStructure);
	TIM_ClearFlag(TIM2, TIM_FLAG_Update);
	TIM_ARRPreloadConfig(TIM2, DISABLE);
	TIM_ITConfig(TIM2, TIM_IT_Update, ENABLE);
	TIM_Cmd(TIM2, ENABLE);
}
/************************************************************************
  * @描述:  LED配置
  * @参数:  LEDControl
  * @返回值: None
  **********************************************************************/
__IO u8 LEDCycleNum;
__IO LEDControldef* LEDControlSP;
void LED_Config(const LEDControldef* LEDControl)
{
  GPIO_InitTypeDef GPIO_InitStructure;

  RCC_APB2PeriphClockCmd(RCC_APBxPeriph_LED_IO_B | RCC_APBxPeriph_LED_IO_A,ENABLE);

  GPIO_InitStructure.GPIO_Pin = LED_D0 | LED_D1 | LED_D2 | LED_D3;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(LED_IO_A,&GPIO_InitStructure);
  GPIO_WriteBit(LED_IO_A, LED_D0|LED_D1|LED_D2|LED_D3, Bit_SET);

  GPIO_InitStructure.GPIO_Pin = LED_D4 | LED_D5 | LED_D6;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(LED_IO_B,&GPIO_InitStructure);
  GPIO_WriteBit(LED_IO_B, LED_D4 | LED_D5 | LED_D6, Bit_SET);

  LEDCycleNum = LEDControl->CycleNum;
  LEDControlSP = (LEDControldef*)LEDControl;
  TIM2_Config(LEDControl->T2Delayms);
}
/************************************************************************
  * @描述:  LED开关
  * @参数:  LEDControl 
  * @返回值: None
  **********************************************************************/
void LED_ONOFF(__IO SwitchType* sw)
{
  if (sw[0] == ON)
  {
    GPIO_WriteBit(LED_IO_A, LED_D0, Bit_RESET);
  }
  else
  {
    GPIO_WriteBit(LED_IO_A, LED_D0, Bit_SET);
  }
  if (sw[1] == ON)
  {
    GPIO_WriteBit(LED_IO_A, LED_D1, Bit_RESET);
  }
  else
  {
    GPIO_WriteBit(LED_IO_A, LED_D1, Bit_SET);
  }
  if (sw[2] == ON)
  {
    GPIO_WriteBit(LED_IO_A, LED_D2, Bit_RESET);
  }
  else
  {
    GPIO_WriteBit(LED_IO_A, LED_D2, Bit_SET);
  }
  if (sw[3] == ON)
  {
    GPIO_WriteBit(LED_IO_A, LED_D3, Bit_RESET);
  }
  else
  {
    GPIO_WriteBit(LED_IO_A, LED_D3, Bit_SET);
  }
  if (sw[4] == ON)
  {
    GPIO_WriteBit(LED_IO_B, LED_D4, Bit_RESET);
  }
  else
  {
    GPIO_WriteBit(LED_IO_B, LED_D4, Bit_SET);
  }
  if (sw[5] == ON)
  {
    GPIO_WriteBit(LED_IO_B, LED_D5, Bit_RESET);
  }
  else
  {
    GPIO_WriteBit(LED_IO_B, LED_D5, Bit_SET);
  }
  if (sw[6] == ON)
  {
    GPIO_WriteBit(LED_IO_B, LED_D6, Bit_RESET);
  }
  else
  {
    GPIO_WriteBit(LED_IO_B, LED_D6, Bit_SET);
  }
}
/************************************************************************
  * @描述:   CAN所用IO引脚配置
  * @参数:   None
  * @返回值: None
  **********************************************************************/
void CAN_GPIOConfig(void)
{
  GPIO_InitTypeDef GPIO_InitStructure;

  RCC_APB2PeriphClockCmd(RCC_APBxPeriph_CAN_IO | RCC_APB2Periph_AFIO,ENABLE);
  RCC_APB1PeriphClockCmd(RCC_APB1Periph_CAN1,ENABLE);
  GPIO_InitStructure.GPIO_Pin = CAN_RXD;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(CAN_IO, &GPIO_InitStructure);

  GPIO_InitStructure.GPIO_Pin = CAN_TXD;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF_PP;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(CAN_IO, &GPIO_InitStructure);
  if (CAN_PinRemap == ENABLE)
  { 
    GPIO_PinRemapConfig(GPIO_Remap1_CAN1,ENABLE);
  }
}

/************************************************************************
  * @描述:  CAN速率配置
  * @参数:  velocity 速率分频值
  * @返回值: None
  **********************************************************************/
__IO u8 TransmitMailbox;
void CAN_Config(u8 velocity)
{
  CAN_InitTypeDef  CAN_InitStructure;

  CAN_GPIOConfig();
  CAN_DeInit(CAN1);
  CAN_StructInit(&CAN_InitStructure);
    
  CAN_InitStructure.CAN_TTCM = DISABLE;
  CAN_InitStructure.CAN_ABOM = DISABLE;
  CAN_InitStructure.CAN_AWUM = DISABLE;
  CAN_InitStructure.CAN_NART = DISABLE;
  CAN_InitStructure.CAN_RFLM = DISABLE;
  CAN_InitStructure.CAN_TXFP = DISABLE;
  CAN_InitStructure.CAN_Mode = CAN_Mode_Normal;
  CAN_InitStructure.CAN_SJW = CAN_SJW_1tq;
  CAN_InitStructure.CAN_BS1 = CAN_BS1_11tq;
  CAN_InitStructure.CAN_BS2 = CAN_BS2_6tq;
  CAN_InitStructure.CAN_Prescaler = velocity;
  CAN_Init(CAN1, &CAN_InitStructure);
}
/************************************************************************
  * @描述:  CAN扩展帧滤波器设置
  * @参数:  id1,id2 效验码   mid1,mid2 屏蔽码
  * @返回值: None
  **********************************************************************/
void CAN1_Config32BitFilter(u32 id1, u32 id2, u32 mid1, u32 mid2)
{
  CAN_FilterInitTypeDef  CAN_FilterInitStructure;
  CAN_FilterInitStructure.CAN_FilterNumber = 0;
  CAN_FilterInitStructure.CAN_FilterMode = CAN_FilterMode_IdMask;
  CAN_FilterInitStructure.CAN_FilterScale = CAN_FilterScale_32bit;  
  CAN_FilterInitStructure.CAN_FilterIdHigh = id1>>13;
  CAN_FilterInitStructure.CAN_FilterIdLow = (id1<<3)|4;
  CAN_FilterInitStructure.CAN_FilterMaskIdHigh = mid1>>13;
  CAN_FilterInitStructure.CAN_FilterMaskIdLow = (mid1<<3)|4;
  CAN_FilterInitStructure.CAN_FilterFIFOAssignment = 0;
  CAN_FilterInitStructure.CAN_FilterActivation = ENABLE;
  CAN_FilterInit(&CAN_FilterInitStructure);
  CAN_FilterInitStructure.CAN_FilterNumber = 1;
  CAN_FilterInitStructure.CAN_FilterIdHigh = id2>>13;
  CAN_FilterInitStructure.CAN_FilterIdLow = (id2<<3)|4;
  CAN_FilterInitStructure.CAN_FilterMaskIdHigh = mid2>>13;
  CAN_FilterInitStructure.CAN_FilterMaskIdLow = (mid2<<3)|4;
  CAN_FilterInit(&CAN_FilterInitStructure);
  NVIC_Config();
  CAN_ITConfig(CAN1, CAN_IT_FMP0, ENABLE);
}
/************************************************************************
  * @描述:  CAN标准帧滤波器设置
  * @参数:  id1,id2 效验码   mid1,mid2 屏蔽码
  * @返回值: None
  **********************************************************************/
void CAN1_Config16BitFilter(u16 id1, u16 id2, u16 mid1, u16 mid2)                                                                             
{                                                                                                                         
    CAN_FilterInitTypeDef  CAN_FilterInitStructure;                                                                       
                                                                                                                          
    CAN_FilterInitStructure.CAN_FilterNumber=1;                                                                           
    CAN_FilterInitStructure.CAN_FilterMode=CAN_FilterMode_IdMask;                                                         
    CAN_FilterInitStructure.CAN_FilterScale=CAN_FilterScale_16bit;                                                        
    CAN_FilterInitStructure.CAN_FilterIdHigh=id1<<5;                                                                      
    CAN_FilterInitStructure.CAN_FilterIdLow=id2<<5;                                                                       
    CAN_FilterInitStructure.CAN_FilterMaskIdHigh=mid1<<5;                                                                  
    CAN_FilterInitStructure.CAN_FilterMaskIdLow=mid2<<5;                                                                   
    CAN_FilterInitStructure.CAN_FilterFIFOAssignment=CAN_FIFO0;                                                           
    CAN_FilterInitStructure.CAN_FilterActivation=ENABLE;                                                                                                                                                                                       
    CAN_FilterInit(&CAN_FilterInitStructure);
	NVIC_Config();
	CAN_ITConfig(CAN1, CAN_IT_FMP0, ENABLE);                                                                             
}
/************************************************************************
  * @描述:  KL线配置
  * @参数:  Baud:K线波特率
  * @返回值: None
  **********************************************************************/
void KL_LINE_Config(u32 Baud)
{
  u8 data;
  GPIO_InitTypeDef GPIO_InitStructure;
  USART_InitTypeDef USART_InitStructure;
  if (KL_LINEAPB == APB1)
  {
    RCC_APB2PeriphClockCmd(RCC_APBxPeriph_KL_LINE_IO | RCC_APB2Periph_AFIO,ENABLE);
    RCC_APB1PeriphClockCmd(RCC_APBxPeriph_KL_LINE,ENABLE);
  }
  else
  {
	RCC_APB2PeriphClockCmd(RCC_APBxPeriph_KL_LINE_IO | RCC_APBxPeriph_KL_LINE | RCC_APB2Periph_AFIO,ENABLE);
  }
  if (KL_LINE_PinRemap == ENABLE)
  {  				 
    GPIO_PinRemapConfig(GPIO_Remap_USART1,ENABLE);
  }
  GPIO_InitStructure.GPIO_Pin = KL_LINE_TXD;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_AF_PP;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(KL_LINE_IO,&GPIO_InitStructure);
  GPIO_WriteBit(GPIOB, GPIO_Pin_6, Bit_SET);

  GPIO_InitStructure.GPIO_Pin = KL_LINE_RXD;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_IN_FLOATING;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(KL_LINE_IO,&GPIO_InitStructure);

  USART_InitStructure.USART_BaudRate = Baud;
  USART_InitStructure.USART_WordLength = USART_WordLength_8b;
  USART_InitStructure.USART_StopBits = USART_StopBits_1;
  USART_InitStructure.USART_Parity = USART_Parity_No;
  USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None ;
  USART_InitStructure.USART_Mode = USART_Mode_Rx|USART_Mode_Tx;
  USART_Init(KL_LINE,&USART_InitStructure);
  data = data;
  data = KL_LINE->DR;
  data = KL_LINE->SR;
  USART_ITConfig(KL_LINE,USART_IT_RXNE,ENABLE);
  USART_Cmd(KL_LINE,ENABLE);
}
/************************************************************************
  * @描述:  使能 失能KL线
  * @参数:   K_LINE L_LINE Destroy 
  * @返回值: None							   
  **********************************************************************/
void KL_LINE_Enable(u8 KL)
{
  GPIO_InitTypeDef GPIO_InitStructure;

  delay_init(72);
  RCC_APB2PeriphClockCmd(RCC_APB2Periph_SET_K_LINE_IO | RCC_APB2Periph_SET_L_LINE_IO | RCC_APB2Periph_AFIO,ENABLE);	  // 

  GPIO_PinRemapConfig(GPIO_Remap_SWJ_JTAGDisable, ENABLE);	//禁用 JTAG		   
  GPIO_InitStructure.GPIO_Pin = SET_K_LINE_PIN;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(SET_K_LINE_IO,&GPIO_InitStructure);
  GPIO_WriteBit(SET_K_LINE_IO, SET_K_LINE_PIN, Bit_SET);

  GPIO_InitStructure.GPIO_Pin = SET_L_LINE_PIN;
  GPIO_InitStructure.GPIO_Mode = GPIO_Mode_Out_PP;
  GPIO_InitStructure.GPIO_Speed = GPIO_Speed_50MHz;
  GPIO_Init(SET_L_LINE_IO,&GPIO_InitStructure);
  GPIO_WriteBit(SET_L_LINE_IO, SET_L_LINE_PIN, Bit_SET);
 
  delay_ms(100);
  if (KL == 1)
  {
	GPIO_WriteBit(SET_K_LINE_IO, SET_K_LINE_PIN, Bit_RESET);
  }
  else if (KL == 2)
  {
	GPIO_WriteBit(SET_L_LINE_IO, SET_L_LINE_PIN, Bit_RESET);
  }
  else if (KL == 3)
  {
	GPIO_WriteBit(SET_K_LINE_IO, SET_K_LINE_PIN, Bit_RESET);
	GPIO_WriteBit(SET_L_LINE_IO, SET_L_LINE_PIN, Bit_RESET);
  }
}
/************************************************************************
  * @描述:  发送一帧K线数据 (KWP2000协议)
  * @参数:  
  * @返回值: None
  **********************************************************************/
__IO u8 RxRAM[100];
void Send_KWP2000Frame(u8* TxMessage)
{
  u8 framelen,i,data;
  data = data;
  data = KL_LINE->DR;
  data = KL_LINE->SR;
  data = 0;
  if (TxMessage[0] == 0x80)
  {
    framelen = TxMessage[3] + 5;      
  }
  else if ((TxMessage[0]&0xf0) == 0x80)
  {
    framelen = TxMessage[0] - 0x7c;
  }
  else if (TxMessage[0] == 0xc0)
  {
    framelen = TxMessage[3] + 5;
  }
  else if (TxMessage[0] == 0xcc)
  {
    framelen = 1;
	data = 0xcc;
  }
  else if ((TxMessage[0]&0xf0) == 0xc0)
  {
    framelen = TxMessage[0] - 0xbc;
  }
  for (i = 0;i < framelen-1;i++)				//生成累加和效验字节
  {
    data +=	TxMessage[i];
  }
  for (i = 0;i < framelen-1;i++)
  {
    USART_SendData(KL_LINE, TxMessage[i]);
	delay_ms(1);
  }
  USART_SendData(KL_LINE, data);
}
/************************************************************************
  * @描述:  发送一帧K线数据 (ISO9141协议)
  * @参数:  u8* TxMessage
  * @返回值: NONE
  **********************************************************************/
void Send_ISO9141Frame(u8* TxMessage)
{
  u8 i,framelen,data = 0;
  data = data;
  data = KL_LINE->DR;
  data = KL_LINE->SR;
  data = 0;
  framelen = TxMessage[0];
  if (framelen == 1)
  {
    data = 0xcc;
  }
  for (i = 0;i < framelen-1;i++)				//生成累加和效验字节
  {
    data +=	TxMessage[i+1];
  }
  for (i = 0;i < framelen - 1;i++)
  {
    USART_SendData(KL_LINE, TxMessage[i+1]);
	delay_ms(1);
  }
  USART_SendData(KL_LINE, data);
}
/************************************************************************
  * @描述:  清空内存
  * @参数:   u8* ram 需要清空的内存指针 u32 n 清空内存的大小
  * @返回值: NONE
  **********************************************************************/
void ClearRAM(u8* ram,u32 n)
{
  u32 i;
  for (i = 0;i < n;i++)
  {
    ram[i] = 0x00;
  }
}
