/**
  ******************************************************************************
  * @file    EXTI/stm32f10x_it.c 
  * @author  MCD Application Team
  * @version V3.3.0
  * @date    04/16/2010
  * @brief   Main Interrupt Service Routines.
  *          This file provides template for all exceptions handler and peripherals
  *          interrupt service routine.
  ******************************************************************************
  * @copy
  *
  * THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS
  * WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE
  * TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY
  * DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS ARISING
  * FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS OF THE
  * CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.
  *
  * <h2><center>&copy; COPYRIGHT 2010 STMicroelectronics</center></h2>
  */

/* Includes ------------------------------------------------------------------*/
#include "includes.h"
/** @addtogroup STM32F10x_StdPeriph_Examples
  * @{
  */

/** @addtogroup EXTI_Example
  * @{
  */ 

/* Private typedef -----------------------------------------------------------*/
/* Private define ------------------------------------------------------------*/
/* Private macro -------------------------------------------------------------*/
/* Private variables ---------------------------------------------------------*/
/* Private function prototypes -----------------------------------------------*/
/* Private functions ---------------------------------------------------------*/

/******************************************************************************/
/*            Cortex-M3 Processor Exceptions Handlers                         */
/******************************************************************************/

/**
  * @brief  This function handles NMI exception.
  * @param  None
  * @retval None
  */
void NMI_Handler(void)
{
}

/**
  * @brief  This function handles Hard Fault exception.
  * @param  None
  * @retval None
  */
void HardFault_Handler(void)
{
  /* Go to infinite loop when Hard Fault exception occurs */
  while (1)
  {
  }
}

/**
  * @brief  This function handles Memory Manage exception.
  * @param  None
  * @retval None
  */
void MemManage_Handler(void)
{
  /* Go to infinite loop when Memory Manage exception occurs */
  while (1)
  {
  }
}

/**
  * @brief  This function handles Bus Fault exception.
  * @param  None
  * @retval None
  */
void BusFault_Handler(void)
{
  /* Go to infinite loop when Bus Fault exception occurs */
  while (1)
  {
  }
}

/**
  * @brief  This function handles Usage Fault exception.
  * @param  None
  * @retval None
  */
void UsageFault_Handler(void)
{
  /* Go to infinite loop when Usage Fault exception occurs */
  while (1)
  {
  }
}

/**
  * @brief  This function handles SVCall exception.
  * @param  None
  * @retval None
  */
void SVC_Handler(void)
{
}

/**
  * @brief  This function handles Debug Monitor exception.
  * @param  None
  * @retval None
  */
void DebugMon_Handler(void)
{
}

/**
  * @brief  This function handles PendSV_Handler exception.
  * @param  None
  * @retval None
  */
void PendSV_Handler(void)
{
}

/**
  * @brief  This function handles SysTick Handler.
  * @param  None
  * @retval None
  */
void SysTick_Handler(void)
{
}



/******************************************************************************/
/*                 STM32F10x Peripherals Interrupt Handlers                   */
/*  Add here the Interrupt Handler for the used peripheral(s) (PPP), for the  */
/*  available peripheral interrupt handler's name please refer to the startup */
/*  file (startup_stm32f10x_xx.s).                                            */
/******************************************************************************/
/**
  * @brief  This function handles USART2 interrupt request.
  * @param  None
  * @retval None
  */
__IO u8 ATCmd[100];
__IO u8 ATLEN = 0;
__IO MenuType CotrolVale = SYSEND;
void PC_USART_IRQHandler(void)
{
  u8 data;
  data = data;
  if (USART_GetFlagStatus(PC_USART,USART_FLAG_ORE)!= RESET)
  {
    data = PC_USART->DR;
	USART_ITConfig(PC_USART,USART_IT_IDLE,ENABLE);
  }
  if (USART_GetFlagStatus(PC_USART,USART_IT_RXNE)!= RESET) 
  {
    ATCmd[ATLEN++] = PC_USART->DR;
	USART_ITConfig(PC_USART,USART_IT_IDLE,ENABLE);
  }
  if(USART_GetFlagStatus(PC_USART,USART_FLAG_IDLE)!= RESET)
  {
    data = PC_USART->DR;
    USART_ITConfig(PC_USART,USART_IT_IDLE,DISABLE);
	if (!strncmp((const char *)ATCmd,"AT+ISO15765-4STD_500K",21))
	{
	  CotrolVale = SYS01;
	  ClearRAM((u8*)ATCmd,100);
	}
	else if (!strncmp((const char *)ATCmd,"AT+ISO15765-4EXT_500K",21))
	{
	  CotrolVale = SYS02;
	  ClearRAM((u8*)ATCmd,100);
	}
	else if (!strncmp((const char *)ATCmd,"AT+ISO15765-4STD_250K",21))
	{
	  CotrolVale = SYS03;
	  ClearRAM((u8*)ATCmd,100);
	}
	else if (!strncmp((const char *)ATCmd,"AT+ISO15765-4EXT_250K",21))
	{
	  CotrolVale = SYS04;
	  ClearRAM((u8*)ATCmd,100);
	}
	else if (!strncmp((const char *)ATCmd,"AT+ISO14230-4ADDR",17))
	{
	  CotrolVale = SYS05;
	  ClearRAM((u8*)ATCmd,100);
	}
	else if (!strncmp((const char *)ATCmd,"AT+ISO14230-4HL",15))
	{
	  CotrolVale = SYS06;
	  ClearRAM((u8*)ATCmd,100);
	}
	else if (!strncmp((const char *)ATCmd,"AT+ISO9141-2ADDR",16))
	{
	  CotrolVale = SYS07;
	  ClearRAM((u8*)ATCmd,100);
	}
	else if (!strncmp((const char *)ATCmd,"AT+DTC",6))
	{
	  CotrolVale =  SYSDTC;
	}
	else if (!strncmp((const char *)ATCmd,"AT+07DTC",8))
	{
	  CotrolVale =  SYSDTC1;
	}
	else if (!strncmp((const char *)ATCmd,"AT+0ADTC",8))
	{
	  CotrolVale =  SYSDTC2;
	}
	else if (!strncmp((const char *)ATCmd,"AT+SDS",6))
	{
	  CotrolVale =  SYSDS;
	}
	else if (!strncmp((const char *)ATCmd,"AT+VIN",6))
	{
	  CotrolVale =  SYSVIN;
	}
	else
	{
	  printf(STR000);
	  printf("\r\n");
	  CotrolVale = SYSXX;
	  ClearRAM((u8*)ATCmd,100);
	}
	ATLEN = 0;
  }
}

__IO u8 TIM2Count = 0;
void TIM2_IRQHandler(void)
{
  if (TIM_GetITStatus(TIM2, TIM_IT_Update) != RESET)
  {
    TIM_ClearITPendingBit(TIM2, TIM_IT_Update);
	LED_ONOFF(&LEDControlSP->LEDSwitch[TIM2Count++][0]);
	if (TIM2Count >= LEDCycleNum)
	{
	  TIM2Count = 0;
	}
  }
}
IRQVarTypeDef IRQVar;
void USB_LP_CAN1_RX0_IRQHandler(void)
{
  CanRxMsg RxMessage;
  __IO u8 i;
  CAN_Receive(CAN1, CAN_FIFO0, &RxMessage);
  if (RxMessage.Data[0] != 0x30 && IRQVar.CANCPT->CANRXCMD[0].CANRxFrame.ExtId == RxMessage.ExtId || RxMessage.Data[0] != 0x30 && IRQVar.CANCPT->CANRXCMD[0].CANRxFrame.StdId == RxMessage.StdId)
  {
    for (IRQVar.CPS = 0;IRQVar.CPS < IRQVar.CANCPT->RXCMDCNT;IRQVar.CPS++)
	{
	  IRQVar.flag = SET;
	  for (i = 0;i < IRQVar.CANCPT->CANRXCMD[IRQVar.CPS].CN;i++)
	  {
	    if (IRQVar.CANCPT->CANRXCMD[IRQVar.CPS].CANRxFrame.Data[i] != RxMessage.Data[i])
		{
		  IRQVar.flag = RESET;
		  break;
		}		
	  }
	  if (IRQVar.flag == SET) 
	  {
	    IRQVar.CPL = SET;
		IRQVar.Flag30H = RESET;
	    break;
	  }
	}
  }
  else
  {
    IRQVar.CPL = SET;
	IRQVar.Flag30H = SET;
  }
}
/************************************************************************
  * @描述:  K线中断
  * @参数:  None
  * @返回值: None
  **********************************************************************/
void K_LINE_IRQHandler(void)
{
  __IO u8 i;
  IRQVar.ACCBYTE = 0;
  if (USART_GetFlagStatus(KL_LINE,USART_IT_RXNE)!= RESET) 
  {
    if (IRQVar.LEN == 0 && KL_LINE->DR == 0)
	{
	  if((++IRQVar.acctivecnt) >= 3)
	  {
	    IRQVar.acctiveflag = SET;
	  }
	  return;
	}
	else if (IRQVar.LEN == 0 && KL_LINE->DR == 0x70)
	{
	  IRQVar.flag = SET;
	  IRQVar.CPS = 116;
	  IRQVar.LEN = 0;
	  return;
	}
    RxRAM[IRQVar.LEN++] = KL_LINE->DR;
    for (i = 0;i < IRQVar.LEN-1;i++)
	{
	  IRQVar.ACCBYTE += RxRAM[i];
	}
	if (IRQVar.ACCBYTE == RxRAM[IRQVar.LEN-1] && IRQVar.LEN > 3)	//
	{
      for (IRQVar.CPS = 0;IRQVar.CPS < IRQVar.KCPT->RXCMDCNT;IRQVar.CPS++)
	  {
	    IRQVar.flag = SET;
	    for (i = 0;i < IRQVar.KCPT->KRXCMD[IRQVar.CPS].CN;i++)
	    {
	      if (IRQVar.KCPT->KRXCMD[IRQVar.CPS].data[i] != RxRAM[i])
		  {
		    IRQVar.flag = RESET;
			IRQVar.LEN = 0;
		    break;
		  }		
	    }
		if(IRQVar.flag == SET)
		{
		  IRQVar.LEN = 0;
		  break;
		}
	  }
	}
  }
}
/**
  * @}
  */ 

/**
  * @}
  */ 

/******************* (C) COPYRIGHT 2010 STMicroelectronics *****END OF FILE****/
