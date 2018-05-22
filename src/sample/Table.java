package sample;

public class Table {

    private int[] table;

    public Table(int width, int height){
        table = new int[width*height];

        for(int i = 0; i < table.length; i++){
            table[i] = 0;
        }
    }

    public int get(int index){
        return table[index];
    }

    public void set(int index, int value){
        table[index] = value;
    }
}
