package tests.game.model.item;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import game.model.jaxb.*;
import game.model.Item;
import game.model.Item.*;

/**
 * This test case is to test the creation of item
 * 
 * @author Priyanka A
 * @author Shikha Jhamb
 * @version 1.0
 * @since 2/28/2017
 *
 */

public class ItemTestCase2{
    
    private static Item item;
    private static String name;
    
    @Before 
    public void beforeEachTest(){
        name = "Dagger";
        item = new Item();
    }
    
    @Test
    public void createItem(){
        item = ItemJaxb.getItemFromXml(name);
        assertEquals(name, item.getItemName());
        
    }

}