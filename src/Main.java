/*
Описание:
Создай консольное приложение “Калькулятор”. Приложение должно читать из консоли введенные пользователем строки, числа, арифметические операции проводимые
между ними и выводить в консоль результат их выполнения.
Реализуй класс Main с методом public static String calc(String input). Метод должен принимать строку с арифметическим выражением
между двумя числами и возвращать строку с результатом их выполнения.
Ты можешь добавлять свои импорты, классы и методы. Добавленные классы не должны иметь модификаторы доступа (public или другие)

Требования:
* Калькулятор умеет выполнять операции сложения, вычитания, умножения и деления с двумя числами: a + b, a - b, a * b, a / b.
Данные передаются в одну строку (смотри пример)! Решения, в которых каждое число и арифмитеческая операция передаются с новой строки считаются неверными.
* Калькулятор умеет работать как с арабскими (1,2,3,4,5…), так и с римскими (I,II,III,IV,V…) числами.
* Калькулятор должен принимать на вход числа от 1 до 10 включительно, не более. На выходе числа не ограничиваются по величине и могут быть любыми.
* Калькулятор умеет работать только с целыми числами.
* Калькулятор умеет работать только с арабскими или римскими цифрами одновременно, при вводе пользователем строки вроде 3 + II
калькулятор должен выбросить исключение и прекратить свою работу.
* При вводе римских чисел, ответ должен быть выведен римскими цифрами, соответственно, при вводе арабских - ответ ожидается арабскими.
* При вводе пользователем неподходящих чисел приложение выбрасывает исключение и завершает свою работу.
При вводе пользователем строки, не соответствующей одной из вышеописанных арифметических операций, приложение выбрасывает исключение и завершает свою работу.
* Результатом операции деления является целое число, остаток отбрасывается.
* Результатом работы калькулятора с арабскими числами могут быть отрицательные числа и ноль.
* Результатом работы калькулятора с римскими числами могут быть только положительные числа, если результат работы меньше единицы, выбрасывается исключение
 */





import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

		try {
			String result = calc(input);
			System.out.println(result);
		}catch (IllegalArgumentException e) {
			System.out.println(" Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
		}

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
			return Numerals.RomanNumeral.generateIntermediateValues(result);
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
	public enum RomanNumeral {
		I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100);

		private int value;

		RomanNumeral(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static String generateIntermediateValues(int targetValue) {
			StringBuilder result = new StringBuilder();
			RomanNumeral[] values = RomanNumeral.values();
			int index = values.length - 1;

			while (targetValue > 0 && index >= 0) {
				RomanNumeral currentSymbol = values[index];

				if (targetValue >= currentSymbol.value) {
					result.append(currentSymbol.name());
					targetValue -= currentSymbol.value;
				} else {
					index--;
				}
			}

			return result.toString();
		}
	}
}
