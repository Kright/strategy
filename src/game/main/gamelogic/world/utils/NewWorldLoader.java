package game.main.gamelogic.world.utils;
import game.main.gamelogic.GameSession;
import game.main.gamelogic.userinput.Gamer;
import game.main.gamelogic.world.*;
import game.main.utils.sprites.SpriteBank;

import java.util.ArrayList;

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
        ArrayList<LandType> landscape = new ArrayList<LandType>();
        landscape.add(new LandType(sprites.getSprite("grass"), 2, "Поле"));
        landscape.add(new LandType(sprites.getSprite("grass"), 4, "Лес", sprites.getSprite("forest")));
        landscape.add(new LandType(sprites.getSprite("hill"), 4, "Холм"));
        landscape.get(2).landUpgrades.add(new LandUpgrade(sprites.getSprite("windmill"), "windmill"));
        landscape.get(0).landUpgrades.add(new LandUpgrade(sprites.getSprite("field"), "field"));

        Settlement.init(sprites);

        World world = new World(width, height, landscape);
        world.spriteBank = sprites;

        Country country = new Country(world, 1);

        UnitType crusader = new UnitType(4, 2, 0, sprites.getSprite("crusader"));
        country.createUnit(crusader, 2, 2);
        country.createUnit(crusader, 4, 4);

        world.map.getCell(2, 2).getUnit().buildCastle().apply();
        new Village(country, 4, 4);

        world.addPlayer(new Gamer(session, country));

        return world;
    }

    @Override
    public boolean save(World world) {
        return false;
    }
}
