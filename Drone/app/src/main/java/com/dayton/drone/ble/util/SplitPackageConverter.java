package com.dayton.drone.ble.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by med on 16/5/20.
 */
public class SplitPackageConverter {

    public static byte[][] rawData2Packages(byte[] rawData,int MTU)
    {
        if(rawData.length>=MTU)
        {
            int packetsNum = rawData.length/(Constants.MTU-1);
            if(rawData.length%(Constants.MTU-1)!=0)
            {
                packetsNum = packetsNum + 1;
            }

            byte [][] packages = new byte[packetsNum][Constants.MTU];
            int totalRead = 0;

            for(int i=0;i<packetsNum;i++)
            {
                //set packet frame SEQ, 1 byte
                if(i==packetsNum-1) {
                    packages[i][0] = (byte) (i|0x80);
                }
                else {
                    packages[i][0] = (byte) i;
                }
                //set packet content, MTU-1 bytes
                for(int j=1;j<Constants.MTU;j++)
                {
                    packages[i][j] = rawData[totalRead];
                    totalRead = totalRead + 1;
                    if(totalRead==rawData.length)
                    {
                        //fill last packet remained bytes with 0
                        j = j + 1;
                        while(j<Constants.MTU)
                        {
                            packages[i][j] = 0;
                            j = j + 1;
                        }
                        break;
                    }
                }
            }
            return packages;
        }
        else
        {
            byte[] onepackage = new byte[rawData.length+1];
            onepackage[0] = (byte) 0x80;
            System.arraycopy(rawData,0,onepackage,1,rawData.length);
            return new byte[][] {onepackage};
        }
    }
}
