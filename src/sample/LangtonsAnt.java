package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.List;


public class LangtonsAnt implements CelluralAutomata {

    private int width;
    private int height;
    private Table list;
    private List<Ant> ants = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();

    public LangtonsAnt(int width, int height){
        this.width = width;
        this.height = height;

        list = new Table(width, height);

    }

    public void process(){
        int i = 1;
        for(Ant ant: ants)
        {
            if(getElement(ant.getX(), ant.getY()) == 0) {
                setElement(ant.getX(), ant.getY(), i);
                turnLeft(ant);
            }
            else{
                setElement(ant.getX(), ant.getY(), 0);
                turnRight(ant);
            }
            i++;

        }
    }

    public void turnLeft(Ant ant) {
        switch (ant.getDirection()) {
            case UP:
                if(ant.getX() > 0)
                    ant.setX(ant.getX()-1);
                else
                    ant.setX(width-1);
                ant.setDirection(Direction.LEFT);
                break;
            case DOWN:
                if(ant.getX() < width - 1)
                    ant.setX(ant.getX()+1);
                else
                    ant.setX(0);
                ant.setDirection(Direction.RIGHT);

                break;
            case LEFT:
                if(ant.getY() < height - 1)
                    ant.setY(ant.getY()+1);
                else
                    ant.setY(0);
                ant.setDirection(Direction.DOWN);

                break;
            case RIGHT:
                if(ant.getY() > 0)
                    ant.setY(ant.getY()-1);
                else
                    ant.setY(height-1);
                ant.setDirection(Direction.UP);

                break;
        }
    }

    public void turnRight(Ant ant){
            switch (ant.getDirection()) {
                case UP:
                    if (ant.getX() < width - 1)
                        ant.setX(ant.getX() + 1);
                    else
                        ant.setX(0);
                    ant.setDirection(Direction.RIGHT);
                    break;
                case DOWN:
                    if (ant.getX() > 0)
                        ant.setX(ant.getX() - 1);
                    else
                        ant.setX(width-1);
                    ant.setDirection(Direction.LEFT);

                    break;
                case LEFT:
                    if (ant.getY() > 0)
                        ant.setY(ant.getY() - 1);
                    else
                        ant.setY(height-1);
                    ant.setDirection(Direction.UP);

                    break;
                case RIGHT:
                    if (ant.getY() < height - 1)
                        ant.setY(ant.getY() + 1);
                    else
                        ant.setY(0);
                    ant.setDirection(Direction.DOWN);

                    break;
            }

    }

    public int getElement(int x, int y)
    {
        return list.get(x*height + y);
    }

    public void setElement(int x, int y, int value)
    {
        list.set(x*height + y, value);
    }

    public void editCanvas(Canvas canvas){

        double scaleX = canvas.getWidth()/width;
        double scaleY = canvas.getHeight()/height;
        int tmp = 0;

        for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    tmp = getElement(i, j);
                    if (tmp == 0) {
                        canvas.getGraphicsContext2D().setFill(Color.BLACK);
                        canvas.getGraphicsContext2D().fillRect(i * scaleX, j * scaleY, scaleX, scaleY);
                    } else {
                        canvas.getGraphicsContext2D().setFill(colors.get(tmp - 1));
                        canvas.getGraphicsContext2D().fillRect(i * scaleX, j * scaleY, scaleX, scaleY);
                    }

                }
            }

    }

    public void addElement(int x, int y, Color color ,Direction direction){
        ants.add(new Ant(x, y, direction));
        setElement(x, y, ants.size());
        colors.add(color);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void restart(){
        list = new Table(width, height);
        colors = new ArrayList<>();
        ants = new ArrayList<>();
    }
}

