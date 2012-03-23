/**
 * 
 */
package performance;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author li.li
 * 
 *         Mar 23, 2012
 * 
 */
public class SmokeTest {
	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	/**
	 * 参数地址:http://databene.org/contiperf<br/>
	 * In the example the test is configured to be executed 1000 times with 20
	 * concurrent threads, so each thread does 50 invocations. A maximum
	 * execution time of 1.2 seconds and and an average below or equals 250
	 * milliseconds are tolerated.
	 * 
	 * 
	 * 
	 */
	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 250)
	public void test1() throws Exception {
		Thread.sleep(200);
	}
}
