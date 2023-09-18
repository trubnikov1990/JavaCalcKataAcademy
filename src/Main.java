import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

		String result = calc(input);
		System.out.println(result);
	}

	public static String calc(String input) {

		// Убираем пробелы в начале и в конце
		input = input.trim();

		//  Находим индекс оператора
		int operatorIndex = -1;
		char[] operators = {'+', '-', '*', '/'};
		for (char operator : operators) {
			operatorIndex = input.indexOf(operator);
			if (operatorIndex != -1) {
				break;
			}
		}

		//Проводим проверку, что слева и справа имеются значения
		if (operatorIndex == -1 || operatorIndex == 0 || operatorIndex == input.length() - 1) {
			throw new IllegalArgumentException("Неверный ввод данных");
		}

		

		// Получаем значения
		String value1 = input.substring(0, operatorIndex).trim();
		String value2 = input.substring(operatorIndex + 1).trim();
		String operator = String.valueOf(input.charAt(operatorIndex));

		// Проверяем не используются ли одновременно разные системы счисления
		boolean val1 = Numerals.determine(value1);
		boolean val2 = Numerals.determine(value2);
		if ((val1 && !val2) || (!val1 && val2)) {
			throw new IllegalArgumentException("Используются одновременно разные системы счисления");
		}

		// Приводим полученные значения в int.
		int num1;
		if (val1) {
			num1 = Numerals.arabConvector(value1);
		} else {
			num1 = Integer.parseInt(value1);
			if (num1 < 1 || num1 > 10) {
				throw new IllegalArgumentException("Арабские числа должны быть от 1 до 10");
			}
		}

		int num2;
		if (val2) {
			num2 = Numerals.arabConvector(value2);
		} else {
			num2 = Integer.parseInt(value2);
			if (num2 < 1 || num2 > 10) {
				throw new IllegalArgumentException("Арабские числа должны быть от 1 до 10");
			}
		}

		// С помощью метода switch производим арифметические вычисления.
		int result = switch (operator) {
			case "+" -> num1 + num2;
			case "-" -> num1 - num2;
			case "*" -> num1 * num2;
			case "/" -> {
				if (num2 == 0) {
					throw new IllegalArgumentException("Делить на нуль нельзя");
				}
				yield num1 / num2;
			}
			default -> 0;
		};

		// Полученный результат приводим к единой системе счисления с введёнными данными
		if (val1) {
			return Numerals.romanConvector(result);
		} else {
			return String.valueOf(result);
		}
	}
}

class Numerals {
	private static final String[] ROMAN = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
	private static final int[] ARAB = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

	/* Данный метод определяет, есть ли в веденных данных римские числа.
	 * Цикл пробегает по массиву и сравнивает значения массива ROMAN c полученными данными, если такие есть то возвращается true
	 */
	public static boolean determine(String input) {
		for (String val : ROMAN) {
			if (val.equals(input)) {
				return true;
			}
		}
		return false;
	}

	/* Если в параметр поступает римское число, то метод сравнивает в цикле значения массива ROMAN с поступившим числом,
	 *	после возвращает арабское число под индексом с массива ARAB.
	 */
	public static int arabConvector(String roman) {
		for (int i = 0; i < ROMAN.length; i++) {
			if (ROMAN[i].equals(roman)) {
				return ARAB[i];
			}
		}
		throw new IllegalArgumentException("Недопустимое римское число");
	}

	/* Если в параметр поступает арабское число, то метод возвращает римское число со значением индекса равного
	 * поступившему арабскому числу минус один т.к. нумерация в массиве начинается с нуля
	 */
	public static String romanConvector(int arab) {
		if (arab<1){
			throw new IllegalArgumentException("В римской системе нет отрицательных чисел");
		}
		return ROMAN[arab - 1];
	}
}