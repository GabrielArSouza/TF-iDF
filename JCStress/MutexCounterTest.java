package bigdata.techniques.tools.mutex;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.III_Result;
import org.openjdk.jcstress.infra.results.II_Result;

public class MutexCounterTest {

	@State
	public static class MyState extends MutexCounter{
		public MyState() {
			super(2);
		}
	}
	
	@JCStressTest
	@Description("Test MutexCounter class")
	@Outcome(id="0, 1, 2", expect = Expect.ACCEPTABLE)
	@Outcome(id="0, 2, 1", expect = Expect.ACCEPTABLE)
	@Outcome(id="1, 2, 0", expect = Expect.ACCEPTABLE)
	@Outcome(id="1, 0, 2", expect = Expect.ACCEPTABLE)
	@Outcome(id="2, 0, 1", expect = Expect.ACCEPTABLE)
	@Outcome(id="2, 1, 0", expect = Expect.ACCEPTABLE)
	public static class StressTest1 {
		@Actor
		public void actor1 (MyState myState, III_Result r) {
			r.r1 = myState.increment();
		}
		
		@Actor
		public void actor2 (MyState myState, III_Result r) {
			r.r2 = myState.increment();
		}
		
		@Actor
		public void actor3 (MyState myState, III_Result r) {
			r.r3 = myState.increment();
		}
	
	}

	
	
}
