package bigdata.TFidF.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.LL_Result;
import bigdata.techniques.tools.semaphore.SemaphoreStringController;


public class SemaphoreStringControllerTest {

	@State
	public static class MyState extends SemaphoreStringController{};
	
	@JCStressTest
	@Description("Test SemaphoreURLController class")
	@Outcome(id="a, b", expect = Expect.ACCEPTABLE)
	@Outcome(id="b, a", expect = Expect.ACCEPTABLE)
	public static class StressTest1 {
		
		@Actor
		public void actor1 (MyState myState, LL_Result r) {
			try {
				r.r1 = myState.getNext();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		@Actor
		public void actor2 (MyState myState, LL_Result r) {
			try {
				r.r2 = myState.getNext();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
