package sensordata.app.utils;
/**
 * helper class to store triple string data
 * @author Anton Kroisant
 *
 * @param <F>
 * @param <S>
 * @param <T>
 */
public class Triple<F, S, T> {
	public final F first;
	public final S second;
	public final T third;

	public Triple(F first, S second, T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Override
	public String toString() {
		String s = "";
		s +=first + "\n";
		s +=second + "\n";
		s +=third + "\n";
		return s;
	}
}
