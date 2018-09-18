import org.junit.Assert;
import org.junit.Test;

public class Q2Test {

    private static final int OPERATIONS = 120000;

    @Test
    public void testFischer() {
        int result = q2.a.PIncrement.parallelIncrement(0, 4);
        System.out.println(result);
        // Fischer takes a long time, you could use a small value for test instead
        Assert.assertEquals(result, OPERATIONS);
    }

    @Test
    public void testLamport() {
        int result = q2.b.PIncrement.parallelIncrement(0, 4);
        System.out.println(result);
        Assert.assertEquals(result, OPERATIONS);
    }

    @Test
    public void testAnderson() {
        int result = q2.c.PIncrement.parallelIncrement(0, 8);
        System.out.println(result);
        Assert.assertEquals(result, OPERATIONS);
    }

}
