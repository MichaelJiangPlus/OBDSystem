

#include "includes.h"


int main(void)
{
  Bsp_Init();
  while(1)
  { 
  	IRQVar.LEN = 0;
	IRQVar.flag = RESET;
    switch (CotrolVale)
    {
      case SYS01:  ISO15765_4Setting(&CANCPTab1,CAN_500K,CAN_ID_STD);  
      break;
 	  case SYS02:  ISO15765_4Setting(&CANCPTab1,CAN_500K,CAN_ID_EXT);
 	  break;
 	  case SYS03:  ISO15765_4Setting(&CANCPTab1,CAN_250K,CAN_ID_STD);
 	  break;
 	  case SYS04:  ISO15765_4Setting(&CANCPTab1,CAN_250K,CAN_ID_EXT);
 	  break;
	  case SYS05:  ISO14230_4Setting(&KWP2000CPTAB,10400);
	  break;
	  case SYS06:  ISO14230_4Setting(&KWP2000CPTAB,10400);
	  break;
	  case SYS07:  ISO9141_2Setting(&ISO9141CPTAB,10400);
	  break;
	  case SYSEND:
	  break;
	  case SYSXX:
	  break;
      default:
	  printf(STR001);
	  CotrolVale = SYSEND;
      break;
  }
  }
}


