package venus.helper.middle;

import org.springframework.stereotype.Component;

@Component
public class Count {
	private int count=0;
	public void init(int count){
		this.count=count;
	}
	public int count(){
		return this.count;
	}
	public void plus(){
		count++;
	}
	public void reduce(){
		count--;
	}
	
}
