
#ifndef __ISO14230_4_H
#define __ISO14230_4_H


#define DSTotal14230  151

typedef enum {ADDR = 0, HL} KActiveType;

void ISO14230_4Setting(KLINETabTypeDef *tab,u32 Baud);
void SetISO14230CmdTabRam(u8 *dtcram,u8 p);


#endif
