

#include "includes.h"

const DSControlTypeDef  DSControl15765[DSTotal15765] = { 
{Numeric,  STR007,  4,3,1, 0,"%3d",  gzmsl   ,"个  ",NONE    ,"000<=data<=127"   },	//000
{Character,STR008,  4,3,1, 2,"   ",  NONE    ,"    ",gzdzt   ,"ON 、OFF"         },	//001
{Character,STR010,  4,4,1, 2,"   ",  NONE    ,"    ",zcshjc  ,"支持 、不支持"    },	//002
{Character,STR011,  4,4,1, 2,"   ",  NONE    ,"    ",zcryjc  ,"支持 、不支持"    },	//003
{Character,STR012,  4,4,1, 2,"   ",  NONE    ,"    ",zcbjjc  ,"支持 、不支持"    },	//004
{Character,STR013,  4,4,1, 2,"   ",  NONE    ,"    ",shjcjx  ,"OK 、未完成"      },	//005
{Character,STR014,  4,4,1, 2,"   ",  NONE    ,"    ",ryjcjx  ,"OK 、未完成"      },	//006
{Character,STR015,  4,4,1, 2,"   ",  NONE    ,"    ",bjjcjx  ,"OK 、未完成"      },	//007
{Character,STR016,  4,5,1, 2,"   ",  NONE    ,"    ",zcshjc  ,"支持 、不支持"    },	//008
{Character,STR017,  4,5,1, 2,"   ",  NONE    ,"    ",zcryjc  ,"支持 、不支持"    },	//009
{Character,STR018,  4,5,1, 2,"   ",  NONE    ,"    ",zczyjc  ,"支持 、不支持"    },	//010
{Character,STR019,  4,5,1, 2,"   ",  NONE    ,"    ",zcfqjc  ,"支持 、不支持"    },	//011
{Character,STR020,  4,5,1, 2,"   ",  NONE    ,"    ",zcpmjc  ,"支持 、不支持"    },	//012
{Character,STR021,  4,5,1, 2,"   ",  NONE    ,"    ",zcegjc  ,"支持 、不支持"    },	//013
{Character,STR022,  4,6,1, 2,"   ",  NONE    ,"    ",nmhcjx  ,"OK 、未完成"      },	//014
{Character,STR023,  4,6,1, 2,"   ",  NONE    ,"    ",dyhcjx  ,"OK 、未完成"      },	//015
{Character,STR024,  4,6,1, 2,"   ",  NONE    ,"    ",zyyljx  ,"OK 、未完成"      },	//016
{Character,STR025,  4,6,1, 2,"   ",  NONE    ,"    ",ryjcjx  ,"OK 、未完成"      },	//017
{Character,STR026,  4,6,1, 2,"   ",  NONE    ,"    ",bjjcjx  ,"OK 、未完成"      },	//018
{Character,STR027,  4,6,1, 2,"   ",  NONE    ,"    ",zcegjx  ,"OK 、未完成"      },	//019
{Character,STR028, 18,4,1, 2,"   ",  NONE    ,"    ",zcshjc  ,"支持 、不支持"    },	//020
{Character,STR029, 18,4,1, 2,"   ",  NONE    ,"    ",zcryjc  ,"支持 、不支持"    },	//021
{Character,STR030, 18,4,1, 2,"   ",  NONE    ,"    ",zcbjjc  ,"支持 、不支持"    },	//022
{Character,STR031, 18,4,1, 2,"   ",  NONE    ,"    ",shjcjx  ,"OK 、未完成"      },	//023
{Character,STR032, 18,4,1, 2,"   ",  NONE    ,"    ",ryjcjx  ,"OK 、未完成"      },	//024
{Character,STR033, 18,5,1, 2,"   ",  NONE    ,"    ",zcshjc  ,"支持 、不支持"    },	//025
{Character,STR034, 18,5,1, 2,"   ",  NONE    ,"    ",zcryjc  ,"支持 、不支持"    },	//026
{Character,STR035, 18,5,1, 2,"   ",  NONE    ,"    ",zczyjc  ,"支持 、不支持"    },	//027
{Character,STR036, 18,5,1, 2,"   ",  NONE    ,"    ",zcfqjc  ,"支持 、不支持"    },	//028
{Character,STR037, 18,5,1, 2,"   ",  NONE    ,"    ",zcpmjc  ,"支持 、不支持"    },	//029
{Character,STR038, 18,5,1, 2,"   ",  NONE    ,"    ",zcegjc  ,"支持 、不支持"    },	//030
{Character,STR039, 18,6,1, 2,"   ",  NONE    ,"    ",nmhcjx  ,"OK 、未完成"      },	//031
{Character,STR040, 18,6,1, 2,"   ",  NONE    ,"    ",dyhcjx  ,"OK 、未完成"      },	//032
{Character,STR041, 18,6,1, 2,"   ",  NONE    ,"    ",zyyljx  ,"OK 、未完成"      },	//033
{Character,STR042, 18,6,1, 2,"   ",  NONE    ,"    ",ryjcjx  ,"OK 、未完成"      },	//034
{Character,STR043, 18,6,1, 2,"   ",  NONE    ,"    ",bjjcjx  ,"OK 、未完成"      },	//035
{Character,STR044, 18,6,1, 2,"   ",  NONE    ,"    ",zcegjx  ,"OK 、未完成"      },	//036
{Character,STR045, 19,3,2, 5,"     ",NONE    ,"    ",swpcbu  ,"PXXXX 、CXXXX 、BXXXX 、UXXXX 其中 0<=XXXX<=4000"},	//037
{Character,STR046, 20,3,1, 8,"     ",NONE    ,"    ",ryxizt  ,"OL 、CL 、OL-Drive 、OL-Fault 、CL-Fault 、--"},	//038
{Character,STR047, 20,4,1, 8,"     ",NONE    ,"    ",ryxizt  ,"OL 、CL 、OL-Drive 、OL-Fault 、CL-Fault 、--"},	//039
{Numeric  ,STR048, 21,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//040
{Numeric  ,STR049, 22,3,1, 0,"%3.0f",flqwd   ,"℃  ",NONE    ,"-40<=data<=215"   },	//041
{Numeric  ,STR050, 23,3,1, 0,"%3.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//042
{Numeric  ,STR051, 24,3,1, 0,"%3.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//043
{Numeric  ,STR052, 25,3,1, 0,"%3.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//044
{Numeric  ,STR053, 26,3,1, 0,"%3.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//045
{Numeric  ,STR054, 27,3,1, 0,"%3.0f",ygyl    ,"kPa ",NONE    ,"0<=data<=765"     },	//046
{Numeric  ,STR055, 28,3,1, 0,"%3.0f",qgjdyl  ,"kPa ",NONE    ,"0<=data<=255"     },	//047
{Numeric  ,STR056, 29,3,2, 0,"%5.2f",fdjzs   ,"Rpm ",NONE    ,"0<=data<=16383"   },	//048
{Numeric  ,STR057, 30,3,1, 0,"%3.0f",qgjdyl  ,"Km/H",NONE    ,"0<=data<=255"     },	//049
{Numeric  ,STR058, 31,3,1, 0,"%2.2f",qgdhtqj ,"BTDC",NONE    ,"-64<=data<=64"  },	//050
{Numeric  ,STR059, 32,3,1, 0,"%3.0f",flqwd   ,"℃  ",NONE    ,"-40<=data<=215"   },	//051
{Numeric  ,STR060, 33,3,2, 0,"%3.2f",zlkqll  ,"g/s ",NONE    ,"0<=data<=655.35"  },	//052
{Numeric  ,STR061, 34,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//053
{Character,STR062, 35,3,1, 4,"     ",NONE    ,"    ",zleckqps,"-- 、UPS 、DNS 、OFF 、DIAG"},	//054
{Character,STR063, 36,3,1, 5,"     ",NONE    ,"    ",ycgqwz  ,"B1-S1 、B1-S2 、B1-S3 、B1-S4 、B2-S1 、B2-S2 、B2-S3 、B2-S4 、--"},	//055
{Numeric  ,STR064, 37,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//056
{Numeric  ,STR065, 37,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//057
{Numeric  ,STR066, 38,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//058
{Numeric  ,STR067, 38,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//059
{Numeric  ,STR068, 39,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//060
{Numeric  ,STR069, 39,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//061
{Numeric  ,STR070, 40,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//062
{Numeric  ,STR071, 40,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//063
{Numeric  ,STR072, 41,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//064
{Numeric  ,STR073, 41,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//065
{Numeric  ,STR074, 42,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//066
{Numeric  ,STR075, 42,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//067
{Numeric  ,STR076, 43,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//068
{Numeric  ,STR077, 43,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//069
{Numeric  ,STR078, 44,3,1, 0,"%1.2f",ycgqdy  ,"V   ",NONE    ,"0<=data<=1.28"    },	//070
{Numeric  ,STR079, 44,4,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//071
{Character,STR080, 45,3,1,18,"     ",NONE    ,"    ",obdqq   ,"OBD 、OBD and OBD II 、OBDI 、NO OBD 、EOBD 、EOBD AND OBDII 、EOBD AND OBD 、EOBD OBD AND OBDII 、JOBD 、JOBD AND OBDII 、JOBD AND EOBD"},	//072
{Character,STR081, 46,3,1, 5,"     ",NONE    ,"    ",ycgq1   ,"B1-S1 、B1-S2 、B2-S1 、B2-S2 、B3-S1 、B3-S2 、B4-S1 、B4-S2 、--"},	//073
{Character,STR082, 47,3,1, 2,"     ",NONE    ,"    ",ptozt   ,"ON 、OFF"},	//074
{Numeric  ,STR083, 48,3,2, 0,"%5.0f",fdjqdsj ,"sec ",NONE    ,"0<=data<=65535"   },	//075
{Numeric  ,STR084, 50,3,2, 0,"%5.0f",fdjqdsj ,"Km  ",NONE    ,"0<=data<=65535"   },	//076
{Numeric  ,STR085, 51,3,2, 0,"%4.2f",qgzkyl  ,"kPa ",NONE    ,"0<=data<=5177.27" },	//077
{Numeric  ,STR086, 52,3,2, 0,"%6.0f",ygyl1   ,"kPa ",NONE    ,"0<=data<=655350"  },	//078
{Numeric  ,STR087, 53,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//079
{Numeric  ,STR088, 53,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//080
{Numeric  ,STR089, 54,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//081
{Numeric  ,STR090, 54,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//082
{Numeric  ,STR091, 55,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//083
{Numeric  ,STR092, 55,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//084
{Numeric  ,STR093, 56,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//085
{Numeric  ,STR094, 56,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//086
{Numeric  ,STR095, 57,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//087
{Numeric  ,STR096, 57,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//088
{Numeric  ,STR097, 58,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//089
{Numeric  ,STR098, 58,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//090
{Numeric  ,STR099, 59,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//091
{Numeric  ,STR100, 59,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//092
{Numeric  ,STR101, 60,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//093
{Numeric  ,STR102, 60,5,2, 0,"%1.5f",ycgqdy1 ,"V   ",NONE    ,"0<=data<=8"       },	//094
{Numeric  ,STR103, 61,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//095
{Numeric  ,STR104, 62,3,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//096
{Numeric  ,STR105, 63,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//097
{Numeric  ,STR106, 64,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//098
{Numeric  ,STR107, 65,3,1, 0,"%3.0f",qgjdyl  ,"    ",NONE    ,"0<=data<=255"     },	//099
{Numeric  ,STR108, 66,3,2, 0,"%5.0f",fdjqdsj ,"Km  ",NONE    ,"0<=data<=65535"   },	//100
{Numeric  ,STR109, 67,3,1, 0,"%4.0f",ryzqyl  ,"pa  ",NONE    ,"-8192<=data<=8128"},	//101
{Numeric  ,STR110, 68,3,1, 0,"%3.0f",qgjdyl  ,"kPa ",NONE    ,"0<=data<=255"     },	//102
{Numeric  ,STR111, 69,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//103
{Numeric  ,STR112, 69,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//104
{Numeric  ,STR113, 70,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//105
{Numeric  ,STR114, 70,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//106
{Numeric  ,STR115, 71,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//107
{Numeric  ,STR116, 71,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//108
{Numeric  ,STR117, 72,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//109
{Numeric  ,STR118, 72,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//110
{Numeric  ,STR119, 73,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//111
{Numeric  ,STR120, 73,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//112
{Numeric  ,STR121, 74,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//113
{Numeric  ,STR122, 74,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//114
{Numeric  ,STR123, 75,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//115
{Numeric  ,STR124, 75,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//116
{Numeric  ,STR125, 76,3,2, 0,"%1.5f",dlb     ,"    ",NONE    ,"0<=data<=2"       },	//117
{Numeric  ,STR126, 76,5,1, 0,"%3.2f",ycgqdl  ,"mA  ",NONE    ,"-128<=data<=128"  },	//118
{Numeric  ,STR127, 77,3,2, 0,"%4.1f",chjwd   ,"℃  ",NONE    ,"-40<=data<=6513.5"},	//119
{Numeric  ,STR128, 78,3,2, 0,"%4.1f",chjwd   ,"℃  ",NONE    ,"-40<=data<=6513.5"},	//120
{Numeric  ,STR129, 79,3,2, 0,"%4.1f",chjwd   ,"℃  ",NONE    ,"-40<=data<=6513.5"},	//121
{Numeric  ,STR130, 80,3,2, 0,"%4.1f",chjwd   ,"℃  ",NONE    ,"-40<=data<=6513.5"},	//122
{Numeric  ,STR131, 83,3,2, 0,"%2.3f",kzmkdy  ,"V   ",NONE    ,"0<=data<=65.53"   },	//123
{Numeric  ,STR132, 84,3,2, 0,"%5.2f",jdfhz   ,"    ",NONE    ,"0<=data<=25700"   },	//124
{Numeric  ,STR133, 85,3,2, 0,"%1.5f",dlb     ,"%%  ",NONE    ,"0<=data<=2"       },	//125
{Numeric  ,STR134, 86,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//126
{Numeric  ,STR135, 87,3,1, 0,"%3.0f",flqwd   ,"℃  ",NONE    ,"-40<=data<=215"   },	//127
{Numeric  ,STR136, 88,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//128
{Numeric  ,STR137, 89,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//129
{Numeric  ,STR138, 90,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//130
{Numeric  ,STR139, 91,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//131
{Numeric  ,STR140, 92,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//132
{Numeric  ,STR141, 93,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//133
{Numeric  ,STR142, 94,3,2, 0,"%5.0f",fdjqdsj ,"m   ",NONE    ,"0<=data<=65535"   },	//134
{Numeric  ,STR143, 95,3,2, 0,"%5.0f",fdjqdsj ,"m   ",NONE    ,"0<=data<=65535"   },	//135
{Character,STR144, 98,3,1, 7,"     ",NONE    ,"    ",rllx    ,"GAS 、METH 、ETH 、DSL 、LPG 、CNG 、PROP 、ELEC 、BI_GAS 、BI_METH 、BI_ETH 、BI_LPG 、BI_CNG 、BI_PROP 、BI_ELEC 、ISO/SAE"},	//136
{Numeric  ,STR145, 99,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//137
{Numeric  ,STR146,100,3,2, 0,"%3.2f",yyzqyl  ,"Kpa ",NONE    ,"0<=data<=327.68"  },	//138
{Numeric  ,STR147,101,3,2, 0,"%5.0f",zqpfxtyl,"pa  ",NONE    ,"0<=data<=32767"   },	//139
{Numeric  ,STR148,102,3,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//140
{Numeric  ,STR149,103,3,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//141
{Numeric  ,STR150,104,3,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//142
{Numeric  ,STR151,105,3,1, 0,"%2.2f",dqryxz  ,"%%  ",NONE    ,"-100<=data<=99.22"},	//143
{Numeric  ,STR152,106,3,2, 0,"%5.0f",fdjqdsj1 ,"Kpa ",NONE    ,"0<=data<=65535"   },	//144
{Numeric  ,STR153,107,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//145
{Numeric  ,STR154,108,3,1, 0,"%3.2f",fhjsz   ,"%%  ",NONE    ,"0<=data<=100"     },	//146
{Numeric  ,STR155,109,3,1, 0,"%5.0f",flqwd   ,"℃  ",NONE    ,"-40<=data<=215"   },	//147
{Numeric  ,STR156,110,3,2, 0,"%3.2f",pyzs    ,"`   ",NONE    ,"0<=data<=511.99"  },	//148
{Numeric  ,STR157,111,3,2, 0,"%4.2f",fdjgyl  ,"L/h ",NONE    ,"0<=data<=3276.75" },	//149
{Numeric  ,STR158,112,3,1, 0,"%3.1f",clpfyq  ,"    ",NONE    ,"-125<=data<=130"  },	//150
                                                       };
   

u8 DTCRAM[21];
u8 DSRAM[2];
u8 VINRAM[17] = {0};
__IO u8 DSOLDNUM = 0xff; 
void ISO15765_4Setting(CANCPTabTypeDef *CANCPTab,u8 speed,u8 CANStype)
{  
  u8 i,nframe,Num;
  __IO MenuType CotrolVale1 = SYSEND;
  char str[100];
  ABdef AB[3] = {
             {6,5},
			 {8,9},
			 {8,13},
             };
  ERRORType err;

  IRQVar.CANCPT = CANCPTab;
  CAN_GPIOConfig();
  CAN_Config(speed);
  if (CANStype == CAN_ID_STD)
  {
	CAN1_Config16BitFilter(CANCPTab->STDID1,CANCPTab->STDID2,CANCPTab->MSTDID1,CANCPTab->MSTDID2);
  }
  else
  {
	CAN1_Config32BitFilter(CANCPTab->EXTID1,CANCPTab->EXTID2,CANCPTab->MEXTID1,CANCPTab->MEXTID2);
  }
  IRQVar.CPL = RESET;
  CotrolVale1 = CotrolVale;
  while(CotrolVale != SYSEND && CotrolVale != SYS05 && CotrolVale != SYS06 && CotrolVale != SYS07)      
  {
    if (CotrolVale == SYS01)
	{
	  if (CotrolVale1 != SYS01)
	  {
	    break;
	  }
	  printf(STR006);
	  CotrolVale = SYSXX;
	}
	else if (CotrolVale == SYS02)
	{
	  if (CotrolVale1 != SYS02)
	  {
	    break;
	  }
	  printf(STR163);
	  CotrolVale = SYSXX;
	}
	else if (CotrolVale == SYS03)
	{
	  if (CotrolVale1 != SYS03)
	  {
	    break;
	  }
	  printf(STR164);
	  CotrolVale = SYSXX;
	}
	else if (CotrolVale == SYS04)
	{
	  if (CotrolVale1 != SYS04)
	  {
	    break;
	  }
	  printf(STR165);
	  CotrolVale = SYSXX;
	}
    else if (CotrolVale == SYSDTC || CotrolVale == SYSDTC1 || CotrolVale == SYSDTC2)
	{
	   if (CotrolVale == SYSDTC)
	   {
	     i = 0;
	   }
	   else if (CotrolVale == SYSDTC1)
	   {
		 i = 1;
	   }
	   else if (CotrolVale == SYSDTC2)
	   {
		 i = 2;
	   }
	   switch(SetDTCRAM(&ATCmd[AB[i].A],DTCRAM))
	   {
	     case ERR0:
		 printf(STR000);
		 printf("\r\n");
		 break;
		 case ERR1:
		 printf(STR002);
		 break;
		 default:
		 SetDTCTab(DTCRAM,AB[i].B,iso15765);
		 if (CotrolVale == SYSDTC)
		 {
		   printf(STR003);
		 }
		 else if (CotrolVale == SYSDTC1)
		 {
		   printf(STR004);
		 }
		 else if (CotrolVale == SYSDTC2)
		 {
		   printf(STR005);
		 }
		 ClearRAM((u8*)str,100);
		 strncpy(str,(char*)&ATCmd[AB[i].A+3],DTCRAM[0]*6);
		 printf((const char*)str);
		 printf("\r\n");
		 ClearRAM((u8*)DTCRAM,21);
		 ClearRAM((u8*)str,100);
		 ClearRAM((u8*)ATCmd,100);
		 break;
	   }
	   CotrolVale = SYSXX;
	}
	else if (CotrolVale == SYSDS)
	{
	  Num = GetDSNumber(&ATCmd[6],&err);
	  if (err == ERR0)
	  {
	    printf(STR000);
		printf("\r\n");
		CotrolVale = SYSXX;
		ClearRAM((u8*)ATCmd,100);
	    ClearRAM((u8*)DSRAM,2);
		continue;
	  }
	  if (DSOLDNUM != Num)
	  {
	    for (i = 0; i < DSControl15765[Num].ByteNum;i++)	
	    {
	     Count = CANCPTab->CANTXCMD[DSControl15765[Num].Y].Data[DSControl15765[Num].X + i];
	    }
		DSOLDNUM = Num;
	  }
	  err = SetDSRAM(ATCmd,DSControl15765,DSRAM);	//===========================================
	  if (err == ERR0)
	  {
        printf(STR000);
	    printf(STR009);
		printf("\"\r\n");
		CotrolVale = SYSXX;
	    ClearRAM((u8*)ATCmd,100);
	    ClearRAM((u8*)DSRAM,2);
		continue;
	  }
	  else if (err == ERR1)
	  {
		printf(STR000);
		printf(STR160);
		printf(DSControl15765[Num].range);
		printf("\r\n");
		CotrolVale = SYSXX;
	    ClearRAM((u8*)ATCmd,100);
	    ClearRAM((u8*)DSRAM,2);
		continue;
	  }
	  for (i = 0; i < DSControl15765[Num].ByteNum;i++)	
	  {
	    CANCPTab->CANTXCMD[DSControl15765[Num].Y].Data[DSControl15765[Num].X + i] = DSRAM[i];
	  }
	  CotrolVale = SYSXX;
	  ClearRAM((u8*)ATCmd,100);
	  ClearRAM((u8*)DSRAM,2);
	}
    else if (CotrolVale == SYSVIN)
	{
	  if (CheckASCII(&ATCmd[7],17,VINRAM) == ERR0)
	  {
		printf(STR161);
		printf("\r\n");
		CotrolVale = SYSXX;
	    ClearRAM((u8*)ATCmd,100);
		continue;
	  }
	  for (i = 0;i < 3;i++)
	  {
		CANCPTab->CANTXCMD[120].Data[5 + i] = VINRAM[i];
	  }
	  for (i = 0;i < 7;i++)
	  {
		CANCPTab->CANTXCMD[121].Data[1 + i] = VINRAM[i+3];
	  }
	  for (i = 0;i < 7;i++)
	  {
		CANCPTab->CANTXCMD[122].Data[1 + i] = VINRAM[i+10];
	  }
	  printf(STR162);
	  printf((const char*)VINRAM);
	  printf("\r\n");
	  CotrolVale = SYSXX;
	  ClearRAM((u8*)ATCmd,100);
	  ClearRAM((u8*)DTCRAM,21);
	}
    if (IRQVar.CPL == SET && IRQVar.Flag30H == RESET)
	{
	  if (CANCPTab->CANTXCMD[CANCPTab->CANRXCMD[IRQVar.CPS].REQ].Data[0] == 0x10)
	  {
	    IRQVar.NFrameLen = CANCPTab->CANTXCMD[CANCPTab->CANRXCMD[IRQVar.CPS].REQ].Data[1];
	  }
	  CANCPTab->CANTXCMD[CANCPTab->CANRXCMD[IRQVar.CPS].REQ].IDE = CANStype;
	  TransmitMailbox = CAN_Transmit(CAN1,&CANCPTab->CANTXCMD[CANCPTab->CANRXCMD[IRQVar.CPS].REQ]);
      while(CAN_TransmitStatus(CAN1,TransmitMailbox) != CANTXOK);
	  IRQVar.CPL = RESET;
	}
	else if (IRQVar.CPL == SET && IRQVar.Flag30H == SET)
	{
	  if ((IRQVar.NFrameLen+1)%7 > 0)
	  {
	    nframe = (IRQVar.NFrameLen+1)/7 + 1;
	  }
	  else
	  {
	    nframe = (IRQVar.NFrameLen+1)/7;
	  }
	  for (i = 1;i < nframe;i++)
	  {
	    delay_ms(1);
		CANCPTab->CANTXCMD[CANCPTab->CANRXCMD[IRQVar.CPS].REQ+i].IDE = CANStype;
	    TransmitMailbox = CAN_Transmit(CAN1,&CANCPTab->CANTXCMD[CANCPTab->CANRXCMD[IRQVar.CPS].REQ+i]);
        while(CAN_TransmitStatus(CAN1,TransmitMailbox) != CANTXOK);
	  }
	  
	  IRQVar.Flag30H = RESET;
	  IRQVar.CPL = RESET;
    }
  }
}
/************************************************************************
  * @描述:  转换指令故障码
  * @参数: ATCmd dtcram
  * @返回值: ERRORType  1517
  **********************************************************************/

ERRORType SetDTCRAM(__IO u8 *cmd,u8 *dtcram)
{
  u8 i,dtcnum;
  ERRORType err;
  ClearRAM(dtcram,21);
  dtcnum = (cmd[0]-0x30)*10+(cmd[1]-0x30);
  if(dtcnum > 10)
  {
	return ERR0;
  }
  else
  {
    dtcram[0] = dtcnum;
  }
  
  for (i = 0;i < dtcnum;i++)
  {
     if (!strncmp((const char *)&cmd[i*6+3],"P",1))
	 {
	   err = OneDTCNum(&cmd[i*6+4],&dtcram[i*2+1]);
	   if (dtcram[i*2+1]>0x40 || err == ERR1)
	   {
		 return ERR1;
	   }
	 }
	 else if (!strncmp((const char *)&ATCmd[i*6+9],"C",1))
	 {
	   err = OneDTCNum(&cmd[i*6+4],&dtcram[i*2+1]);
	   if (dtcram[i*2+1]>0x40 || err == ERR1)
	   {
		 return ERR1;
	   }
	   dtcram[i*2+1] = dtcram[i*2+1]+0x40;
	 }
	 else if (!strncmp((const char *)&ATCmd[i*6+9],"B",1))
	 {
	   err = OneDTCNum(&cmd[i*6+4],&dtcram[i*2+1]);
	   if (dtcram[i*2+1]>0x40 || err == ERR1)
	   {
		 return ERR1;
	   }
	   dtcram[i*2+1] = dtcram[i*2+1]+0x80;
	 }
	 else if (!strncmp((const char *)&ATCmd[i*6+9],"U",1))
	 {
	   err = OneDTCNum(&cmd[i*6+4],&dtcram[i*2+1]);
	   if (dtcram[i*2+1]>0x40 || err == ERR1)
	   {
		 return ERR1;
	   }
	   dtcram[i*2+1] = dtcram[i*2+1]+0xc0;
	 }
	 else
	 {
	   return ERR1;
	 }
  }
  return TRUE;
}
/************************************************************************
  * @描述:  转换一个指令故障码
  * @参数:  __IO u8 *cmd,u8 *ram
  * @返回值: ERRORType
  **********************************************************************/
ERRORType OneDTCNum(__IO u8 *cmd,u8 *ram)
{
  u8 j,k;
  for (j = 0;j < 2;j++)
  {
    for (k = 0;k < 2;k++)
    {
      if (cmd[k+2*j] >= 0x30 && cmd[k+2*j] <= 0x39)
      {
        if (k == 0)
	    {
	      ram[j] = cmd[j*2]-0x30<<4 & 0xf0;
	    }
	    else
	    {
	      ram[j] |= cmd[j*2+1]-0x30 & 0x0f;
	    }

      }
	  else if (cmd[k+2*j] >= 0x41 && cmd[k+2*j] <= 0x46)
	  {
	    if(k == 0)
	    {
		  ram[j] = cmd[j*2]-0x37<<4 & 0xf0;
	    }
	    else
	    {
		  ram[j] |= cmd[j*2+1]-0x37 & 0x0f;
	    }
	  }
	  else
	  {
	    return ERR1;		   
	  }
    }
  }
  return TRUE;
}
/************************************************************************
  * @描述:  设置故障码命令表
  * @参数:  u8 *dtcram,u8 p,ISOTypeDef sw
  * @返回值: NONE
  **********************************************************************/
void SetDTCTab(u8 *dtcram,u8 p,ISOTypeDef sw)
{
  switch (sw)
  {
    case iso15765: SetISO15765CmdTabRam(dtcram,p);
	break;
	case iso14230: SetISO14230CmdTabRam(dtcram,p);
	break;
	case iso9141:  SetISO9141CmdTabRam(dtcram,p);
	break;
  }
  

}
/************************************************************************
  * @描述:  设置数据流命令表
  * @参数:  __IO u8 *cmd,const DSControlTypeDef *Controltab	 u8 *p
  * @返回值: NONE
  **********************************************************************/
__IO u32 Count = 0;
ERRORType SetDSRAM(__IO u8 *cmd,const DSControlTypeDef *Controltab,u8 *p)
{
  u8 Num,i,*sp;  
  ERRORType err;
  char ch[20] = {0};
  Num = GetDSNumber(&cmd[6],&err);
  if (Controltab[Num].Type == Numeric)
  {
    if (!strncmp((const char *)&cmd[10],"DOWN",4))
	{
	   Count--;
	   if (Controltab[Num].ByteNum == 1)
	   {
	      if (Count == 0xffffffff)
		  {
		    Count = 0xff;
		  }
	   }
	   else
	   {
	     if (Count == 0xffffffff)
		 {
		   Count = 0xffff;
		 }
	   }	   
	}
	else if (!strncmp((const char *)&cmd[10],"UP",2))
	{
	   Count++;
	   if (Controltab[Num].ByteNum == 1)
	   {
	      if (Count == 256)
		  {
		    Count = 0;
		  }
	   }
	   else
	   {
	     if (Count == 65536)
		 {
		   Count = 0;
		 }
	   }
	}
	//没错就是在这里使用的Fake自定义函数，即可做到自动的修改数组，如果收到的内容是Fake的话 可以愉快的玩耍AT+SDS048:Fake这种
	else if(!strncmp((const char *)&cmd[10],"Fake",2)){
			Count = 100;
	   if (Controltab[Num].ByteNum == 1)
	   {
	      if (Count == 256)
		  {
		    Count = 0;
		  }
	   }
	   else
	   {
	     if (Count == 65536)
		 {
		   Count = 0;
		 }
	   }
	
	}
	else
	{
	  return  ERR0;
	}
	for (i = 0;i < Controltab[Num].ByteNum;i++)
	{
	   sp = (u8*)(&Count) + i;
	   p[Controltab[Num].ByteNum - i - 1] = *sp;
	 }
     printf(Controltab[Num].str);
	 //最终输出数据存放在Controltab[Num].Equation0
	 if (Controltab[Num].Format[strlen(Controltab[Num].Format)-1]  == 'f')
   {
	   printf(Controltab[Num].Format,Controltab[Num].Equation0((float)Count));
	 }
	 else
	 {
	   printf(Controltab[Num].Format,(u32)Controltab[Num].Equation0((float)Count));
	 }	 
	 printf(Controltab[Num].unit);
	 printf(", ");
	 printf(STR160);
	 printf(Controltab[Num].range);
     printf("\r\n");	      
   }
   else
   {
     for (i = 0;i < Controltab[Num].ChLen;i++)
	 { 
	   ch[i] = (char)cmd[10+i];
	 }
	 Count = Controltab[Num].Equation1(ch,&err);
	  if (err == ERR0)
	  {
	    return ERR1;
	  }
	 for (i = 0;i < Controltab[Num].ByteNum;i++)
	 {
	   sp = (u8*)(&Count) + i;
	   p[Controltab[Num].ByteNum - i - 1] = *sp;
	 }
	  printf(Controltab[Num].str);
	  printf((const char*)&cmd[10]);
	  printf(",");
	  printf(STR160);
	  printf(DSControl15765[Num].range);
	  printf("\r\n");
   }

   return TRUE;
}
/************************************************************************
  * @描述:  获取编号
  * @参数:  __IO u8 *cmd,ERRORType *err
  * @返回值: u8
  **********************************************************************/
u8 GetDSNumber(__IO u8 *cmd,ERRORType *err)
{
  if (cmd[0] - 0x30 > 9 || cmd[1] - 0x30 > 9 || cmd[2] - 0x30 > 9 || cmd[0] - 0x30 < 0 || cmd[1] - 0x30 < 0 || cmd[2] - 0x30 < 0)
  {
    *err = ERR0; 
    return 0xff; 
  }
  else
  {
    *err = TRUE;
    return (cmd[0]-0x30)*100 + (cmd[1]-0x30)*10 + cmd[2]-0x30;
  }  
}

	

/************************************************************************
  * @描述:  计算公式 data
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float gzmsl(float data)
{
  return (float)((u8)data&0x7F);
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 gzdzt(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"ON",2))
  {
    *err = TRUE;
    return 0x80;
  }
  else if (!strncmp((const char *)data,"OF",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}

/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 zcshjc(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"支",2))
  {
    *err = TRUE;
    return 0x01;
  }
  else if (!strncmp((const char *)data,"不",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 zcryjc(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"支",2))
  {
    *err = TRUE;
    return 0x02;
  }
  else if (!strncmp((const char *)data,"不",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 zcbjjc(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"支",2))
  {
    *err = TRUE;
    return 0x04;
  }
  else if (!strncmp((const char *)data,"不",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 shjcjx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"未",2))
  {
    *err = TRUE;
    return 0x10;
  }
  else if (!strncmp((const char *)data,"OK",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 ryjcjx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"未",2))
  {
    *err = TRUE;
    return 0x20;
  }
  else if (!strncmp((const char *)data,"OK",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 bjjcjx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"未",2))
  {
    *err = TRUE;
    return 0x40;
  }
  else if (!strncmp((const char *)data,"OK",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 zczyjc(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"支",2))
  {
    *err = TRUE;
    return 0x08;
  }
  else if (!strncmp((const char *)data,"不",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 zcfqjc(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"支",2))
  {
    *err = TRUE;
    return 0x20;
  }
  else if (!strncmp((const char *)data,"不",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 zcpmjc(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"支",2))
  {
    *err = TRUE;
    return 0x40;
  }
  else if (!strncmp((const char *)data,"不",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 zcegjc(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"支",2))
  {
    *err = TRUE;
    return 0x80;
  }
  else if (!strncmp((const char *)data,"不",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32  nmhcjx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"未",2))
  {
    *err = TRUE;
    return 0x01;
  }
  else if (!strncmp((const char *)data,"OK",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32  dyhcjx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"未",2))
  {
    *err = TRUE;
    return 0x02;
  }
  else if (!strncmp((const char *)data,"OK",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32  zyyljx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"未",2))
  {
    *err = TRUE;
    return 0x08;
  }
  else if (!strncmp((const char *)data,"OK",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32  zcegjx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"未",2))
  {
    *err = TRUE;
    return 0x80;
  }
  else if (!strncmp((const char *)data,"OK",2))
  {
    *err = TRUE;
    return 0;
  }
  else
  {
    *err = ERR0;
    return 0;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 swpcbu(char *data,ERRORType *err)
{
  u32 dtcram,da;
  if (!strncmp((const char *)data,"P",1))
  {
    *err = OneDTCNum((__IO u8*)data+1,(u8*)&dtcram);
	if ((u8)dtcram > 0x40)
	{
	  *err = ERR0;
	}
  }
  else if (!strncmp((const char *)data,"C",1))
  {
    *err = OneDTCNum((__IO u8*)data+1,(u8*)&dtcram);
	if ((u8)dtcram > 0x40)
	{
	  *err = ERR0;
	}
	dtcram+=0x40;
  }
  else if (!strncmp((const char *)data,"B",1))
  {
    *err = OneDTCNum((__IO u8*)data+1,(u8*)&dtcram);
	if ((u8)dtcram > 0x40)
	{
	  *err = ERR0;
	}
	dtcram+=0x80;
  }
  else if (!strncmp((const char *)data,"U",1))
  {
    *err = OneDTCNum((__IO u8*)data+1,(u8*)&dtcram);
	if ((u8)dtcram > 0x40)
	{
	  *err = ERR0;
	}
	dtcram+=0xc0;
  }
  else
  {
    *err = ERR0;
  }
  if (*err ==  ERR1)
  {
    *err = ERR0;
  }
  da = dtcram & 0x00ff;
  da <<= 8;
  dtcram >>= 8;
  dtcram = dtcram | da;
  return dtcram;
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 ryxizt(char *data,ERRORType *err)  
{
  if (!strncmp((const char *)data,"OL-Drive",8))
  {
     *err = TRUE;
	 return 0x04;   
  }
  else if  (!strncmp((const char *)data,"OL-Fault",8))
  {
     *err = TRUE;
	 return 0x08;   
  }
  else if  (!strncmp((const char *)data,"CL-Fault",8))
  {
     *err = TRUE;
	 return 0x10;   
  }
  else if  (!strncmp((const char *)data,"OL",2))
  {
     *err = TRUE;
	 return 0x01;   
  }
  else if  (!strncmp((const char *)data,"CL",2))
  {
     *err = TRUE;
	 return 0x02;   
  }
  else if  (!strncmp((const char *)data,"--",2))
  {
     *err = TRUE;
	 return 0x00;   
  }
  else
  {
	 *err = ERR0;
	 return 0x00;
  }
}

/************************************************************************
  * @描述:  计算公式 x*100/255
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float fhjsz(float data)
{
  return data*100/255;
}
/************************************************************************
  * @描述:  计算公式 x-40
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float flqwd(float data)
{
  return data-40.0f;
}

/************************************************************************
  * @描述:  计算公式
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float dqryxz(float data)
{
  return data*100.0f/128.0f-100.0f;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float ygyl(float data)
{
  return  data*3;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float fdjzs(float data)
{
  return  data/4;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float qgjdyl(float data)
{
  return  data;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float qgdhtqj(float data)
{
  return  data/2-64;

}
/************************************************************************
  * @描述:  计算公式
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float zlkqll(float data)
{
  return  data/100;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float ycgqdy(float data)
{
  return  data*1.28/255;  
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float fdjqdsj(float data)
{
  return  data;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float fdjqdsj1(float data)
{
  return  data*10;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float qgzkyl(float data)
{
  return  data*5177.27/65535;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float ygyl1(float data)
{
  return  data*10;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float dlb(float data)
{
  return  data*2/65535;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float ryzqyl(float data)
{
  if (data < 0x80)
  {
    return data*8128/127;
  }
  else
  {
    return (256-data)*-64;
  }
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float ycgqdl(float data)
{
    return data-128;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float chjwd(float data)
{
  return data/10-40;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float ycgqdy1(float data)
{
  return data*8/65535;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float kzmkdy(float data)
{
  return data*65.53/65535;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float jdfhz(float data)
{
  return data*25700/65535;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float yyzqyl(float data)
{
  return data/200;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float zqpfxtyl(float data)
{
  if (data < 0x8000)
  {
    return data;
  }
  else
  {
    return data-65536;
  }
  
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float pyzs(float data)  
{
  return data*511.99/65535;
} 
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float fdjgyl(float data) 
{
  return data*3276.75/65535;
}
/************************************************************************
  * @描述:  计算公式 
  * @参数:  float data
  * @返回值: float
  **********************************************************************/
float clpfyq(float data) 
{
  return data-125;
}
/************************************************************************
  * @描述:  计算公式                                                     
  * @参数:  char *data,ERRORType *err                                    
  * @返回值: u32                                                         
  **********************************************************************/
u32 zleckqps(char *data,ERRORType *err)                                    
{                                                                        
  if (!strncmp((const char *)data,"DIAG",4))                         
  {                                                                      
    *err = TRUE;                                                        
	  return 0x08;                                                          
  }                                                                      
  else if  (!strncmp((const char *)data,"OFF",3))                   
  {                                                                      
    *err = TRUE;                                                        
	  return 0x04;                                                          
  }                                                                      
  else if  (!strncmp((const char *)data,"DNS",3))                   
  {                                                                      
    *err = TRUE;                                                        
	  return 0x02;                                                          
  }                                                                      
  else if  (!strncmp((const char *)data,"UPS",3))                         
  {                                                                      
    *err = TRUE;                                                        
	  return 0x01;                                                          
  }                                                                      
  else if  (!strncmp((const char *)data,"--",2))                         
  {                                                                      
    *err = TRUE;                                                        
	  return 0x00;                                                          
  }                                                                      
  else                                                                   
  {                                                                      
	 *err = ERR0;                                                          
	 return 0x00;                                                          
  }                                                                      
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 ycgqwz(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"B1-S1",5))
  {
    *err = TRUE;
	  return 0x01;
  }                                                                      
  else if  (!strncmp((const char *)data,"B1-S2",5))                   
  {                                                                      
    *err = TRUE;                                                        
	  return 0x02;                                                          
  }                                                                      
  else if  (!strncmp((const char *)data,"B1-S3",5))
  {
    *err = TRUE;
	  return 0x04;
  }
  else if  (!strncmp((const char *)data,"B1-S4",5))
  {
    *err = TRUE;
	  return 0x08;
  }
  else if  (!strncmp((const char *)data,"B2-S1",5))
  {
    *err = TRUE;
	  return 0x10;
  }
  else if  (!strncmp((const char *)data,"B2-S2",5))
  {
    *err = TRUE;
	  return 0x20;
  }
  else if  (!strncmp((const char *)data,"B2-S3",5))
  {
    *err = TRUE;
	  return 0x40;
  }
  else if  (!strncmp((const char *)data,"B2-S4",5))
  {
    *err = TRUE;
	  return 0x80;
  }  
  else if  (!strncmp((const char *)data,"--",2))
  {
    *err = TRUE;
	  return 0x00;
  }  
  else
  {
	 *err = ERR0;
	 return 0x00;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 obdqq(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"EOBD OBD AND OBDII",18))
  {
    *err = TRUE;
	  return 0x09;
  }                                                                      
  else if  (!strncmp((const char *)data,"OBD and OBD II",14))
  {
    *err = TRUE;
	  return 0x03;
  }
  else if  (!strncmp((const char *)data,"EOBD AND OBDII",14))
  {
    *err = TRUE;
	  return 0x07;
  }
  else if  (!strncmp((const char *)data,"JOBD AND OBDII",14))
  {
    *err = TRUE;
	  return 0x0B;
  }
  else if  (!strncmp((const char *)data,"JOBD AND EOBD",13))
  {
    *err = TRUE;
	  return 0x0C;
  }
  else if  (!strncmp((const char *)data,"EOBD AND OBD",12))
  {
    *err = TRUE;
	  return 0x08;
  }
  else if  (!strncmp((const char *)data,"NO OBD",6))
  {
    *err = TRUE;
	  return 0x05;
  }  
  else if  (!strncmp((const char *)data,"OBDI",4))
  {
    *err = TRUE;
	  return 0x04;
  }    
  else if  (!strncmp((const char *)data,"EOBD",4))
  {
    *err = TRUE;
	  return 0x06;
  }      
  else if  (!strncmp((const char *)data,"JOBD",4))                   
  {                                                                      
    *err = TRUE;                                                        
	  return 0x0A;                                                          
  }                                                                      
  else if  (!strncmp((const char *)data,"OBD",3))
  {
    *err = TRUE;
	  return 0x02;
  }        
  else
  {
	 *err = ERR0;
	 return 0x00;
  }
}

  
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 ycgq1(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"B1-S1",5))
  {
    *err = TRUE;
	  return 0x01;
  }                                                                      
  else if  (!strncmp((const char *)data,"B1-S2",5))                   
  {                                                                      
    *err = TRUE;                                                        
	  return 0x02;                                                          
  }                                                                      
  else if  (!strncmp((const char *)data,"B2-S1",5))
  {
    *err = TRUE;
	  return 0x04;
  }
  else if  (!strncmp((const char *)data,"B2-S2",5))
  {
    *err = TRUE;
	  return 0x08;
  }
  else if  (!strncmp((const char *)data,"B3-S1",5))
  {
    *err = TRUE;
	  return 0x10;
  }
  else if  (!strncmp((const char *)data,"B3-S2",5))
  {
    *err = TRUE;
	  return 0x20;
  }
  else if  (!strncmp((const char *)data,"B4-S1",5))
  {
    *err = TRUE;
	  return 0x40;
  }
  else if  (!strncmp((const char *)data,"B4-S2",5))
  {
    *err = TRUE;
	  return 0x80;
  }  
  else if  (!strncmp((const char *)data,"--",2))
  {
    *err = TRUE;
	  return 0x00;
  }  
  else
  {
	 *err = ERR0;
	 return 0x00;
  }
}  
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 ptozt(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"OFF",2))
  {
    *err = TRUE;
	  return 0x00;
  }                                                                      
  else if  (!strncmp((const char *)data,"ON",2))                   
  {                                                                      
    *err = TRUE;                                                        
	  return 0x01;                                                          
  }                                                                      
  else
  {
	 *err = ERR0;
	 return 0x00;
  }
}
/************************************************************************
  * @描述:  计算公式
  * @参数:  char *data,ERRORType *err
  * @返回值: u32
  **********************************************************************/
u32 rllx(char *data,ERRORType *err)
{
  if (!strncmp((const char *)data,"BI_METH",7))
  {
    *err = TRUE;
	  return 0x0A;
  }
  else if  (!strncmp((const char *)data,"BI_PROP",7))
  {
     *err = TRUE;
	 return 0x0E;
  }
  else if  (!strncmp((const char *)data,"BI_ELEC",7))
  {
     *err = TRUE;
	 return 0x0F;
  }
  else if  (!strncmp((const char *)data,"ISO/SAE",7))
  {
     *err = TRUE;
	 return 0xFF;
  }
  else if  (!strncmp((const char *)data,"BI_GAS",6))
  {
     *err = TRUE;
	 return 0x09;
  }
  else if  (!strncmp((const char *)data,"BI_ETH",6))
  {
     *err = TRUE;
	 return 0x0B;
  }
  else if  (!strncmp((const char *)data,"BI_LPG",6))
  {
     *err = TRUE;
	 return 0x0C;
  }
  else if  (!strncmp((const char *)data,"BI_CNG",6))
  {
     *err = TRUE;
	 return 0x0D;
  }
  else if  (!strncmp((const char *)data,"METH",4))
  {
     *err = TRUE;
	 return 0x02;
  }
  else if  (!strncmp((const char *)data,"PROP",4))
  {
     *err = TRUE;
	 return 0x07;
  }
  else if  (!strncmp((const char *)data,"ELEC",4))
  {
     *err = TRUE;
	 return 0x08;
  }
  else if  (!strncmp((const char *)data,"GAS",3))
  {
     *err = TRUE;
	 return 0x01;
  }
  else if  (!strncmp((const char *)data,"ETH",3))
  {
     *err = TRUE;
	 return 0x03;
  }
  else if  (!strncmp((const char *)data,"DSL",3))
  {
     *err = TRUE;
	 return 0x04;
  }
  else if  (!strncmp((const char *)data,"LPG",3))
  {
     *err = TRUE;
	 return 0x05;
  }
  else if  (!strncmp((const char *)data,"CNG",3))
  {
     *err = TRUE;
	 return 0x06;
  }
  else
  {
	 *err = ERR0;
	 return 0x00;
  }
}
/************************************************************************
  * @描述:  检查ASCII输入是否正确
  * @参数:  __IO u8 *p,u8 n,u8 *ram
  * @返回值: ERRORType
  **********************************************************************/
ERRORType CheckASCII(__IO u8 *p,u8 n,u8 *ram)
{
  u8 i;
  for (i = 0;i < n;i++)
  {
	if (p[i] < 0x30 || (p[i] > 0x39 && p[i] < 0x41) || p[i] > 0x5A)
	{
	  return ERR0;
	}
	ram[i] = p[i];
  }
  return TRUE;
}
/************************************************************************
  * @描述:  设置ISO15765故障码命令
  * @参数:  u8 *dtcram,u8 p
  * @返回值: NONE
  **********************************************************************/
void SetISO15765CmdTabRam(u8 *dtcram,u8 p)
{
  u8 i,j,k,c;
  for (j = 0;j < 4;j++)
  {
    for (i = 0;i < 3;i++)
    {
      IRQVar.CANCPT->CANTXCMD[p+j].Data[2+i*2] = 0;   
	  IRQVar.CANCPT->CANTXCMD[p+j].Data[3+i*2] = 0;  
    }
  }
  if (dtcram[0] < 3)
  {
    IRQVar.CANCPT->CANTXCMD[p].Data[0] = dtcram[0]*2+2;
    IRQVar.CANCPT->CANTXCMD[p].Data[2] = dtcram[0];
	for(i = 0;i < dtcram[0];i++)
	{
	  IRQVar.CANCPT->CANTXCMD[p].Data[3+i*2] = dtcram[1+i*2];
	  IRQVar.CANCPT->CANTXCMD[p].Data[4+i*2] = dtcram[2+i*2];  
	}
  }
  else
  {
    IRQVar.CANCPT->CANTXCMD[p].Data[0] = 0x10;
	IRQVar.CANCPT->CANTXCMD[p].Data[1] = dtcram[0]*2+2;
	IRQVar.CANCPT->CANTXCMD[p].Data[2] = 0x43;
	IRQVar.CANCPT->CANTXCMD[p].Data[3] = dtcram[0];
	for(i = 0;i < 2;i++)
	{
	  IRQVar.CANCPT->CANTXCMD[p].Data[4+i*2] = dtcram[1+i*2];
	  IRQVar.CANCPT->CANTXCMD[p].Data[5+i*2] = dtcram[2+i*2];  
	}  
	if ((dtcram[0]-2)*2%7 == 0 )
	{
	  k = 0;
	}
	else
	{
	  k = 1;
	}
	c = 0;
	for (i = 0;i < (dtcram[0]-2)*2/7+k;i++)
	{
	  for (j = 0;j < 7;j++)
	  {
	    IRQVar.CANCPT->CANTXCMD[p+1+i].Data[j+1] = dtcram[5+c];
		if(++c >= (dtcram[0]-2)*2)
		{
		  break;
		}
	  }
	}  
  }
}


