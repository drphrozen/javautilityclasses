public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Program().run();

	}

	private void run() {
		int sum = 0;
		for (int i = 1; i < 10000; i++) {
			if(isAmicable(i)) {
				sum += i;
				System.out.println(i);
			}
		}
		System.out.println("Sum: " + sum);
	}

	boolean isAmicable(final int a) {
		final int da = sumOfProperDivisors(a);
		final int b = da;
		final int db = sumOfProperDivisors(b);
		return (a != b && a == db);
	}

	int sumOfProperDivisors(final int number) {
		int max = number;
		int sum = 1;
		for (int i = 2; i < max; i++) {
			if (number % i == 0) {
				sum += i;
				int tmp = number / i;
				sum += tmp;
				max = tmp;
			}
		}
		return sum;
	}
}
