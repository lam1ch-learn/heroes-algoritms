package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    /**
     * Алгоритм: BFS (поиск в ширину) на двумерной сетке.
     *
     * Используется BFS, так как:
     * - все перемещения имеют одинаковую стоимость;
     * - требуется найти кратчайший путь;
     * - размеры поля фиксированы (27 × 21).
     *
     * Разрешено движение в 8 направлениях (включая диагонали).
     * Клетки, занятые другими живыми юнитами, считаются препятствиями.
     *
     * Алгоритмическая сложность:
     * Пусть WIDTH = 27, HEIGHT = 21.
     *
     * В худшем случае посещаются все клетки поля.
     *
     * Итоговая сложность: O(WIDTH × HEIGHT)
     */

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    private static final int[][] DIRS = {
            { 1, 0}, {-1, 0}, {0, 1}, {0,-1},
            { 1, 1}, { 1,-1}, {-1, 1}, {-1,-1}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        int sx = attackUnit.getxCoordinate();
        int sy = attackUnit.getyCoordinate();
        int tx = targetUnit.getxCoordinate();
        int ty = targetUnit.getyCoordinate();

        boolean[][] blocked = new boolean[WIDTH][HEIGHT];
        for (Unit u : existingUnitList) {
            if (!u.isAlive()) continue;
            if (u == attackUnit || u == targetUnit) continue;

            blocked[u.getxCoordinate()][u.getyCoordinate()] = true;
        }

        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] parent = new Edge[WIDTH][HEIGHT];

        Queue<Edge> queue = new ArrayDeque<>();
        queue.add(new Edge(sx, sy));
        visited[sx][sy] = true;

        while (!queue.isEmpty()) {
            Edge cur = queue.poll();

            if (cur.getX() == tx && cur.getY() == ty) {
                return buildPath(parent, cur);
            }

            for (int[] d : DIRS) {
                int nx = cur.getX() + d[0];
                int ny = cur.getY() + d[1];

                if (!inBounds(nx, ny)) continue;
                if (blocked[nx][ny]) continue;
                if (visited[nx][ny]) continue;

                visited[nx][ny] = true;
                parent[nx][ny] = cur;
                queue.add(new Edge(nx, ny));
            }
        }

        return Collections.emptyList();
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    private List<Edge> buildPath(Edge[][] parent, Edge end) {
        LinkedList<Edge> path = new LinkedList<>();
        Edge cur = end;

        while (cur != null) {
            path.addFirst(cur);
            cur = parent[cur.getX()][cur.getY()];
        }

        return path;
    }
}
