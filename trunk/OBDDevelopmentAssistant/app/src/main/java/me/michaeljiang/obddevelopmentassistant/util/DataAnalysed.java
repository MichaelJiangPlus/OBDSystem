package me.michaeljiang.obddevelopmentassistant.util;

/**
 * Created by Michael on 2016/7/25 0025.
 */
public class DataAnalysed {
    public byte[][] analysisDate(String date){
        String a=date.split("\r")[1];
        byte[] bytes=a.getBytes();
        byte[][] result=new byte[7][2];
        char[][] temp=new char[7][2];
        int k=0;
        char b,c;
        int i=0;
        for(;i<bytes.length;i++){
            if(bytes[i]=='4'&&bytes[i+1]=='1'){
                for(int j=0;j<7;j++,k++){
                    result[j][0]=bytes[k++];
                    result[j][1]=bytes[k++];
                    temp[j][0]=(char) result[j][0];
                    temp[j][1]=(char) result[j][1];
                }
                break;
            }
        }
        return result;
    }

    public  int hexTodec(byte[] result){
        int sum=0;
        for(int i=0;i<result.length;i++){
            if(result[i]>=65){
                sum=sum*16+result[i]-55;
            }
            else{
                sum=sum*16+result[i]-48;
            }
        }
        return sum;
    }
}
