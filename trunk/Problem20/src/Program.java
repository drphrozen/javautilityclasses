
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Program().run();

	}

	private void run() {
//		long sum = 1;
//		for(int i = 100; i > 1; i--)
//			sum*=i;
//		System.out.println(sum);
		NaturalNumber nn = new NaturalNumber(1);
		for (int i = 2; i <= 1000; i++) {
			NaturalNumber tmp = new NaturalNumber(0);
			for(int j = 0; j <i; j++) {
				tmp.add(nn);
			}
			nn = tmp;
		}
		System.out.println(nn);
		System.out.println(nn.sum());
	}

}
