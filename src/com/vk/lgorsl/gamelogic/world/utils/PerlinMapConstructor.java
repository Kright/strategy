package com.vk.lgorsl.gamelogic.world.utils;

import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.LandType;
import com.vk.lgorsl.gamelogic.world.Map;

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


    public PerlinMapConstructor(int width, int height, List<LandType> types){
        this.width=width;
        this.height=height;
        this.numberOfOctavs=5;
        this.types= types;
        for(int i=0;  i<width; i++)
            for(int j=0; j<height; j++){
                average+=perlinNoise(i,j);
            }
        average/=(width*height);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Cell getCell(int x, int y){
        LandType type=types.get(0); // Должно быть поле
        if(perlinNoise(x,y)<average){// 0.59
                type=types.get(1);
        }
        Cell c= new Cell(x, y, type, type.nextLayer());
       return c;
    }


    private double noise(int x, int y){ // магический постоянный по своим переменным шум
        int n = x + y * 59;
        n = (n<<13) ^ n;
        return ( 2.0 - ( (n * (n * n * 17107 + 809909) + 1370092415) & 0x7fffffff) / 1073741824.0);
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
        double f=0.3;
        for(int i=0; i<numberOfOctavs-1; i++){
            total+=(interpolateNoise((x)*f,y*f))*a ;
            f*=1.5;
            a*=0.8;
        }
        return total;
    }

}
