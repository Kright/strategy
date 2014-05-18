package com.vk.lgorsl.GUI.panels;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.vk.lgorsl.GUI.MapCamera;
import com.vk.lgorsl.GUI.iRenderFeature;
import com.vk.lgorsl.gamelogic.world.Cell;
import com.vk.lgorsl.gamelogic.world.unit.Unit;
import com.vk.lgorsl.utils.Touch;
import com.vk.lgorsl.utils.sprites.RenderParams;
import com.vk.lgorsl.utils.sprites.SpriteBank;
import com.vk.lgorsl.utils.sprites.iRender;

import java.util.ArrayList;
import java.util.List;

/**
 * левая панель с кнопочками - командами юнитам
 * Created by lgor on 18.05.14.
 */
public class LeftButtonsPanel implements iRenderFeature, Touch.TouchListener {

    public enum ButtonState {
        nothing,
        wait,
        defence,
        buildCastle
    }

    private class Icon {
        iRender pic;
        ButtonState state;

        public Icon(iRender pic, ButtonState state) {
            this.pic = pic;
            this.state = state;
        }
    }

    private final static int size = 128;

    private final Icon[] icons;
    private final RenderParams params;
    private final iRender shadow;

    private ButtonState lastPressed = ButtonState.nothing;
    private List<Icon> visible = new ArrayList<Icon>(3);
    private int activeCount = -1;

    public LeftButtonsPanel(SpriteBank sb) {
        icons = new Icon[]{new Icon(sb.getSprite("buttonZz"), ButtonState.wait),
                new Icon(sb.getSprite("buttonShield"), ButtonState.defence),
                new Icon(sb.getSprite("buttonCastle"), ButtonState.buildCastle)};
        shadow = sb.getSprite("buttonShadow");
        params = new RenderParams(new Paint());
        params.setCellSize(size, size);
    }

    public void setUnit(Unit unit) {
        visible.clear();
        if (unit == null) {
            return;
        }
        if (unit.hasMovementPoints()) {
            visible.add(icons[1]);
            Cell c = unit.getCell();
            if (c.controlledByCastle() == null && !c.hasSettlement()) {
                visible.add(icons[2]);
            }
        }
    }

    public ButtonState getLastState() {
        return lastPressed;
    }

    public boolean onTouch(Touch t) {
        if (t.x > size || t.y > visible.size() * size) {
            if (t.lastTouch()) {
                lastPressed = ButtonState.nothing;
            }
            activeCount = -1;
            return false;
        }
        activeCount = (int) (t.y / size);
        lastPressed = visible.get(activeCount).state;
        if (t.lastTouch()) {
            activeCount = -1;
        }
        return true;
    }

    @Override
    public void render(MapCamera camera, Canvas canvas) {
        params.canvas = canvas;
        if (visible.isEmpty()) return;
        int y = 0;
        for (Icon icon : visible) {
            params.y = y;
            icon.pic.render(params);
            y += size;
        }
        if (activeCount != -1) {
            params.y = size * activeCount;
            shadow.render(params);
        }
    }
}
