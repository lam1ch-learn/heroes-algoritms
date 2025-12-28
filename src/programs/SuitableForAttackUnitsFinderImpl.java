//package programs;
//
//import com.battle.heroes.army.Unit;
//import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
//
//    @Override
//    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
//        List<Unit> suitableUnit = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++){
//            List<Unit> currentRow = unitsByRow.get(i);
//            if (currentRow == null || currentRow.isEmpty()) {
//                continue;
//            }
//
//            for (Unit u: currentRow){
//                if (!u.isAlive()){
//                    continue;
//                }
//                if (i == 0) {
//                    suitableUnit.add((u));
//                    continue;
//                }
//
//                List<Unit> prevRow = unitsByRow.get(i - 1);
//                int y = u.getyCoordinate();
//
//                switch (i){
//                    case 1:
//                        for (Unit unit: prevRow){
//                            if (!unit.isAlive()){
//                                continue;
//                            }
//                            int checkY =  unit.getyCoordinate();
//                            if ((checkY >= (y - 1)) && (checkY <= (y + 1))){
//                                continue;
//                            } else {
//	                            suitableUnit.add(u);
//                            }
//                        }
//	                    break;
//                    case 2:
//                        for (Unit unit: prevRow){
//                            if (!unit.isAlive()){
//                                continue;
//                            }
//
//                        }
//                        break;
//	                default:
//		                throw new IllegalStateException("Unexpected value: " + i);
//                }
//            }
//        }
//    return suitableUnit;
//    }
//}
package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {

        /**
         * Алгоритм: линейная фильтрация юнитов по рядам.
         *
         * Идея:
         * - первый ряд всегда доступен для атаки;
         * - юниты второго и третьего ряда доступны,
         *   только если не перекрыты юнитами предыдущего ряда
         *   по вертикали или диагонали (y-1, y, y+1).
         *
         * Алгоритмическая сложность:
         * Пусть n — общее число юнитов противника.
         * Количество рядов фиксировано (3).
         *
         * Итоговая сложность: O(n)
         */

        List<Unit> result = new ArrayList<>();

        for (int row = 0; row < unitsByRow.size(); row++) {
            List<Unit> currentRow = unitsByRow.get(row);
            if (currentRow == null || currentRow.isEmpty()) {
                continue;
            }

            for (Unit unit : currentRow) {
                if (!unit.isAlive()) {
                    continue;
                }

                // первый ряд всегда доступен для атаки
                if (row == 0) {
                    result.add(unit);
                    continue;
                }

                int y = unit.getyCoordinate();
                boolean blocked = false;

                List<Unit> blockingRow = unitsByRow.get(row - 1);
                if (blockingRow != null) {
                    for (Unit blocker : blockingRow) {
                        if (!blocker.isAlive()) {
                            continue;
                        }

                        int by = blocker.getyCoordinate();

                        // проверяем соседние клетки по y
                        if (Math.abs(by - y) <= 1) {
                            blocked = true;
                            break;
                        }
                    }
                }

                if (!blocked) {
                    result.add(unit);
                }
            }
        }

        return result;
    }
}
