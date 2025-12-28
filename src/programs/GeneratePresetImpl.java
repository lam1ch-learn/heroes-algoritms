package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneratePresetImpl implements GeneratePreset {

    /**
     * Алгоритм: жадный (greedy) подбор армии.
     *
     * 1) Типы юнитов сортируются по эффективности:
     *    - сначала по отношению атаки к стоимости,
     *    - затем по отношению здоровья к стоимости.
     * 2) Юниты добавляются в армию, пока:
     *    - хватает очков,
     *    - не превышен лимит юнитов одного типа.
     *
     * Ограничения:
     * - не более 11 юнитов каждого типа;
     * - суммарная стоимость не превышает maxPoints.
     *
     * Алгоритмическая сложность:
     * Пусть t — число типов юнитов (константа, t = 4),
     * m — максимальное число юнитов в армии.
     *
     *  - сортировка типов: O(t log t) ≈ O(1)
     *  - добавление юнитов: O(m)
     *
     * Итоговая сложность: O(m)
     */

    public static final int BATTLEFIELD_Y_SIZE = 21;
    public static final int MAX_UNITS_PER_TYPE = 11;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {

        Army myWarriors = new Army(new ArrayList<>());


        for (Unit u : unitList) {
            System.out.println("  " + u.getUnitType() + ": cost=" + u.getCost() +
                    ", attack=" + u.getBaseAttack());

            System.out.println("🔥 МОЙ GeneratePreset ВЫЗВАН! maxPoints=" + maxPoints);
            System.out.println("Units available: " + unitList.size());
        }

        // 1. Сортируем типы по эффективности (attack/cost, потом health/cost). Вместо reverse используем "-"
        List<Unit> sortedTypes = unitList.stream()
                .sorted(Comparator.<Unit>comparingDouble(u -> (double) (-u.getBaseAttack() / u.getCost()) * 2)
                        .thenComparingDouble(u -> (double) -u.getHealth() / u.getCost())
                ).toList();

        System.out.println("🔥 МОИ отсортированные юниты! maxPoints=" + maxPoints);
        System.out.println("Units available: " + sortedTypes.size());

        for (Unit u : sortedTypes) {
            System.out.println("  " + u.getUnitType() + ": cost=" + u.getCost() +
                    ", attack=" + u.getBaseAttack() + ", health=" + u.getHealth());
        }

        List<Unit> result = new ArrayList<>();
        int remainingPoints = maxPoints;

        for (Unit type : sortedTypes) {
            int count = 0;
            int x = switch (type.getUnitType()) {
	            case "Knight" -> 2;
	            case "Swordsman", "Pikeman" -> 1;
	            default -> 0;
            };
            while (count < MAX_UNITS_PER_TYPE && remainingPoints >= type.getCost()) {
                String name = type.getUnitType() + " " + count;
                int y = switch(x) {
                    case 0, 2 -> count + 2;
	                case 1 -> (type.getUnitType().equals("Swordsman")) ? 2 * count : (2 * count + 1) % BATTLEFIELD_Y_SIZE;
	                default -> throw new IllegalStateException("Unexpected value: " + x);
                };
                // добавляем копию юнита данного типа
                result.add(new Unit(name,
                        type.getUnitType(),
                        type.getHealth(),
                        type.getBaseAttack(),
                        type.getCost(),
                        type.getAttackType(),
                        type.getAttackBonuses(),
                        type.getDefenceBonuses(),
                        (2 * count + 2 > BATTLEFIELD_Y_SIZE && type.getUnitType().equals("Pikeman")) ? 2 : x,
		                y
                ));
                System.out.println("Юнит добавлен: " + name);

                remainingPoints -= type.getCost();
                count++;
            }
        }

        myWarriors.setUnits(result);
        System.out.println("🔥 МОЙ GeneratePreset ВЕРНУЛ армию!\n ЧИСЛЕННОСТЬ: " + result.toArray().length);

        return myWarriors;
    }
}