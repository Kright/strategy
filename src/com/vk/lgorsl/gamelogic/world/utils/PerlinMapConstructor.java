package com.vk.lgorsl.gamelogic.world.utils;

import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.LandType;
import com.vk.lgorsl.gamelogic.world.Map;
import com.vk.lgorsl.utils.CustomRandom;
import com.vk.lgorsl.utils.LinearCongruentialGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael-PC on 15.05.14.
 */
public class PerlinMapConstructor implements Map.MapConstructor {

    private int width;
    private int height;
    private int numberOfOctavs;
    private List<LandType> types;
    private double average;
    private int parameter;
    private int[][] table;
    private final static int[][] k = {{1,0},{1,1}, {0,1}, {-1,0}, {-1,-1} , {0, -1} };

    public PerlinMapConstructor(int width, int height, List<LandType> types, CustomRandom random){
        this.width=width;
        this.height=height;
        this.numberOfOctavs=5;
        this.types= types;
        parameter=17000+random.get(4000);//         random.get(4000);
        for(int i=0;  i<width+height/2; i++)
            for(int j=0; j<height; j++){
                average+=perlinNoise(i,j);
            }
        average/=((width+height/2)*height);
        table=new int[width+height/2][height+1];
        fillTable(random);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Cell getCell(int x, int y){
        LandType type=types.get(0); // Должно быть поле
        double p=perlinNoise(x,y);
        if(p<average-0.01){// 0.59
                type=types.get(1); // лес
        }
        if(table[x][y]==2){
            type=types.get(3);
        }
        if(table[x][y]==1){
            type=types.get(2);
        }

        Cell c= new Cell(x, y, type, type.nextLayer());
       return c;
    }


    private double noise(int x, int y){ // магический постоянный по своим переменным шум
        int n = x + y * 59;
        n = (n<<13) ^ n;
        return ( 2.0 - ( (n * (n * n * /*17107*/parameter + 809909) + 1370092415) & 0x7fffffff) / 1073741824.0);
        //return ( 2.0 - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
    }

    private double interpolate(double v1, double v2, double x){
        double ft = x * 3.1415927;
        double f1 = (1 - Math.cos(ft)) / 2;
        return v1*(1-f1)+v2*f1;
    }

    private double interpolateNoise(double x, double y){
        int xInt = (int)x;
        double xF = x - xInt;
        int yInt = (int)y;
        double yF = x - yInt;
        double v0 = smoothNoise(xInt, yInt);
        double v1 = smoothNoise(xInt + 1, yInt);
        double v2 = smoothNoise(xInt + 1, yInt + 1);
        double v3 = smoothNoise(xInt, yInt+1);


        v1 = interpolate(v0 , v1 , xF);
        v3 = interpolate(v3, v0,  yF);
        v2 = interpolate(v2,v0, xF+yF);

        v3=interpolate(v3,v2,xF);
        v1=interpolate(v2,v1,yF);

        return interpolate(v1, v3, xF+yF);
    }

    private double smoothNoise(int x, int y){

        double corners = (noise(x+1, y-1)+noise(x-1, y+1)+noise(x+2,y+2)+noise(x-2,y-2)+
                noise(x-2,y)+ noise(x+2,y)+noise(x,y-2)+noise(x,y+2)+
                noise(x+1,y+2)+noise(x-1,y+2)+noise(x+1,y-2)+noise(x-1,y-2)+
                noise(x+2,y+1)+noise(x-2,y+1)+noise(x+2,y-1)+noise(x-2,y-1)) /32 ;
    double sides   = ( noise(x-1, y)  +noise(x+1, y)  +noise(x, y-1)  +noise(x, y+1)+ noise(x-1, y-1)+noise(x+1,y+1) ) /16  ;
    double center  =  noise(x, y) / 8;
            return corners + sides + center;
    }

    private double perlinNoise(int x, int y){
        double total=0;
        double a0=0.2;
        double a=a0;
        double f=1.5;
        for(int i=0; i<numberOfOctavs-1; i++){
            total+=(interpolateNoise((x)*f,y*f))*a ;
            f*=1.5;
            a*=0.8;
        }
        return total;
    }

    private void fillTable(CustomRandom random){
        makeMountains(random);
        makeHills(random);
        makeRivers(random);
        makeVillages(random);
    }

    private void makeMountains(CustomRandom random){
        for(int x=0; x<width+height/2; x++ )
            for(int y=0; y<height; y++)
                if((random.get(500)==0)&& table[x][y]==0){
                    makeChain(x, y, random.get(10) + 3, 2, random);
                }
    }
     private void makeChain(int x, int y, int length, int kind,  CustomRandom random ){
         int p=0;
         table[x][y]=kind;
         if(kind==2){
             makeChain(x,y, random.get(3)+1, 1, random);
         }
         int count=0;
         while(random.get(length)!=0 || count<10 ){
             count++;
             int i=0;
             while( (table[x][y]!=0)&& random.get(10)!=0 ){
                 if(i!=0){
                     x-=k[p][0];
                     y-=k[p][1];
                 }
                 i++;
                 p+=-1+random.get(3);
                 if(p<0){
                     p=5;
                 }
                 if(p>5){
                     p=0;
                 }
                 if(checkXY(x,y,p)){
                     x+=k[p][0];
                     y+=k[p][1];
                 }else{
                     i=0;
                 }
             }
             table[x][y]=kind;
             if(random.get(3)==0){
             int p1=(p+4)%6;
             if(checkXY(x,y,p1) && kind==2){ // утолщаем горы
                 table[x+k[p1][0]][y+k[p1][1]]=kind;
             }
             }
         }
           if(kind==2)
               makeChain(x,y, random.get(3)+1, 1, random);

     }

    public void makeHills(CustomRandom random){
        for(int x=0; x<width+height/2; x++ )
            for(int y=0; y<height; y++)
                if((table[x][y]==2 && random.get(2)==0)){
                    for(int p=0; p<6; p++){
                        if(checkXY(x,y,p)&&(table[x+k[p][0]][y+k[p][1]]==0)&&random.get(6)==0){
                            table[x+k[p][0]][y+k[p][1]]=1;
                        }
                    }
                }else{
                if(random.get(200)==0 && table[x][y]==0){
                    makeChain(x,y,random.get(4)+1,1,random);
                }

                }
    }

    public void makeRivers(CustomRandom random){

    }

    public void makeVillages(CustomRandom random){

    }

    /*
       проверяет клетка в карте или нет
     */
    public boolean checkXY(int x, int y, int p){
        return !(x+k[p][0]<0 || y+k[p][1]<0 || x+k[p][0]>=width+height/2 || y+k[p][1]>=height);

    }
}




