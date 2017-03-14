
#ifndef __BSP_H
#define __BSP_H
/**********************************************************************/
#define  NONE           0
/****************************结构类型**********************************/
typedef enum {APB1 = 0, APB2} APBType;
typedef enum {OFF = 0, ON} SwitchType;
typedef enum {SYSEND = 0, SYS01,SYS02,SYS03,SYS04,SYS05,SYS06,SYS07,SYSXX,SYSDTC,SYSDTC1,SYSDTC2,SYSDS,SYSVIN} MenuType;
typedef enum {TRUE = 0, ERR0,ERR1,ERR2,ERR3} ERRORType;
typedef enum {Numeric = 0, Character} DataType;
typedef struct
{
  __IO u8 CycleNum;
  __IO u32 T2Delayms;
  __IO SwitchType LEDSwitch[8][7];
}LEDControldef;

typedef struct
{
  __IO DataType	Type;
  const char *str;
  u8 Y;
  u8 X;
  u8 ByteNum;
  u8 ChLen;
  char *Format;
  float (*Equation0)(float data);
  char* unit;
  u32 (*Equation1)(char* data,ERRORType *err);
  char* range;
}DSControlTypeDef;
/****************************函数声明**********************************/
void Bsp_Init(void);
void PC_USART_Config(u32 Baud);
void NVIC_Config(void);
void TIM2_Config(u32 T2Delayms);
void LED_Config(const LEDControldef* LEDControl);
void LED_ONOFF(__IO SwitchType* sw);
void CAN_GPIOConfig(void);
void CAN_Config(u8 velocity);
void CAN1_Config32BitFilter(u32 id1, u32 id2, u32 mid1, u32 mid2);
void CAN1_Config16BitFilter(u16 id1, u16 id2, u16 mid1, u16 mid2);
void KL_LINE_Config(u32 Baud);
void KL_LINE_Enable(u8 KL);
void Send_KWP2000Frame(u8* TxMessage);
void Send_ISO9141Frame(u8* TxMessage);
void ClearRAM(u8* ram,u32 n);
/**************************全局变量************************************/
extern  __IO u8 LEDCycleNum;
extern  __IO LEDControldef* LEDControlSP;
extern __IO u8 RxRAM[100];
extern __IO u8 TransmitMailbox;
/**************************PC串口配置**********************************/
#define RCC_APBxPeriph_PC_USART_IO   RCC_APB2Periph_GPIOA
#define RCC_APBxPeriph_PC_USART		 RCC_APB1Periph_USART2
#define PC_USART_TXD				 GPIO_Pin_2
#define PC_USART_RXD				 GPIO_Pin_3
#define PC_USART_IO					 GPIOA
#define PC_USART	                 USART2
#define PC_PinRemap					 DISABLE
#define PC_USARTAPB					 APB1
#define PC_USART_IRQHandler			 USART2_IRQHandler
/**************************LED配置*************************************/
#define RCC_APBxPeriph_LED_IO_B	     RCC_APB2Periph_GPIOB
#define RCC_APBxPeriph_LED_IO_A	     RCC_APB2Periph_GPIOA
#define LED_D0						 GPIO_Pin_4
#define LED_D1						 GPIO_Pin_5
#define LED_D2						 GPIO_Pin_6
#define LED_D3						 GPIO_Pin_7
#define LED_D4						 GPIO_Pin_0
#define LED_D5						 GPIO_Pin_1
#define LED_D6						 GPIO_Pin_2
#define LED_IO_A				     GPIOA
#define LED_IO_B				     GPIOB
/**************************CAN配置*************************************/
#define CAN_500K                     4
#define CAN_250K                     8
#define CAN_125K                     16
#define RCC_APBxPeriph_CAN_IO        RCC_APB2Periph_GPIOB
#define CAN_RXD					     GPIO_Pin_8
#define CAN_TXD						 GPIO_Pin_9
#define CAN_IO						 GPIOB
#define CAN_PinRemap				 ENABLE
/**************************KL线配置************************************/
#define K_LINE                       1
#define L_LINE					     2
#define Destroy                      0xff
#define RCC_APBxPeriph_KL_LINE_IO    RCC_APB2Periph_GPIOB
#define RCC_APBxPeriph_KL_LINE	     RCC_APB2Periph_USART1
#define KL_LINE_TXD				     GPIO_Pin_6
#define KL_LINE_RXD				     GPIO_Pin_7
#define KL_LINE_IO				     GPIOB
#define KL_LINE	                     USART1
#define KL_LINE_PinRemap		     ENABLE
#define KL_LINEAPB				     APB2
#define K_LINE_IRQHandler		     USART1_IRQHandler
/**************************KL控制配置**********************************/
#define RCC_APB2Periph_SET_K_LINE_IO RCC_APB2Periph_GPIOB
#define RCC_APB2Periph_SET_L_LINE_IO RCC_APB2Periph_GPIOB
#define SET_K_LINE_PIN				 GPIO_Pin_3
#define SET_K_LINE_IO				 GPIOB
#define SET_L_LINE_PIN				 GPIO_Pin_4
#define SET_L_LINE_IO				 GPIOB
/**********************************************************************/
#endif
