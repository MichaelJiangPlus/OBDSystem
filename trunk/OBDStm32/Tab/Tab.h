

#ifndef __TAB_H
#define __TAB_H

typedef struct
{
  u32 CN;
  CanRxMsg CANRxFrame;
  u8     REQ;
} CPCMD;

typedef struct
{
  u32 CN;
  u8  data[15];
  u8     REQ;
} KRXCMDTypeDef;

typedef struct
{
  u8  nf;
  u8  data[15];
} KTXCMDTypeDef;

typedef struct
{
  u16 STDID1; 
  u16 STDID2;
  u16 MSTDID1;
  u16 MSTDID2;
  u32 EXTID1;   
  u32 EXTID2;
  u32 MEXTID1;
  u32 MEXTID2;
  u32 RXCMDCNT;  
  CPCMD	  CANRXCMD[150];
  CanTxMsg  CANTXCMD[150];  
}CANCPTabTypeDef;

typedef struct
{
  u32 RXCMDCNT;
  KRXCMDTypeDef KRXCMD[150];
  KTXCMDTypeDef KTXCMD[150];
}KLINETabTypeDef;
 
extern CANCPTabTypeDef  CANCPTab1;
extern KLINETabTypeDef	KWP2000CPTAB;
extern KLINETabTypeDef	 ISO9141CPTAB;
#endif


