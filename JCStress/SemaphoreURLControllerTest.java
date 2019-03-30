package bigdata.techniques.tools.semaphore;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;


public class SemaphoreURLControllerTest {

	@State
	public static class MyState extends SemaphoreURLController{};
	
	@JCStressTest
	@Description("Test SemaphoreURLController class")
	@Outcome(id="1, 2", expect = Expect.ACCEPTABLE)
	@Outcome(id="2, 1", expect = Expect.ACCEPTABLE)
	public static class StressTest1 {
		
		@Actor
		public void actor1 (MyState myState, II_Result r) {
			try {
				String value = myState.getNext();
				if(value.equals("a"))
					r.r1 = 1;
				else if (value.equals("b")) r.r1 =2;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		@Actor
		public void actor2 (MyState myState, II_Result r) {
			try {
				String value = myState.getNext();
				if(value.equals("a"))
					r.r2 = 1;
				else if (value.equals("b")) r.r2 =2;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
