package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {

    /**
     * Алгоритм: симуляция пошагового боя по раундам.
     *
     * На каждом раунде:
     * 1) собираются все живые юниты обеих армий;
     * 2) сортируются по убыванию силы атаки;
     * 3) юниты ходят по очереди, пока бой не завершится.
     *
     * Алгоритмическая сложность:
     * Пусть n — общее число юнитов, R — число раундов боя.
     * На раунд:
     *  - сбор списка: O(n)
     *  - сортировка: O(n log n)
     *  - ходы юнитов: O(n)
     *
     * Итоговая сложность: O(R · n log n)
     */

    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    public SimulateBattleImpl() {
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {

        while (hasAlive(playerArmy) && hasAlive(computerArmy)) {

            List<Unit> turnOrder = new ArrayList<>();

            for (Unit u : playerArmy.getUnits()) {
                if (u.isAlive()) turnOrder.add(u);
            }
            for (Unit u : computerArmy.getUnits()) {
                if (u.isAlive()) turnOrder.add(u);
            }

            // сортировка по убыванию атаки
            turnOrder.sort((a, b) -> Integer.compare(
                    b.getBaseAttack(), a.getBaseAttack()
            ));

            for (Unit unit : turnOrder) {
                if (!unit.isAlive()) continue;

                Unit target = unit.getProgram().attack();

                if (target != null) {
                    printBattleLog.printBattleLog(unit, target);
                }

                // если одна из армий полностью выбита — бой закончен
                if (!hasAlive(playerArmy) || !hasAlive(computerArmy)) {
                    return;
                }
            }
        }
    }

    private boolean hasAlive(Army army) {
        for (Unit u : army.getUnits()) {
            if (u.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
