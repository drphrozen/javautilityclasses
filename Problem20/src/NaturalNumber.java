import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * This class handles simples math operations on large natural numbers.
 * @author drphrozen
 *
 */
public class NaturalNumber {
	static final int INTEGER_WIDTH = 9;
	static final int LIMIT = 1000000000;
	
	final LinkedList<Integer> data = new LinkedList<Integer>();

	public NaturalNumber() {
		data.add(0);
	}
	
	public NaturalNumber(NaturalNumber nn) {
		for (int i : nn.data) {
			data.add(i);
		}
	}
	
	public NaturalNumber(int value) {
		if(value >= LIMIT)
		{
			data.add(value%LIMIT);
			data.add(value/LIMIT);
		} else
		{
			data.add(value);
		}
	}

	public NaturalNumber add(NaturalNumber value) {
		ListIterator<Integer> iter = data.listIterator();
		Iterator<Integer> otherIter = value.data.iterator();
		int carry = 0;
		for (; otherIter.hasNext() && iter.hasNext();) {
			int integer = iter.next();
			int otherInteger = otherIter.next();
			int tmp = integer + otherInteger + carry;
			iter.set(tmp%LIMIT);
			carry = tmp/LIMIT;
		}
		for (; otherIter.hasNext() ;) {
			int otherInteger = otherIter.next();
			int tmp = otherInteger + carry;
			iter.add(tmp%LIMIT);
			carry = tmp/LIMIT;
		}
		if(carry > 0) {
			if(iter.hasNext())
				iter.set(iter.next() + carry);
			else
				iter.add(carry);
		}
		return this;
	}
	
	public NaturalNumber sum() {
		NaturalNumber sum = new NaturalNumber();
		for (int i : data) {
			for(int j = 0; j<INTEGER_WIDTH; j++) {
				sum.add(new NaturalNumber(i%10));
				i /= 10;
			}
		}
		return sum;
	}

	@Override
	public String toString() {
		final int size = data.size();
		StringBuilder stringBuilder = new StringBuilder(size*INTEGER_WIDTH);
		ListIterator<Integer> iter = data.listIterator(size);
		if(iter.hasPrevious())
		{
			Integer value = iter.previous();
			stringBuilder.append(value);
			while(iter.hasPrevious())
			{
				value = iter.previous();
				stringBuilder.append(String.format("%09d", value));
			}
		}
		return stringBuilder.toString();
	}
}
