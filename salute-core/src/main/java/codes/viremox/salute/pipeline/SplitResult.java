package codes.viremox.salute.pipeline;

import java.util.List;

public record SplitResult(List<Tile> tiles, int columns, int rows) {

    public int tileCount() {
        return tiles.size();
    }

    public Tile tileAt(int col, int row) {
        for (Tile t : tiles) {
            if (t.column() == col && t.row() == row) return t;
        }
        return null;
    }
}
