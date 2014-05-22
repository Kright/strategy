package com.vk.lgorsl.gamelogic.world.utils;
import com.vk.lgorsl.GUI.BorderLine;
import com.vk.lgorsl.gamelogic.AI.PlayerAI;
import com.vk.lgorsl.gamelogic.GameSession;
import com.vk.lgorsl.gamelogic.userinput.Gamer;
import com.vk.lgorsl.gamelogic.world.*;
import com.vk.lgorsl.utils.CustomRandom;
import com.vk.lgorsl.utils.LinearCongruentialGenerator;
import com.vk.lgorsl.utils.sprites.SpriteBank;
import com.vk.lgorsl.utils.sprites.iRender;

import java.util.ArrayList;
import java.util.Random;

/**
 * Загружает абсолютно новый мир
 * Created by lgor on 03.05.14.
 */
public class NewWorldLoader implements iWorldLoader {

    private final SpriteBank sprites;
    private final int width, height;

    public NewWorldLoader(SpriteBank spriteBank, int width, int height){
        this.sprites = spriteBank;
        this.width = width;
        this.height = height;
    }

    @Override
    public World load(GameSession session) {
        BorderLine.circles = sprites.getCircles();
        ArrayList<LandType> landscape = new ArrayList<LandType>();
        landscape.add(new LandType(sprites.getSprite("grass"), 2, "Поле"));
        landscape.add(new LandType(sprites.getSprite("grass"), 4, "Лес", sprites.getSprite("forest")));
        landscape.add(new LandType(sprites.getSprite("hill"), 4, "Холм"));
        landscape.add(new LandType(iRender.NullRender.get() , 0, "Гора", sprites.getSprite("mountain")));
        landscape.get(2).landUpgrades.add(new LandUpgrade(sprites.getSprite("windmill"), "windmill"));
        landscape.get(0).landUpgrades.add(new LandUpgrade(sprites.getSprite("field"), "field"));

        Settlement.init(sprites);

        CustomRandom random = LinearCongruentialGenerator.getLikeNativeRandom();
        random.setSeed(0x1488);
        World world = new World(new PerlinMapConstructor(width, height, landscape, random));
        world.spriteBank = sprites;
        Country country = new Country(world, 1);
        UnitType crusader = new UnitType("crusader", 4, 2, 0, sprites.getSprite("crusader"));
        int x=width/2+height/4+6;
        int y=height/2-3;
        while(!world.map.getCell(x,y).accessible()){
            x++;
            y++;
        }
        country.createUnit(crusader, x, y);
        world.map.getCell(x, y).getUnit().buildCastle().apply();
        x+=2;
        y+=2;

        while(!world.map.getCell(x,y).accessible()){
            x++;
            y++;
        }
        country.createUnit(crusader, x, y);
        new Village(country, x, y);

        Country country2 =new Country(world, 2);
        country2.createUnit(crusader, 8, 8);

        world.map.getCell(8,8).getUnit().buildCastle().apply();


        new Village(country, 4, 4);

        world.addPlayer(new Gamer(session, country));
        //world.addPlayer(new PlayerAI(session, country));
        world.addPlayer(new PlayerAI(session, country2));
        return world;
    }

    @Override
    public boolean save(GameSession world) {
        return false;
    }
}
