//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class CustomBlockTest {
//
//	private CustomBlock block;
//
//	@Before
//    public void setUp() {
//		block = new CustomBlock();
//	}
//
//	@Test
//	public void testCustomBlock() {
//		assertNotNull(block);
//	}
//
//	@Test
//	public void testEqualsObject() {
//		CustomBlock block2 = new CustomBlock();
//		assertTrue(block.equals(block2));
//	}
//
//	@Test
//	public void testHashCode() {
//		assertTrue(block.hashCode()==31);
//	}
//
//	@Test
//	public void testGetBlockNumber() {
//		block.setNumber(1024l);
//		assertNotNull(block.getBlockNumber());
//	}
//
//	@Test
//	public void testGetTransactionList() {
//		assertNotNull(block.getTransactionList());
//	}
//
//}
