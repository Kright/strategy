package com.vk.lgorsl.gamelogic.world.utils;

import com.vk.lgorsl.gamelogic.world.*;
import com.vk.lgorsl.utils.CustomRandom;
import com.vk.lgorsl.utils.LinearCongruentialGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael-PC on 15.05.14.
 * Создает леса с помощью шума Перлина
 * Создает горы и холмы в виде фрактальных цепочек
 */
public class PerlinMapConstructor implements Map.MapConstructor {

    private int width;
    private int height;
    private List<LandType> types;

    private int numberOfOctavs; // количество гармник у шума Перлина, чем больше, тем более зашумленный, не сбивающийся в кучки лес
    private double average; // среднее значение шума на всей карте
    private int parameter; // параметр шума, задается с помощью псевдослучайно функции из интерфейса CustomRandom


    private final static int[][] k = {{1,0},{1,1}, {0,1}, {-1,0}, {-1,-1} , {0, -1} };

    private int[][] table; // по сути карта высот. 0 - поле, 1 - лес, 2 - холм, 3 - гора, 10 - деревня

    private int pVillages;// вероятность появления деревни в какой-то клетке.
    private int pMountains; // вероятность появления цепочки гор из какой-то клетки
    private int pHills; // веротяность поялвения цепочки холмов из какой-то клетки
    private int pRivers; // вероятнось появления реки из холма или горы

    /**
     *
     * @param width
     * @param height
     * @param types
     * @param random
     */

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
        pVillages=50;
        pMountains=500;
        pHills=100;

        table=new int[width+height/2][height+1];

        fillTable(random);
    }


    @Override
    public int getWidth(){
        return width;
    }

    @Override
    public int getHeight(){
        return height;
    }

    /**
     *
     * @param x
     * @param y
     * @return возвращает тип клетки
     */
    @Override
    public Cell getCell(int x, int y){
        LandType type=types.get(0); // Должно быть поле

        if(table[x][y]==1){  // лес
            type=types.get(1);
        }

        if(table[x][y]==3){  // гора
            type=types.get(3);
        }
        if(table[x][y]==2){ // холм
            type=types.get(2);
        }
        if(table[x][y]==10){  // деревня
            type=types.get(0);
        }

        Cell c= new Cell(x, y, type, type.nextLayer());

        if(table[x][y] ==10){
            c.needVillage=true;
        }
       return c;
    }

    /**
     *
     * @param x - целое число
     * @param y - целое число
     * @return вещественное чилсо из промежутка [0;2]. Магический метод постоянный по своим переменный и зависищий только от параметра, задаваемого при создании контструктора
     */
    private double noise(int x, int y){ // магический постоянный по своим переменным шум
        int n = x + y * 59;
        n = (n<<13) ^ n;
        return ( 2.0 - ( (n * (n * n * /*17107*/parameter + 809909) + 1370092415) & 0x7fffffff) / 1073741824.0);
        //return ( 2.0 - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
    }

    /**
     * интерполирует два значения функции v1 и v2 при смещении x
     * @param v1 первое значение
     * @param v2 второе значение
     * @param x смещение
     * @return интерполирует два значения функции v1 и v2 при смещении x
     */
    private double interpolate(double v1, double v2, double x){
        double ft = x * 3.1415927;
        double f1 = (1 - Math.cos(ft)) / 2;
        return v1*(1-f1)+v2*f1;
    }

    /**
     * интерполируют двумерный шум по трем направлениям
     * @param x
     * @param y
     * @return
     */
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

    /**
     * сглаживание шума по трем направлениям- за счет гексогональной решетки
     * @param x
     * @param y
     * @return
     */
    private double smoothNoise(int x, int y){

        double corners = (noise(x+1, y-1)+noise(x-1, y+1)+noise(x+2,y+2)+noise(x-2,y-2)+
                noise(x-2,y)+ noise(x+2,y)+noise(x,y-2)+noise(x,y+2)+
                noise(x+1,y+2)+noise(x-1,y+2)+noise(x+1,y-2)+noise(x-1,y-2)+
                noise(x+2,y+1)+noise(x-2,y+1)+noise(x+2,y-1)+noise(x-2,y-1)) /32 ;
    double sides   = ( noise(x-1, y)  +noise(x+1, y)  +noise(x, y-1)  +noise(x, y+1)+ noise(x-1, y-1)+noise(x+1,y+1) ) /16  ;
    double center  =  noise(x, y) / 8;
            return corners + sides + center;
    }

    /**
     *
     * @param x координата клетки
     * @param y координата клетки
     * @return шум Перлина от [0;2]
     */

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

    /**
     * Заполняет таблицу высот
     * 1)Горы
     * 2)Холмы
     * 3)Реки
     * 4)Леса
     * 5)Деревни
     * @param random
     */

    private void fillTable(CustomRandom random){
        makeMountains(random);
        makeHills(random);
        makeRivers(random);
        makeForests(random);
        makeVillages(random);
    }


    /**
     * Заполняет таблицу там где она не заполнена лесом по шуму Перлина
     * @param random
     */
    private void makeForests(CustomRandom random){
        double p;
        for(int x=0; x<width+height/2; x++ )
            for(int y=0; y<height; y++){
                p=perlinNoise(x,y);
                if((p<average-0.01)&&(table[x][y]==0)){// 0.59
                    table[x][y]=1; // лес
                }
            }
    }

    /**
     * Заполняет таблицу цепочками гор
     * @param random
     */

    private void makeMountains(CustomRandom random){
        for(int x=0; x<width+height/2; x++ )
            for(int y=0; y<height; y++)
                if((random.get(pMountains)==0)&& table[x][y]==0){
                    makeChain(x, y, random.get(10) + 3, 3, random);
                }
    }

    /**
     * Заполняет таблицу цепочками холмов
     * @param random
     */
    public void makeHills(CustomRandom random){
        for(int x=0; x<width+height/2; x++ )
            for(int y=0; y<height; y++)
                if((table[x][y]==3 && random.get(2)==0)){
                    for(int p=0; p<6; p++){
                        if(checkXY(x,y,p)&&(table[x+k[p][0]][y+k[p][1]]==0)&&random.get(6)==0){
                            table[x+k[p][0]][y+k[p][1]]=1;
                        }
                    }
                }else{
                if(random.get(pHills)==0 && table[x][y]==0){
                    makeChain(x,y,random.get(5)+1,2,random);
                }

                }
    }

    /**
     * Там где вокруг есть поля или холмы, заполняет деревнями
     * @param random
     */
    public void makeVillages(CustomRandom random){
        int s;
        boolean near;
        boolean upgrades;
        for(int x=0; x<width+height/2; x++ )
            for(int y=0; y<height; y++){
                if(table[x][y]==0){
                    s=0;
                    near=false;
                    upgrades=false;
                    for(int p=0; p<6; p++){
                        if(checkXY(x,y,p)){
                            s+=table[x+k[p][0]][y+k[p][1]];
                            if (table[x+k[p][0]][y+k[p][1]]==10){
                                near=true;
                            }
                            if(table[x+k[p][0]][y+k[p][1]]==0 || table[x+k[p][0]][y+k[p][1]]==2)
                                upgrades=true;
                        }

                    }
                    if( !near &&random.get(pVillages+s)==0 && upgrades){
                        table[x][y]=10;
                    }
                }
            }
    }

    /**
     * Создает реки
     * @param random
     */

    public void makeRivers(CustomRandom random){

    }

    /**
     * Создает цепочку определнного вида
     * @param x начальная координата клетки
     * @param y начальная координата клетки
     * @param length миниммальная дляина цепочки
     * @param kind вид местности цепочки
     * @param random
     */

    private void makeChain(int x, int y, int length, int kind,  CustomRandom random ){
        int p=0;
        table[x][y]=kind;
        if(kind==3){
            makeChain(x,y, random.get(5)+1, 2, random);
        }
        int count=0;
        while(random.get(length)!=0 || count<length ){
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
                if(checkXY(x,y,p1) && kind==3){ // утолщаем горы
                    table[x+k[p1][0]][y+k[p1][1]]=kind;
                }
            }
        }
        if(kind==3)
            makeChain(x,y, random.get(5)+1, 1, random);

    }

    /**
     * Проверяем выползли ли за край карты при определнном смещении
     * @param x
     * @param y
     * @param p параметр смещения от 0 до 5
     * @return
     */
    public boolean checkXY(int x, int y, int p){
        return !(x+k[p][0]<0 || y+k[p][1]<0 || x+k[p][0]>=width+height/2 || y+k[p][1]>=height);

    }
}




