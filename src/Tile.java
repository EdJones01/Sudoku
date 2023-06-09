public class Tile {
    private int value;
    private final boolean[] pencil = new boolean[9];

    public Tile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean[] getPencil() {
        return pencil;
    }

    public void addPencil(int number) {
        pencil[number-1] = true;
    }

    public void removePencil(int number) {
        pencil[number-1] = false;
    }
}
