package game.main.gamelogic;

import android.graphics.Canvas;
import android.util.FloatMath;
import game.main.gamelogic.world.*;
import game.main.utils.Touch;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, обрабатывающий действия игрока - нажатия на экран и сворачивание приложения
 * TODO добавить действия игрока
 * На данный момент реализовано только перемещение камеры
 * В будущем - кнопочки, выделение, перемещение юнитов нажатием, переход в меню города.
 * Created by lgor on 17.04.14.
 */
public class Gamer extends Player {

    /**
     * состояние интерфейса
     */
    private abstract class State {
        public abstract State getNext();

        public void paint(Canvas canvas, MapRender render) {
            render.render(canvas, country.map);
        }
    }

    private boolean turnFunished;
    private State state;
    private MapRender camera;
    private List<Touch> touches = new ArrayList<Touch>();

    public Gamer(GameSession session, Country country) {
        super(session, country);
        state = doNothing;
    }

    @Override
    protected void doTurn(MapRender render) {
        this.camera = render;
        turnFunished = false;
        while (!turnFunished) {
            state = state.getNext();
        }
    }

    @Override
    public void paint(Canvas canvas, MapRender render) {
        state.paint(canvas, render);
    }

    protected void updateTouches() {
        touches.addAll(session.getTouches());
    }

    private final State doNothing = new State() {
        @Override
        public State getNext() {
            session.safeRepaint();
            if (touches.isEmpty()) {
                waitAndUpdate();
                session.safeRepaint();
                return this;
            }
            Touch t = touches.get(0);
            Cell c = camera.getCell(country.map, t.x, t.y);
            if (c.hasUnit()) {
                activeUnit.setUnit(c.getUnit());
                return activeUnit;
            }
            return mapMoving;
        }
    };

    private final State mapMoving = new State() {
        float vx
                ,
                vy;       //накопители для перемещений за кадр
        float vx2
                ,
                vy2;     //итоговые значения

        @Override
        public State getNext() {
            for (; ; ) {
                assert !touches.isEmpty();
                Touch t = touches.remove(0);
                if (t.count() == 1) {
                    vx -= t.dx();
                    vy -= t.dy();
                    camera.move(-t.dx(), -t.dy());
                    if (t.lastTouch()) {
                        camInertia.set(vx2, vy2);
                        return camInertia;
                    }
                } else if (t.count() > 1) {
                    Touch t2 = t.next;
                    camera.move((-t.dx() - t2.dx()) / 2, (-t.dy() - t2.dy()) / 2);
                    float scale = FloatMath.sqrt(len2(t.x - t2.x, t.y - t2.y) /
                            len2(t.oldX() - t2.oldX(), t.oldY() - t2.oldY()));
                    camera.scale(scale, (t.x + t2.x) / 2, (t.y + t2.y) / 2);
                }
                if (touches.isEmpty()) {
                    vx2 = vx;
                    vy2 = vy;
                    vx = 0;
                    vy = 0;
                    session.repaint();
                    while (touches.isEmpty()) {
                        waitAndUpdate();
                    }
                }
            }
        }
    };

    private final CamInertia camInertia = new CamInertia();

    private class CamInertia extends State {
        float vx, vy, k = 0.9f, minV2;

        public void set(float dx, float dy) {
            vx = dx;
            vy = dy;
            minV2 = 50 * FloatMath.sqrt(camera.getScreenWidth() / 1920f);
        }

        @Override
        public State getNext() {
            for (; ; ) {
                if (!touches.isEmpty() || len2(vx, vy) < minV2) {
                    return doNothing;
                }
                vx *= k;
                vy *= k;
                camera.move(vx, vy);
                session.repaint();
                updateTouches();
                if (session.checkPause()) {
                    return doNothing;
                }
            }
        }
    }

    private final ActiveUnit activeUnit = new ActiveUnit();

    private class ActiveUnit extends State {
        Unit unit;
        AlternativeWay way;

        public void setUnit(Unit unit) {
            this.unit = unit;
            way = new AlternativeWay(country.map.getTrueMap(), unit);
        }

        @Override
        public State getNext() {
            session.repaint();
            Touch t;
            while (!(t = waitTouches()).lastTouch()) ;
            Cell c = getTrueCell(t);
            session.needUpdate(true);
            if (way.isInto(c)) {
                way.getMoveTo(c).apply();
                return doNothing;
            }
            if (unit.getCell() == c) {
                secondActive.setUnit(unit, way);
                return secondActive;
            }
            return doNothing;
        }

        @Override
        public void paint(Canvas canvas, MapRender render) {
            camera.render(canvas, country.map, way);
        }
    }

    private final SecondActiveUnit secondActive = new SecondActiveUnit();

    private class SecondActiveUnit extends State {
        Unit unit;
        AlternativeWay way;

        public void setUnit(Unit unit, AlternativeWay way) {
            this.unit = unit;
            this.way = way;
        }

        @Override
        public State getNext() {
            Touch t;
            while (!(t = waitTouches()).firstTouch()) ;
            Cell c = getTrueCell(t);
            if (!way.isInto(c) && unit.getCell() != c) {
                session.needUpdate(true);
                return doNothing;
            }
            while (!(t = waitTouches()).lastTouch()) ;
            c = camera.getCell(country.map.getTrueMap(), t.x, t.y);
            if (unit.getCell() == c) {
                session.needUpdate(true);
                return doNothing;
            }
            if (way.isInto(c)) {
                way.getMoveTo(c).apply();
                if (!unit.hasMovementPoints()) {
                    session.needUpdate(true);
                    return doNothing;
                }
                way = new AlternativeWay(country.map.getTrueMap(), unit);
                session.repaint();
                return this;
            }
            session.needUpdate(true);
            return doNothing;
        }

        @Override
        public void paint(Canvas canvas, MapRender render) {
            camera.render(canvas, country.map, way);
        }
    }

    private Cell getTrueCell(Touch t) {
        return camera.getCell(country.map.getTrueMap(), t.x, t.y);
    }

    private Touch waitTouches() {
        while (touches.isEmpty()) {
            waitAndUpdate();
        }
        return touches.remove(0);
    }

    private void waitAndUpdate() {
        if (!session.checkPause()) {
            session.sleep(20);
        }
        updateTouches();
    }

    private static float len2(float x, float y) {
        return (x * x + y * y);
    }
}
