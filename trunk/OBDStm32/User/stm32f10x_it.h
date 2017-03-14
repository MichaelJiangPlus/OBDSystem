/**
  ******************************************************************************
  * @file    EXTI/stm32f10x_it.h 
  * @author  MCD Application Team
  * @version V3.3.0
  * @date    04/16/2010
  * @brief   This file contains the headers of the interrupt handlers.
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

/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __STM32F10x_IT_H
#define __STM32F10x_IT_H

/* Includes ------------------------------------------------------------------*/
#include "stm32f10x.h"


void NMI_Handler(void);
void HardFault_Handler(void);
void MemManage_Handler(void);
void BusFault_Handler(void);
void UsageFault_Handler(void);
void SVC_Handler(void);
void DebugMon_Handler(void);
void PendSV_Handler(void);
void SysTick_Handler(void);
void PC_USART_IRQHandler(void);
void TIM2_IRQHandler(void);
void USB_LP_CAN1_RX0_IRQHandler(void);
typedef struct
{
  __IO u32 CPS;
  __IO CANCPTabTypeDef *CANCPT;
  __IO FlagStatus flag;
  __IO FlagStatus CPL;
  __IO FlagStatus Flag30H;
  __IO u8 NFrameLen;
  __IO u8	LEN;
  __IO u8 ACCBYTE;
  __IO KLINETabTypeDef *KCPT;
  __IO u8 acctivecnt;
  __IO FlagStatus acctiveflag;
}IRQVarTypeDef;
extern __IO u8 TIM2Count;
extern __IO u8 ATCmd[100];
extern __IO u8 ATLEN;
extern __IO MenuType CotrolVale;
extern IRQVarTypeDef IRQVar;
#endif /* __STM32F10x_IT_H */

/******************* (C) COPYRIGHT 2010 STMicroelectronics *****END OF FILE****/
